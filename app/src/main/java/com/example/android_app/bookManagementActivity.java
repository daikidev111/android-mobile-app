package com.example.android_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuView;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.internal.NavigationMenuItemView;
import com.google.android.material.navigation.NavigationView;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class bookManagementActivity extends AppCompatActivity {
//    private final List<Book> bookCollection = new ArrayList<>();
    TextView editID, editTitle, editISBN, editAuthor, editDescription, editPrice;
    FloatingActionButton fabAdd;

//    ArrayAdapter bookAdapter;
    ListView bookListView;

    Toolbar bookToolBar;
    DrawerLayout bookDrawer;

    NavigationView bookNavigation;

    ArrayList<Book> bookList = new ArrayList<Book>();

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    MyRecyclerViewAdapter adapter;

    public int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("lifecycle", "onCreate invoked");
        // setContentView(R.layout.activity_book_management);
        setContentView(R.layout.drawer);

        editID = findViewById(R.id.editID);
        editTitle = findViewById(R.id.editTitle);
        editISBN = findViewById(R.id.editISBN);
        editAuthor = findViewById(R.id.editAuthor);
        editDescription = findViewById(R.id.editDescription);
        editPrice = findViewById(R.id.editPrice);

        /* Request permissions to access SMS */
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS}, 0);
        registerReceiver(bookBroadCastReceiver, new IntentFilter(SMSReceiver.SMS_FILTER));

        // this creates a list view with an adapter
//        bookListView = findViewById(R.id.bookListView);
//        bookAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, bookList);
//        bookListView.setAdapter(bookAdapter);

        // creation of fab button
        fabAdd = findViewById(R.id.fab_add);

        // place a drawer on the toolbar
        bookToolBar = findViewById(R.id.bookToolBar);
        bookDrawer = findViewById(R.id.bookDrawer);
        setSupportActionBar(bookToolBar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                bookDrawer,
                bookToolBar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );
        bookDrawer.addDrawerListener(toggle);
        toggle.syncState();

        bookNavigation = findViewById(R.id.bookNavigation);
        bookNavigation.setNavigationItemSelectedListener(new bookNavigationListener());


        // Recycler View
        recyclerView = findViewById(R.id.rv);

        layoutManager = new LinearLayoutManager(this);  //A RecyclerView.LayoutManager implementation which provides similar functionality to ListView.
        recyclerView.setLayoutManager(layoutManager);   // Also StaggeredGridLayoutManager and GridLayoutManager or a custom Layout manager
        adapter = new MyRecyclerViewAdapter();
        adapter.setData(bookList);
        recyclerView.setAdapter(adapter);

        // onClick listener for the fab button
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Item added", Toast.LENGTH_LONG).show();
                showToast();
                Book book = new Book(editID.getText().toString(), editTitle.getText().toString(), editISBN.getText().toString(),
                        editDescription.getText().toString(), editAuthor.getText().toString(), editPrice.getText().toString());
                bookList.add(book);
                adapter.notifyDataSetChanged();
            }
        });

    }

    class bookNavigationListener implements NavigationView.OnNavigationItemSelectedListener {
        @SuppressLint("NotifyDataSetChanged")
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int id = item.getItemId();
            if (id == R.id.add_books) {
                Book book = new Book(editID.getText().toString(), editTitle.getText().toString(), editISBN.getText().toString(),
                        editDescription.getText().toString(), editAuthor.getText().toString(), editPrice.getText().toString());
                bookList.add(book);
                Toast.makeText(getApplicationContext(), "Book added", Toast.LENGTH_SHORT).show();
                adapter.notifyDataSetChanged();
            } else if (id == R.id.remove_last_book) {
                bookList.remove(bookList.size() - 1);
                Toast.makeText(getApplicationContext(), "Last Book Removed", Toast.LENGTH_SHORT).show();
                adapter.notifyDataSetChanged();
            } else if (id == R.id.remove_all_books) {
                bookList.clear();
                Toast.makeText(getApplicationContext(), "All Books Removed", Toast.LENGTH_SHORT).show();
                adapter.notifyDataSetChanged();
            } else if (id == R.id.close) {
                finishAffinity();
            }
            bookDrawer.closeDrawers();
            return true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       getMenuInflater().inflate(R.menu.option_menu, menu);
       return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.clear_fields) {
            clearInputs();
            Toast.makeText(getApplicationContext(), "Cleared Fields", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.load_book) {
            loadBook();
            Toast.makeText(getApplicationContext(), "Book Loaded", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.total_book) {
            Toast.makeText(getApplicationContext(), "Total Book: " + String.valueOf(bookList.size()), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
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
    public void onSaveInstanceState(@NonNull Bundle outState) {
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

    public void showToast() {
        String ID = editID.getText().toString();
        String Title = editTitle.getText().toString();
        String ISBN = editISBN.getText().toString();
        String Author = editAuthor.getText().toString();
        String Description = editDescription.getText().toString();
        int Price = Integer.parseInt(editPrice.getText().toString());

//        Book book = new Book(ID, Title, ISBN, Author, Description, editPrice.getText().toString());

        String toastMessage = "Confirm with the title and price: \n" + "Title: " + Title + "\n" + "Price: " + Price;

        // All the information (everything about the application) is contained in "this"
        // LENGTH_SHORT means displaying the toast for a short period of time
        Toast.makeText(bookManagementActivity.this, toastMessage, Toast.LENGTH_SHORT).show();
//        bookList.add(book);

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

    public void clearInputs() {
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

    public void loadBook() {
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

    private final BroadcastReceiver bookBroadCastReceiver = new BroadcastReceiver() {
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