package com.example.android_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
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
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.example.android_app.provider.Book;
import com.example.android_app.provider.BookViewModel;
import com.example.android_app.utility.randomString;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.StringTokenizer;

public class bookManagementActivity extends AppCompatActivity {
    TextView editID, editTitle, editISBN, editAuthor, editDescription, editPrice;
    ConstraintLayout motionLayout;
    FloatingActionButton fabAdd;

    Toolbar bookToolBar;
    DrawerLayout bookDrawer;

    NavigationView bookNavigation;
    GestureDetector gestureDetector;

    // declare view model for book object
    private BookViewModel mBookViewModel;

    // variables for keeping track of touch movement
    private int x_start, y_start;
    private final static int vertical_threshold = 500;
    private final static int horizontal_threshold = 50;


    @SuppressLint("ClickableViewAccessibility")
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
        motionLayout = findViewById(R.id.motion_layout);

        /* Request permissions to access SMS */
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS}, 0);
        registerReceiver(bookBroadCastReceiver, new IntentFilter(SMSReceiver.SMS_FILTER));

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

        // instantiate bookViewModel
        mBookViewModel = new ViewModelProvider(this).get(BookViewModel.class);

        // Fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_book_frame, new BookRecyclerViewFragment()).commit();

        // onClick listener for the fab button
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Item added", Toast.LENGTH_LONG).show();
                showToast();
                Book book = new Book(editID.getText().toString(), editTitle.getText().toString(), editISBN.getText().toString(),
                        editDescription.getText().toString(), editAuthor.getText().toString(), Integer.parseInt(editPrice.getText().toString()));
                mBookViewModel.insert(book);
            }
        });

        // instantiate a gesture detector class
        gestureDetector = new GestureDetector(this, new gestureDetector());

        // make the layout accessible with motion event
        motionLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                gestureDetector.onTouchEvent(motionEvent);
                return true;
            }
        });
    }

    class gestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapConfirmed(@NonNull MotionEvent e) {
            Log.i("Debug", "OnSingleTapConfirmed");
            String randomISBNString = randomString.generateNewRandomString(10);
//            editISBN = findViewById(R.id.editISBN);
            editISBN.setText(randomISBNString);

            return super.onSingleTapConfirmed(e);
        }
        @Override
        public boolean onDoubleTap(@NonNull MotionEvent e) {
            Log.i("Debug", "onDoubleTap");
            clearInputs();
            return super.onDoubleTap(e);
        }
        @Override
        public boolean onScroll(@NonNull MotionEvent e1, @NonNull MotionEvent e2, float distanceX, float distanceY) {
            int price = 0;
            if (editPrice.getText().length() != 0) {
                price = Integer.parseInt(editPrice.getText().toString());
            }

            if (Math.abs(distanceX) >= 50 && Math.abs(distanceY) < 50) {
                if (e2.getX() > e1.getX()) {
                    price += Math.abs(distanceX);
                    editPrice.setText(String.valueOf(price));
                } else {
                    price -= Math.abs(distanceX);
                    if (price < 0) {
                        price = 0;
                    }
                    editPrice.setText(String.valueOf(price));
                }
            } else if (Math.abs(distanceY) >= 50 && Math.abs(distanceX) < 50) {
                Log.i("Debug", "vertical scroll");
                if (e2.getY() > e1.getY()) {
                    editTitle.setText("untitled");
                }
            }
            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        @Override
        public boolean onFling(@NonNull MotionEvent e1, @NonNull MotionEvent e2, float velocityX, float velocityY) {
            if (velocityX > 1000 || velocityY > 1000) {
                moveTaskToBack(true);
                Log.i("Debug", "onFling");
            }
            return super.onFling(e1, e2, velocityX, velocityY);
        }
        @Override
        public void onLongPress(@NonNull MotionEvent e) {
            loadBook();
            Log.i("Debug", "onLongPress");
            super.onLongPress(e);
        }
    }

    class bookNavigationListener implements NavigationView.OnNavigationItemSelectedListener {
        @SuppressLint("NotifyDataSetChanged")
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int id = item.getItemId();
            if (id == R.id.add_books) {
                Book book = new Book(editID.getText().toString(), editTitle.getText().toString(), editISBN.getText().toString(),
                        editDescription.getText().toString(), editAuthor.getText().toString(), Integer.parseInt(editPrice.getText().toString()));
                mBookViewModel.insert(book);
                Toast.makeText(getApplicationContext(), "Book added", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.remove_last_book) {
                mBookViewModel.deleteLastBook();
                Toast.makeText(getApplicationContext(), "Last Book Removed", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.remove_all_books) {
                mBookViewModel.deleteAll();
                Toast.makeText(getApplicationContext(), "All Books Removed", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.view_list) {
                Intent bookListIntent = new Intent(bookManagementActivity.this, bookListActivity.class);
                startActivity(bookListIntent);
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
            if (mBookViewModel != null) {
                mBookViewModel.getBookCount().observe(this, count -> {
                    Toast.makeText(getApplicationContext(), "Total Book: " + count, Toast.LENGTH_SHORT).show();
                });
            }
        } else if (id == R.id.remove_expensive_books) {
            if (mBookViewModel != null) {
                mBookViewModel.deleteExpensiveBooks();
                Toast.makeText(getApplicationContext(), "Expensive Books Removed", Toast.LENGTH_SHORT).show();
            }
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

        String toastMessage = "Confirm with the title and price: \n" + "Title: " + Title + "\n" + "Price: " + Price;

        // All the information (everything about the application) is contained in "this"
        // LENGTH_SHORT means displaying the toast for a short period of time
        Toast.makeText(bookManagementActivity.this, toastMessage, Toast.LENGTH_SHORT).show();

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