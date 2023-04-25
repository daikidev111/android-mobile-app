package com.example.android_app.provider;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface BookDao {
    @Query("SELECT * FROM books")
    LiveData<List<Book>> getAllBook();

    @Query("SELECT COUNT(*) FROM books")
    LiveData<Integer> getBookCount();

    @Insert
    void addBook(Book book);

    @Query("DELETE FROM books WHERE ID=:ID")
    void deleteBookByID(String ID);

    @Query("DELETE FROM books")
    void deleteAllBooks();

    @Query("DELETE FROM books WHERE bookID = (SELECT MAX(bookID) FROM books)")
    void deleteLastBook();

    @Query("DELETE FROM books WHERE price > 50")
    void deleteExpensiveBooks();

}