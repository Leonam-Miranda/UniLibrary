package com.example.unilibrary.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.unilibrary.db.Converters;

import java.util.Date;

@Entity(
    tableName = "loan",
    foreignKeys = {
        @ForeignKey(entity = User.class,
                    parentColumns = "id",
                    childColumns = "userId",
                    onDelete = ForeignKey.CASCADE),
        @ForeignKey(entity = Book.class,
                    parentColumns = "id",
                    childColumns = "bookId",
                    onDelete = ForeignKey.CASCADE)
    },
    indices = {@Index("userId"), @Index("bookId")}
)
@TypeConverters(Converters.class)
public class Loan {
    @PrimaryKey(autoGenerate = true)
    private Integer id;

    private Integer userId;
    private Integer bookId;

    private Date loanDate;
    private Date dueDate;
    private Date returnDate;

    private double fine;
    private boolean returned;
    private boolean renewed = false;

    public Loan() {}

    public Loan(Integer userId, Integer bookId) {
        this.userId = userId;
        this.bookId = bookId;
        this.loanDate = new Date();
        this.dueDate = new Date(loanDate.getTime() + (5L * 24 * 60 * 60 * 1000));
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
    public Date getLoanDate() { return loanDate; }
    public void setLoanDate(Date loanDate) {
        this.loanDate = loanDate;
    }
    public Date getDueDate() { return dueDate; }
    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }
    public Date getReturnDate() { return returnDate; }
    public void setReturnDate(Date returnDate) { this.returnDate = returnDate; }
    public double getFine() { return fine; }
    public void setFine(double fine) { this.fine = fine; }
    public boolean isReturned() { return returned; }
    public void setReturned(boolean returned) { this.returned = returned; }
    public boolean isRenewed() { return renewed; }        // ← novo
    public void setRenewed(boolean renewed) { this.renewed = renewed; } // ← novo
}
