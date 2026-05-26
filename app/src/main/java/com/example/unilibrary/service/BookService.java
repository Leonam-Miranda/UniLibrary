package com.example.unilibrary.service;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;

import com.example.unilibrary.db.AppDatabase;
import com.example.unilibrary.db.dao.BookDao;
import com.example.unilibrary.model.Book;

import java.util.List;

public class BookService {
    private final BookDao dao;
    private final Handler mainThread = new Handler(Looper.getMainLooper());

    public BookService(Context ctx) {
        dao = AppDatabase.getInstance(ctx).bookDao();
    }

    public LiveData<List<Book>> getAllBooks() {
        return dao.getAll();
    }

    public void getBookById(int id, androidx.core.util.Consumer<Book> onResult) {
        new Thread(() -> {
            Book book = dao.searchById(id);
            mainThread.post(() -> onResult.accept(book));
        }).start();
    }

    public LiveData<List<Book>> searchBooks(String query, String genre) {
        if (genre == null || genre.isEmpty() || genre.equals("Todas as Categorias")) {
            return dao.searchAlphabetical(query);
        } else {
            return dao.searchByGenre(query, genre);
        }
    }
}
