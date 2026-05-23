package com.example.unilibrary.db;
import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.Database;
import androidx.room.TypeConverters;

import com.example.unilibrary.db.Converters;
import com.example.unilibrary.db.dao.BookDao;
import com.example.unilibrary.db.dao.LoanDao;
import com.example.unilibrary.db.dao.UserDao;
import com.example.unilibrary.model.Book;
import com.example.unilibrary.model.Loan;
import com.example.unilibrary.model.User;

@Database(entities = {User.class, Book.class, Loan.class}, version = 1, exportSchema = false)
@TypeConverters(Converters.class)
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase instance;
    public abstract UserDao userDao();
    public abstract BookDao bookDao();
    public abstract LoanDao loanDao();
    public static AppDatabase getInstance(Context ctx) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                            ctx.getApplicationContext(),
                            AppDatabase.class,
                            "biblioteca.db"
                    ).build();
                }
            }
        }
        return instance;
    }
}
