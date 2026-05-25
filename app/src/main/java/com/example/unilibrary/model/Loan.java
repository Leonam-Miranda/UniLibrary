package com.example.unilibrary.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.example.unilibrary.db.Converters;

import java.util.Date;

// model/Loan.java
@Entity(tableName = "loan")
@TypeConverters(Converters.class)
public class Loan {
    @PrimaryKey(autoGenerate = true)
    private Integer id;

    private Integer userId;
    private Integer bookId;

    private long loanDate;         // timestamp início
    private long dueDate;          // prazo: loanDate + 5 dias
    private Long returnDate;       // null enquanto não devolvido

    private double fine;           // multa acumulada
    private boolean returned;      // false = ativo

    public Loan() {}

    public Loan(Integer userId, Integer bookId) {
        this.userId = userId;
        this.bookId = bookId;
        this.loanDate = System.currentTimeMillis();
        this.dueDate = loanDate + (5L * 24 * 60 * 60 * 1000);
        this.fine = 0.0;
        this.returned = false;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    public Integer getBookId() { return bookId; }
    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }
    public long getLoanDate() { return loanDate; }
    public void setLoanDate(long loanDate) {
        this.loanDate = loanDate;
    }
    public long getDueDate() { return dueDate; }
    public void setDueDate(long dueDate) {
        this.dueDate = dueDate;
    }
    public Long getReturnDate() { return returnDate; }
    public void setReturnDate(Long returnDate) { this.returnDate = returnDate; }
    public double getFine() { return fine; }
    public void setFine(double fine) { this.fine = fine; }
    public boolean isReturned() { return returned; }
    public void setReturned(boolean returned) { this.returned = returned; }
}
