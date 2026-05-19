package com.example.unilibrary.entities;

import com.example.unilibrary.enums.BookStatus;

public class Book {
    private Integer id;
    private String title;
    private String author;
    private String description;
    private BookStatus status;
    private String epubUrl;

    public Book(){}
    public Book(Integer id, String title, String author, String description, BookStatus status, String epubUrl) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.description = description;
        this.status = status;
        this.epubUrl = epubUrl;
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getDescription() {
        return description;
    }

    public BookStatus getStatus() {
        return status;
    }

    public String getEpubUrl() {
        return epubUrl;
    }
}
