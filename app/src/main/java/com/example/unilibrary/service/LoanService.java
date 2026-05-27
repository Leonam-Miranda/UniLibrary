package com.example.unilibrary.service;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.core.util.Consumer;
import androidx.lifecycle.LiveData;

import com.example.unilibrary.db.AppDatabase;
import com.example.unilibrary.db.dao.BookDao;
import com.example.unilibrary.db.dao.LoanDao;
import com.example.unilibrary.db.dao.UserDao;
import com.example.unilibrary.enums.BookStatus;
import com.example.unilibrary.model.Book;
import com.example.unilibrary.model.Loan;
import com.example.unilibrary.model.LoanWithBook;
import com.example.unilibrary.model.User;

import java.util.Date;
import java.util.List;

// service/LoanService.java
public class LoanService {

    private static final double MULTA_INICIAL = 2.00;
    private static final double MULTA_POR_DIA = 0.10;
    private static final int LIMITE_ALUGUEIS = 3;

    private final LoanDao loanDao;
    private final BookDao bookDao;
    private final UserDao userDao;
    private final Handler mainThread =
            new Handler(Looper.getMainLooper());

    public LoanService(Context ctx) {
        AppDatabase db = AppDatabase.getInstance(ctx);
        loanDao = db.loanDao();
        bookDao = db.bookDao();
        userDao = db.userDao();
    }

    public LiveData<List<LoanWithBook>> getActiveLoans(int userId) {
        return loanDao.findAssetsWithBook(userId);
    }

    // ALUGAR
    public void alugar(int userId, int bookId,
                       Consumer<String> onErro,
                       Consumer<Loan> onSucesso) {
        new Thread(() -> {
            // Checar limite de 3 aluguéis
            if (loanDao.accountActives(userId) >= LIMITE_ALUGUEIS) {
                mainThread.post(() ->
                        onErro.accept("Limite de " + LIMITE_ALUGUEIS +
                                " aluguéis atingido"));
                return;
            }

            // Checar disponibilidade
            Book book = bookDao.searchById(bookId);
            if (book.getStatus() != BookStatus.AVAILABLE) {
                mainThread.post(() ->
                        onErro.accept("Livro indisponível"));
                return;
            }

            // Criar aluguel
            Loan loan = new Loan(userId, bookId);
            long id = loanDao.add(loan);
            loan.setId((int) id);

            // Marcar livro como emprestado
            book.setStatus(BookStatus.BORROWED);
            bookDao.update(book);

            mainThread.post(() -> onSucesso.accept(loan));
        }).start();
    }

    // DEVOLVER
    public void devolver(int loanId,
                         Consumer<String> onErro,
                         Consumer<Loan> onSucesso) {
        new Thread(() -> {
            Loan loan = loanDao.searchById(loanId);
            if (loan == null || loan.isReturned()) {
                mainThread.post(() ->
                        onErro.accept("Aluguel não encontrado"));
                return;
            }

            Date agora = new Date();
            loan.setReturnDate(agora);
            loan.setReturned(true);

            // Calcular multa
            double multa = calcularMulta(loan, agora);
            loan.setFine(multa);
            loanDao.update(loan);

            // Livro volta a ficar disponível
            Book book = bookDao.searchById(loan.getBookId());
            book.setStatus(BookStatus.AVAILABLE);
            bookDao.update(book);

            // Incrementar livros lidos do usuário
            User user = userDao.searchById(loan.getUserId());
            user.markBookAsRead(loan.getBookId());
            userDao.update(user);

            mainThread.post(() -> onSucesso.accept(loan));
        }).start();
    }

    // CALCULAR MULTA
    public static double calcularMulta(Loan loan, Date agora) {
        if (agora.getTime() <= loan.getDueDate().getTime()) return 0.0;

        long diff = agora.getTime() - loan.getDueDate().getTime();
        long diasAtraso = diff / (24L * 60 * 60 * 1000);
        return MULTA_INICIAL + (diasAtraso * MULTA_POR_DIA);
    }
    public void renovar(int loanId,
                        Consumer<String> onErro,
                        Consumer<Loan> onSucesso) {
        new Thread(() -> {
            Loan loan = loanDao.searchById(loanId);

            if (loan == null || loan.isReturned()) {
                mainThread.post(() -> onErro.accept("Empréstimo não encontrado"));
                return;
            }

            if (loan.isRenewed()) {
                mainThread.post(() -> onErro.accept("Este empréstimo já foi renovado uma vez"));
                return;
            }

            // Adiciona 14 dias à data de vencimento atual
            Date novaDueDate = new Date(loan.getDueDate().getTime() + (14L * 24 * 60 * 60 * 1000));
            loan.setDueDate(novaDueDate);
            loan.setRenewed(true);
            loanDao.update(loan);

            mainThread.post(() -> onSucesso.accept(loan));
        }).start();
    }
}
