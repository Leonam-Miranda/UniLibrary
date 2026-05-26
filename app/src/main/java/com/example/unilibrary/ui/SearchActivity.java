package com.example.unilibrary.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unilibrary.R;
import com.example.unilibrary.service.BookService;
import com.example.unilibrary.ui.adapter.SearchBookAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

public class SearchActivity extends AppCompatActivity {
    private BookService bookService;
    private SearchBookAdapter adapter;
    private String currentQuery = "";
    private String currentGenre = "Todas as Categorias";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        bookService = new BookService(this);
        setupRecyclerView();
        setupSearch();
        setupFilters();
        setupNavigation();

        performSearch();
    }

    private void setupRecyclerView() {
        RecyclerView rv = findViewById(R.id.rvSearchResults);
        adapter = new SearchBookAdapter(book -> {
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra("book_id", book.getId());
            startActivity(intent);
        });
        rv.setAdapter(adapter);
    }

    private void setupSearch() {
        EditText etSearch = findViewById(R.id.etSearch);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                currentQuery = s.toString();
                performSearch();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void setupFilters() {
        ChipGroup chipGroup = findViewById(R.id.chipGroupGenre);
        chipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (!checkedIds.isEmpty()) {
                Chip chip = findViewById(checkedIds.get(0));
                currentGenre = chip.getText().toString();
                performSearch();
            }
        });
    }

    private void performSearch() {
        bookService.searchBooks(currentQuery, currentGenre).observe(this, books -> {
            if (books != null) {
                adapter.setBooks(books);
            }
        });
    }

    private void setupNavigation() {
        BottomNavigationView nav = findViewById(R.id.bottomNavigation);
        nav.setSelectedItemId(R.id.nav_search);
        nav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                startActivity(new Intent(this, DashboardActivity.class));
                return true;
            } else if (id == R.id.nav_loans) {
                startActivity(new Intent(this, LoansActivity.class));
                return true;
            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(this, ProfileActivity.class));
                return true;
            }
            return id == R.id.nav_search;
        });
    }
}
