package com.example.unilibrary.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.unilibrary.enums.BookStatus;

// data/model/Book.java
@Entity(tableName = "book")
public class Book {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String title;
    public String author;
    public String genre;
    public String pdfAssetPath;
    public BookStatus status;
    public boolean avaible = true;
}