package entities;

import enums.BookStatus;

public class Book {
    private Integer id;
    private String title;
    private String author;
    private String description;
    private BookStatus status;
    private final String epubUrl;

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
}
