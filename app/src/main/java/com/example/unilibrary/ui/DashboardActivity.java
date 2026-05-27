package com.example.unilibrary.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unilibrary.R;
import com.example.unilibrary.service.BookService;
import com.example.unilibrary.service.LoanService;
import com.example.unilibrary.service.Session;
import com.example.unilibrary.ui.adapter.BookAdapter;
import com.example.unilibrary.ui.adapter.LoanAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DashboardActivity extends AppCompatActivity {
    private BookService bookService;
    private LoanService loanService;
    private BookAdapter bookAdapter;
    private LoanAdapter loanAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        bookService = new BookService(this);
        loanService = new LoanService(this);
        
        setupRecyclerViews();
        observeData();
        setupNavigation();
    }

    private void setupRecyclerViews() {
        // Suggested Books
        RecyclerView rvSuggested = findViewById(R.id.rvSuggestedBooks);
        bookAdapter = new BookAdapter(book -> {
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra("book_id", book.getId());
            startActivity(intent);
        });
        rvSuggested.setAdapter(bookAdapter);

        // Loans RecyclerView
        RecyclerView rvLoans = findViewById(R.id.rvDashboardLoans);
        loanAdapter = new LoanAdapter(loan -> {
            Intent intent = new Intent(this, RenewLoanActivity.class);
            intent.putExtra("loan_id", loan.loan.getId());
            startActivity(intent);
        });
        rvLoans.setAdapter(loanAdapter);
    }

    private void observeData() {
        // Observe Books
        bookService.getAllBooks().observe(this, books -> {
            if (books != null) {
                bookAdapter.setBooks(books);
            }
        });

        // Observe Loans
        int userId = Session.getUserId(this);
        if (userId != -1) {
            loanService.getActiveLoans(userId).observe(this, loans -> {
                TextView tvNoLoans = findViewById(R.id.tvNoLoans);
                RecyclerView rvLoans = findViewById(R.id.rvDashboardLoans);
                
                if (loans != null && !loans.isEmpty()) {
                    loanAdapter.setLoans(loans);
                    rvLoans.setVisibility(View.VISIBLE);
                    if (tvNoLoans != null) tvNoLoans.setVisibility(View.GONE);
                } else {
                    rvLoans.setVisibility(View.GONE);
                    if (tvNoLoans != null) tvNoLoans.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    private void setupNavigation() {
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
}
