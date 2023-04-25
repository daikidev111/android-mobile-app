package com.example.android_app.provider;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.Objects;

public class BookViewModel extends AndroidViewModel {
    private BookRepository mBookRepository;
    private LiveData<List<Book>> mAllBooks;

    public BookViewModel(@NonNull Application application) {
        super(application);
        mBookRepository = new BookRepository(application);
        mAllBooks = mBookRepository.getAllBooks();
    }

    public LiveData<List<Book>> getAllBooks() {
        return mAllBooks;
    }

    public LiveData<Integer> getBookCount() {
        return mBookRepository.getBookCount();
    }


    public void insert(Book book) {
        mBookRepository.insert(book);
    }

    public int countBookRecords() {
        // System.out.println(mAllBooks.getValue().size());
        return Objects.requireNonNull(mAllBooks.getValue()).size();
    }
    public void deleteAll() {
        mBookRepository.deleteAll();
    }

    public void deleteLastBook() {
        mBookRepository.deleteLastBook();
    }

    public void deleteExpensiveBooks() {
        mBookRepository.deleteExpensiveBooks();
    }
}