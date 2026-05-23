package com.example.unilibrary.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.example.unilibrary.db.Converters;

import java.util.Date;

@Entity(
        tableName = "loan",
        foreignKeys = {

                // Relaciona o campo "userId" dessa tabela
                // com o campo "id" da tabela User.
                // Garante que não dá pra criar um empréstimo
                // com um userId que não existe no banco.
                @ForeignKey(
                        entity = User.class,
                        parentColumns = "id",    // campo na tabela User
                        childColumns = "userId", // campo aqui no Loan
                        onDelete = ForeignKey.CASCADE
                        // CASCADE = se o usuário for deletado,
                        // todos os empréstimos dele são deletados junto.
                ),

                // Mesma coisa pra Book:
                // o bookId precisa existir na tabela Book.
                // Se o livro for deletado, os empréstimos
                // relacionados a ele são deletados também.
                @ForeignKey(
                        entity = Book.class,
                        parentColumns = "id",    // campo na tabela Book
                        childColumns = "bookId", // campo aqui no Loan
                        onDelete = ForeignKey.CASCADE
                )
        }
)
@TypeConverters(Converters.class)
public class Loan {

    @PrimaryKey(autoGenerate = true)
    private Integer id;
    private Integer userId;
    private Integer bookId;
    private Date loanDate;
    private Date returnDate;

    public Loan() {}

    public Loan(Integer id, Integer userId, Integer bookId, Date loanDate, Date returnDate) {
        this.id = id;
        this.userId = userId;
        this.bookId = bookId;
        this.loanDate = loanDate;
        this.returnDate = returnDate;
    }

    public Integer getId() {
        return id;
    }

    public Integer getUserId() {
        return userId;
    }

    public Integer getBookId() {
        return bookId;
    }

    public Date getLoanDate() {
        return loanDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }
}
