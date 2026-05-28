package com.example.unilibrary.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unilibrary.R;
import com.example.unilibrary.db.AppDatabase;
import com.example.unilibrary.db.dao.UserDao;
import com.example.unilibrary.model.Book;
import com.example.unilibrary.model.User;
import com.example.unilibrary.service.Session;
import com.example.unilibrary.ui.adapter.BookAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {
    private UserDao userDao;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        userDao = AppDatabase.getInstance(this).userDao();
        loadUserData();
        setupNavigation();
        setupLogout();
    }

    private void setupLogout() {
        findViewById(R.id.btnLogout).setOnClickListener(v -> {
            Session.end(this);
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void loadUserData() {
        userId = Session.getUserId(this);
        if (userId == -1) {
            Toast.makeText(this, "Usuário não logado", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        userDao.searchForId(userId).observe(this, user -> {
            if (user != null) {
                populateUI(user);
            }
        });
    }

    private void populateUI(User user) {
        TextView tvName = findViewById(R.id.profileName);
        TextView tvEmail = findViewById(R.id.profileEmail);
        TextView tvBooksRead = findViewById(R.id.tvBooksReadCount);
        TextView tvSavedBooks = findViewById(R.id.tvSavedBooksCount);
        TextView tvActiveLoans = findViewById(R.id.tvActiveLoansCount);
        TextView tvCourse = findViewById(R.id.profileCourse);
        ImageView ivAvatar = findViewById(R.id.ivProfileAvatar);

        if (tvName != null) tvName.setText(user.getName());
        if (tvEmail != null) tvEmail.setText(user.getEmail());
        if (tvBooksRead != null) tvBooksRead.setText(String.valueOf(user.readBooks));
        if (tvSavedBooks != null) tvSavedBooks.setText(String.valueOf(user.getSavedBooksCount()));
        if (ivAvatar != null && user.getAvatarResId() != 0)  // ← novo
            ivAvatar.setImageResource(user.getAvatarResId());// ← novo
        if (tvCourse != null) tvCourse.setText(user.getCourse());

        if (tvActiveLoans != null) {
            AppDatabase.getInstance(this).loanDao()
                    .findAssetsWithBook(userId)
                    .observe(this, loans -> {
                        if (loans != null)
                            tvActiveLoans.setText(String.valueOf(loans.size()));
                    });
        }
        // busca os livros salvos pelo ID
        if (user.getSavedBookIds() != null && !user.getSavedBookIds().isEmpty()) {
            new Thread(() -> {
                // extrai os IDs da string "[1][2][3]"
                List<Book> savedBooks = new ArrayList<>();
                String ids = user.getSavedBookIds();
                java.util.regex.Matcher matcher = java.util.regex.Pattern
                        .compile("\\[(\\d+)\\]").matcher(ids);
                while (matcher.find()) {
                    int bookId = Integer.parseInt(matcher.group(1));
                    Book book = AppDatabase.getInstance(this).bookDao().searchById(bookId);
                    if (book != null) savedBooks.add(book);
                }
                runOnUiThread(() -> {
                    RecyclerView rvSaved = findViewById(R.id.rvSavedBooks);
                    BookAdapter savedAdapter = new BookAdapter(book -> {
                        Intent intent = new Intent(this, DetailActivity.class);
                        intent.putExtra("book_id", book.getId());
                        startActivity(intent);
                    });
                    rvSaved.setAdapter(savedAdapter);
                    savedAdapter.setBooks(savedBooks);
                });
            }).start();
        }
    }

    private void setupNavigation() {
        BottomNavigationView nav = findViewById(R.id.bottomNavigation);

        nav.setSelectedItemId(R.id.nav_profile);

        nav.setOnItemSelectedListener(item -> {

            int id = item.getItemId();

            if (id == R.id.nav_profile) {
                return true;
            }

            if (id == R.id.nav_home) {

                Intent intent = new Intent(this, DashboardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                startActivity(intent);
                finish();

                return true;
            }

            if (id == R.id.nav_search) {

                Intent intent = new Intent(this, SearchActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                startActivity(intent);
                finish();

                return true;
            }

            if (id == R.id.nav_loans) {

                Intent intent = new Intent(this, LoansActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                startActivity(intent);
                finish();

                return true;
            }

            return false;
        });
    }
}
