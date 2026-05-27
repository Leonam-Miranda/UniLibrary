package com.example.unilibrary.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.unilibrary.R;
import com.example.unilibrary.enums.BookStatus;
import com.example.unilibrary.model.Book;
import com.example.unilibrary.model.User;
import com.example.unilibrary.db.AppDatabase;
import com.example.unilibrary.db.dao.BookDao;
import com.example.unilibrary.db.dao.UserDao;
import com.example.unilibrary.service.LoanService;
import com.example.unilibrary.service.Session;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DetailActivity extends AppCompatActivity {

    private BookDao bookDao;
    private UserDao userDao;
    private LoanService loanService;
    private Book book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        bookDao = AppDatabase.getInstance(this).bookDao();
        userDao = AppDatabase.getInstance(this).userDao();
        loanService = new LoanService(this);

        ImageView btnBack = findViewById(R.id.btnBack);
        if (btnBack != null) btnBack.setOnClickListener(v -> onBackPressed());

        int bookId = getIntent().getIntExtra("book_id", -1);
        if (bookId == -1) {
            Toast.makeText(this, "Erro ao carregar livro", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Carregar livro em background
        new Thread(() -> {
            book = bookDao.searchById(bookId);
            runOnUiThread(() -> {
                if (book == null) {
                    Toast.makeText(this, "Livro não encontrado", Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }
                populateUI(book);
            });
        }).start();
    }

    private void populateUI(Book book) {
        TextView tvTitle    = findViewById(R.id.bookTitle);
        TextView tvAuthor   = findViewById(R.id.bookAuthor);
        TextView tvStatus   = findViewById(R.id.tvStatus);
        TextView tvSynopsis = findViewById(R.id.tvDescription);
        ImageView ivStatusIcon = findViewById(R.id.ivStatusIcon);
        ImageView ivBookCover = findViewById(R.id.ivBookCover);
        MaterialButton btnReserve = findViewById(R.id.btnReserve);
        MaterialButton btnReadOnline = findViewById(R.id.btnReadOnline);

        tvTitle.setText(book.getTitle());
        tvAuthor.setText("por " + book.getAuthor());
        tvSynopsis.setText(book.getDescription());
        
        if (book.getCoverResId() != 0) {
            ivBookCover.setImageResource(book.getCoverResId());
        }

        if (book.getStatus() == BookStatus.AVAILABLE) {
            tvStatus.setText("Disponível");
            tvStatus.setTextColor(getColor(R.color.black));
            ivStatusIcon.setColorFilter(getColor(R.color.badge_available));
            btnReserve.setEnabled(true);
            btnReserve.setText("Reservar Empréstimo");
            btnReserve.setOnClickListener(v -> showLoanBottomSheet());
        } else {
            tvStatus.setText("Indisponível");
            tvStatus.setTextColor(getColor(R.color.text_secondary));
            ivStatusIcon.setColorFilter(getColor(R.color.badge_unavailable));
            btnReserve.setEnabled(false);
            btnReserve.setAlpha(0.5f);
            btnReserve.setText("Livro já emprestado");
        }

        // Configuração do botão Ler Online
        if (btnReadOnline != null) {
            btnReadOnline.setOnClickListener(v -> {
                if (book.getEpubUrl() != null && !book.getEpubUrl().isEmpty()) {
                    // 1. Redirecionar para o link
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(book.getEpubUrl()));
                    startActivity(browserIntent);

                    // 2. Incrementar contador de livros lidos no perfil
                    incrementReadBooksCount();
                } else {
                    Toast.makeText(this, "Link de leitura não disponível para este livro", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void incrementReadBooksCount() {
        int userId = Session.getUserId(this);
        if (userId != -1) {
            new Thread(() -> {
                User user = userDao.searchById(userId);
                if (user != null) {
                    user.readBooks++;
                    userDao.update(user);
                }
            }).start();
        }
    }

    private void showLoanBottomSheet() {
        View sheetView = getLayoutInflater()
                .inflate(R.layout.bottom_sheet_loan, null);

        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(sheetView);

        // Preencher dados do livro
        ((TextView) sheetView.findViewById(R.id.tvSheetBookTitle))
                .setText(book.getTitle());
        ((TextView) sheetView.findViewById(R.id.tvSheetBookAuthor))
                .setText(book.getAuthor());

        ImageView ivSheetCover = sheetView.findViewById(R.id.imgBookCoverSheet);
        if (ivSheetCover != null && book.getCoverResId() != 0) {
            ivSheetCover.setImageResource(book.getCoverResId());
        }

        // Data de devolução (hoje + 5 dias)
        long dueTime = System.currentTimeMillis() + (5L * 24 * 60 * 60 * 1000);
        String dueDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                .format(new Date(dueTime));
        ((TextView) sheetView.findViewById(R.id.tvDueDate)).setText(dueDate);

        // Confirmar empréstimo
        sheetView.findViewById(R.id.btnConfirmLoan).setOnClickListener(btn -> {
            dialog.dismiss();
            int userId = Session.getUserId(this);
            loanService.alugar(userId, book.getId(),
                    erro -> Toast.makeText(this, erro, Toast.LENGTH_SHORT).show(),
                    loan -> {
                        Toast.makeText(this,
                                "Empréstimo realizado! Devolva até " + dueDate,
                                Toast.LENGTH_LONG).show();
                        finish();
                    }
            );
        });

        // Cancelar
        sheetView.findViewById(R.id.btnCancelLoan)
                .setOnClickListener(btn -> dialog.dismiss());

        dialog.show();
    }
}
