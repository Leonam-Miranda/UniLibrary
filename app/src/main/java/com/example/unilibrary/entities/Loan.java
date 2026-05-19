package com.example.unilibrary.entities;

import java.util.Date;

public class Loan {
    private Integer id;
    private User user;
    private Book book;
    private Date loanDate;
    private Date returnDate;

    public Loan() {}

    public Loan(Integer id, User user, Book book, Date loanDate, Date returnDate) {
        this.id = id;
        this.user = user;
        this.book = book;
        this.loanDate = loanDate;
        this.returnDate = returnDate;
    }

    public Integer getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Book getBook() {
        return book;
    }

    public Date getLoanDate() {
        return loanDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }
}
