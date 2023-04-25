package com.example.android_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class bookListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);
        getSupportFragmentManager().beginTransaction().replace(R.id.book_list_frame, new BookRecyclerViewFragment()).commit();
    }
}