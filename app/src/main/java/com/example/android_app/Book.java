package com.example.android_app;

public class Book {
    private String ID;
    private String title;
    private int isbn;
    private String author;
    private String description;
    private int price;

    public Book(String ID, String title, int isbn, String author, String description, int price) {
        this.ID = ID;
        this.title = title;
        this.isbn = isbn;
        this.author = author;
        this.description = description;
        this.price = price;
    }

    public String getID() {
        return this.ID;
    }

    public String getTitle() {
        return this.title;
    }

    public int getIsbn() {
        return this.isbn;
    }

    public String getAuthor() {
        return this.author;
    }

    public String getDescription() {
        return this.description;
    }

    public int getPrice() {
        return this.price;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setIsbn(int isbn) {
        this.isbn = isbn;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
