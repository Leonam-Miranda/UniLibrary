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
    private User currentUser;

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

        // Carregar livro e usuário em background
        new Thread(() -> {
            book = bookDao.searchById(bookId);
            int userId = Session.getUserId(this);
            if (userId != -1) {
                currentUser = userDao.searchById(userId);
            }
            
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
        MaterialButton btnSaveBook = findViewById(R.id.btnSaveBook);

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
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(book.getEpubUrl()));
                    startActivity(browserIntent);
                    incrementReadBooksCount();
                } else {
                    Toast.makeText(this, "Link de leitura não disponível para este livro", Toast.LENGTH_SHORT).show();
                }
            });
        }

        // Configuração do botão Salvar Livro
        if (btnSaveBook != null && currentUser != null) {
            updateSaveButtonUI(btnSaveBook);
            btnSaveBook.setOnClickListener(v -> {
                new Thread(() -> {
                    currentUser.toggleSaveBook(book.getId());
                    userDao.update(currentUser);
                    runOnUiThread(() -> {
                        updateSaveButtonUI(btnSaveBook);
                        String msg = currentUser.isBookSaved(book.getId()) ? "Adicionado aos salvos" : "Removido dos salvos";
                        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                    });
                }).start();
            });
        }
    }

    private void updateSaveButtonUI(MaterialButton btn) {
        if (currentUser.isBookSaved(book.getId())) {
            btn.setText("Retirar dos salvos");
            btn.setIconResource(android.R.drawable.btn_star_big_on);
        } else {
            btn.setText("Adicionar aos salvos");
            btn.setIconResource(android.R.drawable.btn_star_big_off);
        }
    }

    private void incrementReadBooksCount() {
        if (currentUser != null) {
            new Thread(() -> {
                currentUser.readBooks++;
                userDao.update(currentUser);
            }).start();
        }
    }

    private void showLoanBottomSheet() {
        View sheetView = getLayoutInflater()
                .inflate(R.layout.bottom_sheet_loan, null);

        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(sheetView);

        ((TextView) sheetView.findViewById(R.id.tvSheetBookTitle))
                .setText(book.getTitle());
        ((TextView) sheetView.findViewById(R.id.tvSheetBookAuthor))
                .setText(book.getAuthor());

        ImageView ivSheetCover = sheetView.findViewById(R.id.imgBookCoverSheet);
        if (ivSheetCover != null && book.getCoverResId() != 0) {
            ivSheetCover.setImageResource(book.getCoverResId());
        }

        long dueTime = System.currentTimeMillis() + (5L * 24 * 60 * 60 * 1000);
        String dueDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                .format(new Date(dueTime));
        ((TextView) sheetView.findViewById(R.id.tvDueDate)).setText(dueDate);

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

        sheetView.findViewById(R.id.btnCancelLoan)
                .setOnClickListener(btn -> dialog.dismiss());

        dialog.show();
    }
}
