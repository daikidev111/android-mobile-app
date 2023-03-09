package com.example.android_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class bookManagementActivity extends AppCompatActivity {
    private List<Book> bookCollection = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_management);
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
        int ISBN = Integer.parseInt(editISBN.getText().toString());
        String Author = editAuthor.getText().toString();
        String Description = editDescription.getText().toString();
        int Price = Integer.parseInt(editPrice.getText().toString());

        Book book = new Book(ID, Title, ISBN, Author, Description, Price);

        String toastMessage = "Confirm with the title and price: \n" + "Title: " + Title + "\n" + "Price: " + Price;

        // All the information (everything about the application) is contained in "this"
        // LENGTH_SHORT means displaying the toast for a short period of time
        Toast.makeText(bookManagementActivity.this, toastMessage, Toast.LENGTH_SHORT).show();
        bookCollection.add(book);
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
}