package com.example.android_app.provider;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "books")
public class Book {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "bookID")
    private int bookID;
    
    @ColumnInfo(name = "ID")
    private String ID;
    
    @ColumnInfo(name="title")
    private String title;

    @ColumnInfo(name="isbn")
    private String isbn;

    @ColumnInfo(name="author")
    private String author;

    @ColumnInfo(name="description")
    private String description;

    @ColumnInfo(name="price")
    private int price;

    public Book(String ID, String title, String isbn, String author, String description,  int price) {
        this.ID = ID;
        this.title = title;
        this.isbn = isbn;
        this.author = author;
        this.description = description;
        this.price = price;
    }

    public int getBookID() {
        return this.bookID;
    };

    public void setBookID(int bookID) {
        this.bookID = bookID;
    }

    public String getID() {
        return this.ID;
    }

    public String getTitle() {
        return this.title;
    }

    public String getIsbn() {
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

    public void setIsbn(String isbn) {
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
