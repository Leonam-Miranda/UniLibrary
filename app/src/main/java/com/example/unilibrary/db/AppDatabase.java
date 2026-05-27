package com.example.unilibrary.db;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.Database;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.unilibrary.R;
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
        version = 12
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
                            )
                            .fallbackToDestructiveMigration()
                            .addCallback(new RoomDatabase.Callback() {
                                @Override
                                public void onOpen(@NonNull SupportSQLiteDatabase db) {
                                    super.onOpen(db);
                                    // Ao abrir o banco (incluindo após migrations), 
                                    // checamos se precisamos inserir os livros iniciais
                                    seedDatabase(instance);
                                }
                            })
                            .build();
                }
            }
        }
        return instance;
    }

    private static void seedDatabase(AppDatabase db) {
        Executors.newSingleThreadExecutor().execute(() -> {
            BookDao dao = db.bookDao();
            // Só insere se o banco estiver vazio
            if (dao.searchById(1) == null) { 
                dao.insert(new Book(null, "Dom Casmurro", "Machado de Assis",
                        "Um homem relembra sua juventude e desconfia que foi traído pela esposa. Gênero: Ficção, Clássico.",
                        BookStatus.AVAILABLE,
                        "https://www.gutenberg.org/ebooks/55752", R.drawable.dom_casmurro));
                
                dao.insert(new Book(null, "O Cortiço", "Aluísio Azevedo",
                        "A vida dos moradores de um cortiço cheio de conflitos e pobreza. Gênero: Realismo, Ficção.",
                        BookStatus.AVAILABLE,
                        "https://www.gutenberg.org/ebooks/6913", R.drawable.o_cortico));
                
                dao.insert(new Book(null, "Frankenstein", "Mary Shelley",
                        "Um cientista cria uma criatura que acaba se tornando um perigo. Gênero: Ficção Científica, Horror.",
                        BookStatus.AVAILABLE,
                        "https://www.gutenberg.org/ebooks/84", R.drawable.frankenstein));
                
                dao.insert(new Book(null, "Dracula", "Bram Stocker",
                        "Um vampiro tenta espalhar o terror por várias pessoas na Idade Média. Gênero: Horror, Romance Gótico.",
                        BookStatus.AVAILABLE,
                        "https://www.gutenberg.org/ebooks/345", R.drawable.dracula));
                
                dao.insert(new Book(null, "Alice no País das Maravilhas", "Lewis Caroll",
                        "Uma garota cai em um mundo estranho cheio de criaturas malucas. Gênero: Fantasia, Ficção.",
                        BookStatus.AVAILABLE,
                        "https://www.gutenberg.org/ebooks/11", R.drawable.alice));
                
                dao.insert(new Book(null, "Sherlock Holmes", "Arthur Conan Doyle",
                        "Um detetive precisa resolver um mistério usando inteligência e observação. Gênero: Mistério, Ficção.",
                        BookStatus.AVAILABLE,
                        "https://www.gutenberg.org/ebooks/1661", R.drawable.sherlock));
                
                dao.insert(new Book(null, "Moby Dick", "Herman Melville",
                        "Um capitão obcecado caça uma enorme baleia branca. Gênero: Aventura, Ficção.",
                        BookStatus.AVAILABLE,
                        "https://www.gutenberg.org/ebooks/2701", R.drawable.moby_dick));
            }
        });
    }
}
