package com.example.unilibrary.ui;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.unilibrary.R;
import com.example.unilibrary.enums.BookStatus;
import com.example.unilibrary.model.Book;
import com.example.unilibrary.service.BookService;
import com.google.android.material.button.MaterialButton;

public class DetailActivity extends AppCompatActivity {
    private BookService bookService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        bookService = new BookService(this);

        ImageView btnBack = findViewById(R.id.btnBack);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> onBackPressed());
        }

        int bookId = getIntent().getIntExtra("book_id", -1);
        if (bookId != -1) {
            loadBookDetails(bookId);
        } else {
            Toast.makeText(this, "Erro ao carregar livro", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void loadBookDetails(int bookId) {
        bookService.getBookById(bookId, book -> {
            if (book != null) {
                populateUI(book);
            } else {
                Toast.makeText(this, "Livro não encontrado", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void populateUI(Book book) {
        TextView tvTitle = findViewById(R.id.tvBookTitle);
        TextView tvAuthor = findViewById(R.id.tvBookAuthor);
        TextView tvStatus = findViewById(R.id.tvBookStatus);
        TextView tvSynopsis = findViewById(R.id.tvBookSynopsis);
        ImageView ivStatusIcon = findViewById(R.id.ivStatusIcon);
        MaterialButton btnReserve = findViewById(R.id.btnReserve);

        tvTitle.setText(book.getTitle());
        tvAuthor.setText("por " + book.getAuthor());
        tvSynopsis.setText(book.getDescription());

        if (book.getStatus() == BookStatus.AVAILABLE) {
            tvStatus.setText("Disponível");
            tvStatus.setTextColor(getResources().getColor(R.color.black));
            ivStatusIcon.setColorFilter(getResources().getColor(R.color.badge_available));
            btnReserve.setEnabled(true);
            btnReserve.setText("Reservar Empréstimo");
        } else {
            tvStatus.setText("Indisponível");
            tvStatus.setTextColor(getResources().getColor(R.color.text_secondary));
            ivStatusIcon.setColorFilter(getResources().getColor(R.color.badge_unavailable));
            btnReserve.setEnabled(false);
            btnReserve.setText("Livro já emprestado");
        }
    }
}
