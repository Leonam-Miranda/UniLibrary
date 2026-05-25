package com.example.unilibrary.db;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.Database;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.unilibrary.db.Converters;
import com.example.unilibrary.db.dao.BookDao;
import com.example.unilibrary.db.dao.LoanDao;
import com.example.unilibrary.db.dao.UserDao;
import com.example.unilibrary.enums.BookStatus;
import com.example.unilibrary.model.Book;
import com.example.unilibrary.model.Loan;
import com.example.unilibrary.model.User;

import java.util.concurrent.Executors;

@Database(
        entities = {User.class, Book.class, Loan.class},
        version = 2
)
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
                            ).fallbackToDestructiveMigration()
                            .addCallback(new RoomDatabase.Callback() {     // ← começa aqui
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    super.onCreate(db);
                                    Executors.newSingleThreadExecutor().execute(() -> {
                                        BookDao dao = instance.bookDao();

                                        dao.insert(new Book(null, "Dom Casmurro", "Machado de Assis",
                                                "Um homem relembra sua juventude e desconfia que foi traído pela esposa.",
                                                BookStatus.AVAILABLE,
                                                "https://www.gutenberg.org/ebooks/55752?utm_source=chatgpt.com"));
                                        dao.insert(new Book(null, "O Cortiço", "Aluísio Azevedo",
                                                "A vida dos moradores de um cortiço cheio de conflitos e pobreza.",
                                                BookStatus.AVAILABLE,
                                                "https://www.gutenberg.org/ebooks/6913?utm_source=chatgpt.com"));
                                        dao.insert(new Book(null, "Frankenstein", "Mary Shelley",
                                                "Um cientista cria uma criatura que acaba se tornando um perigo.",
                                                BookStatus.AVAILABLE,
                                                "https://www.gutenberg.org/ebooks/84?utm_source=chatgpt.com"));
                                        dao.insert(new Book(null, "Dracula", "Bram Stocker",
                                                "Um vampiro tenta espalhar o terror por várias pessoas na Idade Média.",
                                                BookStatus.AVAILABLE,
                                                "https://www.gutenberg.org/ebooks/345?utm_source=chatgpt.com"));
                                        dao.insert(new Book(null, "Alice no País das Maravilhas", "Lewis Caroll",
                                                "Uma garota cai em um mundo estranho cheio de criaturas malucas.",
                                                BookStatus.AVAILABLE,
                                                "https://www.gutenberg.org/ebooks/11?utm_source=chatgpt.com"));
                                        dao.insert(new Book(null, "Sherlock Holmes", "Arthur Conan Doyle",
                                                "Um detetive precisa resolver um mistério usando inteligência e observação.",
                                                BookStatus.AVAILABLE,
                                                "https://www.gutenberg.org/ebooks/1661?utm_source=chatgpt.com"));
                                        dao.insert(new Book(null, "Moby Dick", "Herman Melville",
                                                "Um capitão obcecado caça uma enorme baleia branca.",
                                                BookStatus.AVAILABLE,
                                                "https://www.gutenberg.org/ebooks/2701?utm_source=chatgpt.com"));
                                    });
                                }
                            })
                            .build();
                }
            }
        }
        return instance;
    }

}
