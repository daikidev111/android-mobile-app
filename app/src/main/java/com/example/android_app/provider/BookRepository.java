package com.example.android_app.provider;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import java.util.List;

public class BookRepository {
    private BookDao mBookDao;
    private LiveData<List<Book>> mAllBooks;

    BookRepository(@NonNull Application application) {
        BookDatabase db = BookDatabase.getDatabase(application);
        mBookDao = db.bookDao();
        mAllBooks = mBookDao.getAllBook();
    }

    LiveData<List<Book>> getAllBooks() {
        return mAllBooks;
    }

    public LiveData<Integer> getBookCount() {
        return mBookDao.getBookCount();
    }

    void insert(Book book) {
        BookDatabase.databaseWriteExecutor.execute(() -> mBookDao.addBook(book));
    }

    void deleteAll() {
        BookDatabase.databaseWriteExecutor.execute(() -> {
          mBookDao.deleteAllBooks();
        });
    }

    void deleteLastBook() {
        BookDatabase.databaseWriteExecutor.execute(() -> {
            mBookDao.deleteLastBook();
        });
    }

    void deleteExpensiveBooks() {
        BookDatabase.databaseWriteExecutor.execute(() -> {
            mBookDao.deleteExpensiveBooks();
        });
    }
}