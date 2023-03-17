package com.example.android_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class bookManagementActivity extends AppCompatActivity {
    private List<Book> bookCollection = new ArrayList<>();
    private String savedTitle;
    private String savedISBN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_management);
        Log.i("a", "onCreate");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("a", "onStart");

        EditText editID = findViewById(R.id.editID);
        EditText editTitle = findViewById(R.id.editTitle);
        EditText editISBN = findViewById(R.id.editISBN);
        EditText editAuthor = findViewById(R.id.editAuthor);
        EditText editDescription = findViewById(R.id.editDescription);
        EditText editPrice = findViewById(R.id.editPrice);

        SharedPreferences bookData = getSharedPreferences("last_book", 0);
        String ID = bookData.getString("id", "");
        String Title = bookData.getString("title", "");
        String ISBN = bookData.getString("ISBN", "");
        String Author = bookData.getString("author", "");
        String Description = bookData.getString("description", "");
        int Price = bookData.getInt("price", 0);

        editID.setText(ID);
        editTitle.setText(Title);
        editISBN.setText(ISBN);
        editAuthor.setText(Author);
        editDescription.setText(Description);
        editPrice.setText(String.valueOf(Price));
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("a", "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("a", "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("a", "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("a", "onDestroy");
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i("a", "onSaveInstanceState");
        EditText editTitle = findViewById(R.id.editTitle);
        EditText editISBN = findViewById(R.id.editISBN);
        savedTitle = editTitle.getText().toString();
        savedISBN = editISBN.getText().toString();
        outState.putString("title", savedTitle);
        outState.putString("ISBN", savedISBN);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i("a", "onRestoreInstanceState");
        EditText editID = findViewById(R.id.editID);
        EditText editTitle = findViewById(R.id.editTitle);
        EditText editISBN = findViewById(R.id.editISBN);
        EditText editAuthor = findViewById(R.id.editAuthor);
        EditText editDescription = findViewById(R.id.editDescription);
        EditText editPrice = findViewById(R.id.editPrice);

        editTitle.setText(savedInstanceState.getString("title"));
        editISBN.setText(savedInstanceState.getString("ISBN"));

        editID.setText("");
        editAuthor.setText("");
        editDescription.setText("");
        editPrice.setText("");
    }

    public void showToast(View view) {
        EditText editID = findViewById(R.id.editID);
        EditText editTitle = findViewById(R.id.editTitle);
        EditText editISBN = findViewById(R.id.editISBN);
        EditText editAuthor = findViewById(R.id.editAuthor);
        EditText editDescription = findViewById(R.id.editDescription);
        EditText editPrice = findViewById(R.id.editPrice);

        String ID = editID.getText().toString();
        String Title = editTitle.getText().toString();
        String ISBN = editISBN.getText().toString();
        String Author = editAuthor.getText().toString();
        String Description = editDescription.getText().toString();
        int Price = Integer.parseInt(editPrice.getText().toString());

        Book book = new Book(ID, Title, ISBN, Author, Description, Price);

        String toastMessage = "Confirm with the title and price: \n" + "Title: " + Title + "\n" + "Price: " + Price;

        // All the information (everything about the application) is contained in "this"
        // LENGTH_SHORT means displaying the toast for a short period of time
        Toast.makeText(bookManagementActivity.this, toastMessage, Toast.LENGTH_SHORT).show();

        bookCollection.add(book);

        SharedPreferences bookData = getSharedPreferences("last_book", 0);
        SharedPreferences.Editor bookEditor = bookData.edit();
        bookEditor.putString("id", ID);
        bookEditor.putString("title", Title);
        bookEditor.putString("ISBN", ISBN);
        bookEditor.putString("author", Author);
        bookEditor.putString("description", Description);
        bookEditor.putInt("price", Price);
        bookEditor.apply();
    }

    public void clearInputs(View view) {
        EditText editID = findViewById(R.id.editID);
        EditText editTitle = findViewById(R.id.editTitle);
        EditText editISBN = findViewById(R.id.editISBN);
        EditText editAuthor = findViewById(R.id.editAuthor);
        EditText editDescription = findViewById(R.id.editDescription);
        EditText editPrice = findViewById(R.id.editPrice);
        editID.getText().clear();
        editTitle.getText().clear();
        editISBN.getText().clear();
        editAuthor.getText().clear();
        editDescription.getText().clear();
        editPrice.getText().clear();

        Toast.makeText(bookManagementActivity.this, "Cleared Inputs", Toast.LENGTH_SHORT).show();
    }

//    public void doublePrice(View view) {
//        EditText editPrice = findViewById(R.id.editPrice);
//        int price = Integer.parseInt(editPrice.getText().toString()) * 2;
//        Toast.makeText(bookManagementActivity.this, "Doubled the price: " + price, Toast.LENGTH_SHORT).show();
//        editPrice.setText(String.valueOf(price));
//    }

    public void loadBook(View view) {
        EditText editID = findViewById(R.id.editID);
        EditText editTitle = findViewById(R.id.editTitle);
        EditText editISBN = findViewById(R.id.editISBN);
        EditText editAuthor = findViewById(R.id.editAuthor);
        EditText editDescription = findViewById(R.id.editDescription);
        EditText editPrice = findViewById(R.id.editPrice);

        SharedPreferences bookData = getSharedPreferences("last_book", 0);
        String ID = bookData.getString("id", "");
        String Title = bookData.getString("title", "");
        String ISBN = bookData.getString("ISBN", "");
        String Author = bookData.getString("author", "");
        String Description = bookData.getString("description", "");
        int Price = bookData.getInt("price", 0);

        editID.setText(ID);
        editTitle.setText(Title);
        editISBN.setText(ISBN);
        editAuthor.setText(Author);
        editDescription.setText(Description);
        editPrice.setText(String.valueOf(Price));
    }

    public void fixedISBN(View view) {
        SharedPreferences bookData = getSharedPreferences("last_book", 0);
        SharedPreferences.Editor bookEditor = bookData.edit();
        bookEditor.putString("ISBN", "00112233");
        bookEditor.apply();
    }
}