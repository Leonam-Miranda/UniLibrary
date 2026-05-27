package com.example.unilibrary.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unilibrary.R;
import com.example.unilibrary.service.LoanService;
import com.example.unilibrary.service.Session;
import com.example.unilibrary.ui.adapter.LoanAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class LoansActivity extends AppCompatActivity {
    private LoanService loanService;
    private LoanAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loans);

        loanService = new LoanService(this);
        setupRecyclerView();
        observeLoans();
        setupNavigation();
    }

    private void setupRecyclerView() {
        RecyclerView rv = findViewById(R.id.rvLoans);
        adapter = new LoanAdapter(loan -> {
            // Lógica de renovação (pode ser implementada depois)
            Toast.makeText(this, "Renovação solicitada para: " + loan.book.getTitle(), Toast.LENGTH_SHORT).show();
        });
        rv.setAdapter(adapter);
    }

    private void observeLoans() {
        int userId = Session.getUserId(this);
        if (userId != -1) {
            loanService.getActiveLoans(userId).observe(this, loans -> {
                if (loans != null) {
                    adapter.setLoans(loans);
                }
            });
        }
    }

    private void setupNavigation() {
        BottomNavigationView nav = findViewById(R.id.bottomNavigation);
        nav.setSelectedItemId(R.id.nav_loans);
        nav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                startActivity(new Intent(this, DashboardActivity.class));
                return true;
            } else if (id == R.id.nav_search) {
                startActivity(new Intent(this, SearchActivity.class));
                return true;
            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(this, ProfileActivity.class));
                return true;
            }
            return id == R.id.nav_loans;
        });
    }
}
