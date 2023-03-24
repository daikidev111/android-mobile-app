package com.example.android_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class bookManagementActivity extends AppCompatActivity {
    private final List<Book> bookCollection = new ArrayList<>();
    TextView editID, editTitle, editISBN, editAuthor, editDescription, editPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("lifecycle", "onCreate invoked");
        setContentView(R.layout.activity_book_management);

        editID = findViewById(R.id.editID);
        editTitle = findViewById(R.id.editTitle);
        editISBN = findViewById(R.id.editISBN);
        editAuthor = findViewById(R.id.editAuthor);
        editDescription = findViewById(R.id.editDescription);
        editPrice = findViewById(R.id.editPrice);

        /* Request permissions to access SMS */
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS}, 0);
        registerReceiver(bookBroadCastReceiver, new IntentFilter(SMSReceiver.SMS_FILTER));
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("lifecycle", "onStart invoked");

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
        Log.i("lifecycle", "onResume invoked");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("lifecycle", "onPause invoked");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("lifecycle", "onStop invoked");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("lifecycle", "onDestroy invoked");
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i("lifecycle", "onSaveInstanceState invoked");
        outState.putString("title", editTitle.getText().toString());
        outState.putString("ISBN", editISBN.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i("lifecycle", "onRestoreInstanceState invoked");
        editTitle.setText(savedInstanceState.getString("title"));
        editISBN.setText(savedInstanceState.getString("ISBN"));
        editID.setText("");
        editAuthor.setText("");
        editDescription.setText("");
        editPrice.setText("");
    }

    public void showToast(View view) {
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

    private BroadcastReceiver bookBroadCastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String msg = intent.getStringExtra(SMSReceiver.SMS_MSG_KEY);
            StringTokenizer sT = new StringTokenizer(msg, "|");
            String ID = sT.nextToken();
            String Title = sT.nextToken();
            String ISBN = sT.nextToken();
            String Author = sT.nextToken();
            String Description = sT.nextToken();
            String Price = sT.nextToken();
            String addBool = sT.nextToken();
            boolean addPriceBool = Boolean.parseBoolean(addBool);

            editID.setText(ID);
            editISBN.setText(ISBN);
            editTitle.setText(Title);
            editAuthor.setText(Author);
            editDescription.setText(Description);

            if (addPriceBool) {
                Price = String.valueOf(Integer.parseInt(Price) + 100);
            } else {
               Price = String.valueOf(Integer.parseInt(Price) + 5);
            }
            editPrice.setText(Price);
        }
    };
}