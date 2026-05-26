package com.example.unilibrary.ui;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unilibrary.R;
import com.example.unilibrary.service.BookService;
import com.example.unilibrary.ui.adapter.BookAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DashboardActivity extends AppCompatActivity {
    private BookService bookService;
    private BookAdapter bookAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        bookService = new BookService(this);
        setupRecyclerView();
        observeBooks();

        BottomNavigationView nav = findViewById(R.id.bottomNavigation);
        nav.setSelectedItemId(R.id.nav_home);
        nav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_search) {
                startActivity(new Intent(this, SearchActivity.class));
                return true;
            } else if (id == R.id.nav_loans) {
                startActivity(new Intent(this, LoansActivity.class));
                return true;
            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(this, ProfileActivity.class));
                return true;
            }
            return id == R.id.nav_home;
        });
    }

    private void setupRecyclerView() {
        RecyclerView rv = findViewById(R.id.rvSuggestedBooks);
        bookAdapter = new BookAdapter(book -> {
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra("book_id", book.getId());
            startActivity(intent);
        });
        rv.setAdapter(bookAdapter);
    }

    private void observeBooks() {
        bookService.getAllBooks().observe(this, books -> {
            if (books != null) {
                bookAdapter.setBooks(books);
            }
        });
    }
}
