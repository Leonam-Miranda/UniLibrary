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
        version = 14
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
                        "Bentinho relembra sua juventude, seu romance com Capitu e os acontecimentos que marcaram sua vida." +
                                " Ao longo da história, ele passa a desconfiar que foi traído," +
                                " criando uma narrativa cheia de dúvidas, ciúmes e interpretações. Gênero: Ficção, Clássico.",
                        BookStatus.AVAILABLE,
                        "https://www.gutenberg.org/cache/epub/55752/pg55752-images.html", R.drawable.dom_casmurro));
                
                dao.insert(new Book(null, "O Cortiço", "Aluísio Azevedo",
                        "A obra mostra o cotidiano dos moradores de um cortiço no Rio de Janeiro," +
                                " retratando pobreza, ambição, conflitos e desigualdade social." +
                                " O livro acompanha diferentes personagens e como o ambiente influencia suas vidas. Gênero: Realismo, Ficção.",
                        BookStatus.AVAILABLE,
                        "https://www.gutenberg.org/cache/epub/6913/pg6913-images.html", R.drawable.o_cortico));
                
                dao.insert(new Book(null, "Frankenstein", "Mary Shelley",
                        "Victor Frankenstein é um cientista que decide criar vida através de um experimento." +
                                " Porém, ao perceber o resultado de sua criação, ele a abandona, dando início a uma história marcada por solidão," +
                                " vingança e consequências perigosas. Gênero: Ficção Científica, Horror.",
                        BookStatus.AVAILABLE,
                        "https://www.gutenberg.org/cache/epub/84/pg84-images.html", R.drawable.frankenstein));
                
                dao.insert(new Book(null, "Dracula", "Bram Stocker",
                        "O conde Drácula deixa seu castelo na Transilvânia para espalhar o terror em outros lugares." +
                                " Enquanto várias pessoas tentam sobreviver aos ataques do vampiro," +
                                " elas também procuram descobrir uma maneira de derrotá-lo. Gênero: Horror, Romance Gótico.",
                        BookStatus.AVAILABLE,
                        "https://www.gutenberg.org/cache/epub/345/pg345-images.html", R.drawable.dracula));
                
                dao.insert(new Book(null, "Alice no País das Maravilhas", "Lewis Caroll",
                        "Alice cai em um mundo fantástico cheio de criaturas estranhas," +
                                " lugares curiosos e situações absurdas. Durante sua jornada, " +
                                "ela encontra personagens marcantes enquanto " +
                                "tenta entender aquele universo completamente diferente da realidade. Gênero: Fantasia, Ficção.",
                        BookStatus.AVAILABLE,
                        "https://www.gutenberg.org/cache/epub/11/pg11-images.html", R.drawable.alice));
                
                dao.insert(new Book(null, "Sherlock Holmes", "Arthur Conan Doyle",
                        "A coletânea acompanha Sherlock Holmes e Dr. Watson na resolução de diversos casos misteriosos." +
                                " Utilizando lógica, observação e inteligência," +
                                " Holmes desvenda crimes e enigmas que parecem impossíveis para a polícia. Gênero: Mistério, Ficção.",
                        BookStatus.AVAILABLE,
                        "https://www.gutenberg.org/cache/epub/1661/pg1661-images.html", R.drawable.sherlock));
                
                dao.insert(new Book(null, "Moby Dick", "Herman Melville",
                        "A história acompanha o capitão Ahab em sua obsessiva caçada à baleia branca Moby Dick." +
                                " Durante a viagem marítima, a tripulação enfrenta perigos no oceano enquanto Ahab" +
                                " se torna cada vez mais consumido pela vingança. Gênero: Aventura, Ficção.",
                        BookStatus.AVAILABLE,
                        "https://www.gutenberg.org/cache/epub/2701/pg2701-images.html", R.drawable.moby_dick));
            }
        });
    }
}
