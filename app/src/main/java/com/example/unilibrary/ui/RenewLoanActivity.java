package com.example.unilibrary.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.unilibrary.R;
import com.example.unilibrary.db.AppDatabase;
import com.example.unilibrary.model.Book;
import com.example.unilibrary.model.Loan;
import com.example.unilibrary.service.LoanService;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RenewLoanActivity extends AppCompatActivity {
    private LoanService loanService;
    private int loanId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_renew_loan);

        loanService = new LoanService(this);
        loanId = getIntent().getIntExtra("loan_id", -1);

        if (loanId == -1) {
            Toast.makeText(this, "Erro ao carregar empréstimo", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        setupUI();
        loadLoanData();
        setupNavigation();
    }

    private void setupUI() {
        findViewById(R.id.btnBack).setOnClickListener(v -> onBackPressed());
        findViewById(R.id.btnCancel).setOnClickListener(v -> finish());

        findViewById(R.id.btnConfirmRenewal).setOnClickListener(v -> {
            loanService.renovar(loanId,
                    erro -> Toast.makeText(this, erro, Toast.LENGTH_SHORT).show(),
                    loan -> {
                        Toast.makeText(this, "Renovação confirmada!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
            );
        });
    }

    private void loadLoanData() {
        new Thread(() -> {
            AppDatabase db = AppDatabase.getInstance(this);
            Loan loan = db.loanDao().searchById(loanId);
            if (loan != null) {
                Book book = db.bookDao().searchById(loan.getBookId());
                runOnUiThread(() -> {
                    if (book != null) {
                        populateUI(loan, book);
                    }
                });
            }
        }).start();
    }

    private void populateUI(Loan loan, Book book) {
        TextView tvTitle = findViewById(R.id.bookTitle);
        TextView tvAuthor = findViewById(R.id.bookAuthor);
        ImageView ivCover = findViewById(R.id.ivBookCover);

        tvTitle.setText(book.getTitle());
        tvAuthor.setText(book.getAuthor());
        
        if (book.getCoverResId() != 0) {
            ivCover.setImageResource(book.getCoverResId());
        } else {
            ivCover.setImageResource(R.color.background_light_grey);
        }

        // Datas
        SimpleDateFormat sdf = new SimpleDateFormat("dd 'de' MMMM", new Locale("pt", "BR"));
        ((TextView) findViewById(R.id.tvCurrentDueDate)).setText(sdf.format(loan.getDueDate()));
        
        Date newDueDate = new Date(loan.getDueDate().getTime() + (14L * 24 * 60 * 60 * 1000));
        ((TextView) findViewById(R.id.tvNewDueDate)).setText(sdf.format(newDueDate));
    }

    private void setupNavigation() {
        BottomNavigationView nav = findViewById(R.id.bottomNavigation);
        nav.setSelectedItemId(R.id.nav_loans);
        nav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                startActivity(new Intent(this, DashboardActivity.class));
                finish();
                return true;
            } else if (id == R.id.nav_search) {
                startActivity(new Intent(this, SearchActivity.class));
                finish();
                return true;
            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(this, ProfileActivity.class));
                finish();
                return true;
            }
            return id == R.id.nav_loans;
        });
    }
}
