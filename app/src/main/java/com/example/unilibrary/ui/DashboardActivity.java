package com.example.unilibrary.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unilibrary.R;
import com.example.unilibrary.model.LoanWithBook;
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
        loanAdapter = new LoanAdapter(new LoanAdapter.OnLoanClickListener() {
            @Override
            public void onRenewClick(LoanWithBook loan) {
                Intent intent = new Intent(DashboardActivity.this, RenewLoanActivity.class);
                intent.putExtra("loan_id", loan.loan.getId());
                startActivity(intent);
            }

            @Override
            public void onReturnClick(LoanWithBook loan) {
                loanService.devolver(loan.loan.getId(),
                        erro -> Toast.makeText(DashboardActivity.this, erro, Toast.LENGTH_SHORT).show(),
                        l -> Toast.makeText(DashboardActivity.this, "Livro devolvido com sucesso!", Toast.LENGTH_SHORT).show()
                );
            }
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

            if (id == R.id.nav_home) {
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

            if (id == R.id.nav_profile) {

                Intent intent = new Intent(this, ProfileActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                startActivity(intent);
                finish();

                return true;
            }

            return false;
        });
    }
}
