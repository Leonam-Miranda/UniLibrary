package com.example.unilibrary.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import com.example.unilibrary.db.Converters;

import com.example.unilibrary.enums.BookStatus;

@Entity(tableName = "book")
@TypeConverters(Converters.class)
public class Book {
    @PrimaryKey(autoGenerate = true)
    private Integer id;
    private String title;
    private String author;
    private String description;
    private BookStatus status;
    private String epubUrl;
    private int coverResId;

    public Book(){}
    public Book(Integer id, String title, String author, String description, BookStatus status, String epubUrl, int coverResId) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.description = description;
        this.status = status;
        this.epubUrl = epubUrl;
        this.coverResId = coverResId;

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BookStatus getStatus() {
        return status;
    }

    public void setStatus(BookStatus status) {
        this.status = status;
    }

    public String getEpubUrl() {
        return epubUrl;
    }

    public void setEpubUrl(String epubUrl) {
        this.epubUrl = epubUrl;
    }
    public int getCoverResId() {
        return coverResId;
    }
    public void setCoverResId(int coverResId) {
        this.coverResId = coverResId;
    }
}
