package com.example.unilibrary.model;

import androidx.room.Embedded;
import androidx.room.Relation;

public class LoanWithBook {
    @Embedded
    public Loan loan;

    @Relation(
            parentColumn = "bookId",
            entityColumn = "id"
    )
    public Book book;
}
