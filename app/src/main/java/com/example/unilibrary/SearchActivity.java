package com.example.unilibrary;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;

public class SearchActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Configuração da navegação inferior
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

        // Configuração dos cliques nos livros para abrir os detalhes
        MaterialCardView book1 = findViewById(R.id.bookCard1);
        MaterialCardView book2 = findViewById(R.id.bookCard2);

        View.OnClickListener openDetail = v -> {
            Intent intent = new Intent(SearchActivity.this, DetailActivity.class);
            startActivity(intent);
        };

        if (book1 != null) book1.setOnClickListener(openDetail);
        if (book2 != null) book2.setOnClickListener(openDetail);
    }
}