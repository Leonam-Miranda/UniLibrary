package com.example.unilibrary.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.unilibrary.R;
import com.example.unilibrary.db.AppDatabase;
import com.example.unilibrary.db.dao.UserDao;
import com.example.unilibrary.model.User;
import com.example.unilibrary.service.Session;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileActivity extends AppCompatActivity {
    private UserDao userDao;

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
        int userId = Session.getUserId(this);
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
        ImageView ivAvatar = findViewById(R.id.ivProfileAvatar);

        if (tvName != null) tvName.setText(user.getName());
        if (tvEmail != null) tvEmail.setText(user.getEmail());
        if (tvBooksRead != null) tvBooksRead.setText(String.valueOf(user.readBooks));
        if (tvSavedBooks != null) tvSavedBooks.setText(String.valueOf(user.getSavedBooksCount()));
        if (ivAvatar != null && user.getAvatarResId() != 0)  // ← novo
            ivAvatar.setImageResource(user.getAvatarResId());// ← novo
    }

    private void setupNavigation() {
        BottomNavigationView nav = findViewById(R.id.bottomNavigation);
        nav.setSelectedItemId(R.id.nav_profile);
        nav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                startActivity(new Intent(this, DashboardActivity.class));
                return true;
            } else if (id == R.id.nav_search) {
                startActivity(new Intent(this, SearchActivity.class));
                return true;
            } else if (id == R.id.nav_loans) {
                startActivity(new Intent(this, LoansActivity.class));
                return true;
            }
            return id == R.id.nav_profile;
        });
    }
}
