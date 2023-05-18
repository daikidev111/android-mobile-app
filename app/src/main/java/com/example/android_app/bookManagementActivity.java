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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.example.android_app.provider.Book;
import com.example.android_app.provider.BookViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.StringTokenizer;

public class bookManagementActivity extends AppCompatActivity {
//    private final List<Book> bookCollection = new ArrayList<>();
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
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int motion = motionEvent.getActionMasked();
                int x_end, y_end;

                switch(motion) {
                    case (MotionEvent.ACTION_DOWN):
                        x_start = (int) motionEvent.getX();
                        y_start = (int) motionEvent.getY();

                        if (x_start < 50 && y_start < 150) {
                            if (editAuthor != null || editAuthor.getText().toString() != "") {
                                editAuthor.setText(editAuthor.getText().toString().toUpperCase());
                            }
                        }
                        return true;

                    case (MotionEvent.ACTION_MOVE):
                        x_end = (int) motionEvent.getX();
                        y_end = (int) motionEvent.getY();

                        // subtraction of y_end and y_start to check if there is a large distance change in y-axis
                        if(Math.abs(y_end - y_start) < horizontal_threshold) {
                            if(x_start > x_end) { // Right to Left swipe
                                Toast.makeText(getApplicationContext(), "Item added", Toast.LENGTH_LONG).show();
                                showToast();
                                Book book = new Book(editID.getText().toString(), editTitle.getText().toString(), editISBN.getText().toString(),
                                        editDescription.getText().toString(), editAuthor.getText().toString(), Integer.parseInt(editPrice.getText().toString()));
                                mBookViewModel.insert(book);
                            } else { // Left to Right swipe
                                editPrice.setText(String.valueOf(Integer.parseInt(editPrice.getText().toString()) + 1));
                            }
                        }
                        return true;
                    case (MotionEvent.ACTION_UP):
                        x_end = (int) motionEvent.getX();
                        y_end = (int) motionEvent.getY();

                        // top to bottom
                        if (y_end > y_start && Math.abs(y_end - y_start) > vertical_threshold) {
                            finishAffinity();
                            Log.i("LOG", "finish call() executed");
                        }
                        // bottom to top
                        if (y_end < y_start && Math.abs(y_start - y_end) > vertical_threshold) {
                            clearInputs();
                            Toast.makeText(getApplicationContext(), "Cleared Fields", Toast.LENGTH_SHORT).show();
                        }
                        return true;

                    default:
                        return false;
                }
            }
        });

    }

    static class gestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapConfirmed(@NonNull MotionEvent e) {
            Log.i("Debug", "OnSingleTapConfirmed");
            // TODO: generate a new random ISBN
            return super.onSingleTapConfirmed(e);
        }
        @Override
        public boolean onDoubleTap(@NonNull MotionEvent e) {
            Log.i("Debug", "onDoubleTap");
            // TODO: add clearAllFields()
            return super.onDoubleTap(e);
        }
        @Override
        public boolean onScroll(@NonNull MotionEvent e1, @NonNull MotionEvent e2, float distanceX, float distanceY) {
            // e1 is the touchDown Event -> when you touch down the screen
            // e2 is the move motion event that triggered the CURRENT onScroll
            // distance X is the distance along the X axis that has been scrolled
            // distance Y is the distance along the Y axis that has been scrolled
            // distance is NOT the distance between e1 and e2
            // distance = current e2 - previous e2
            Log.i("Week 11", "onScroll");
            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        @Override
        public boolean onFling(@NonNull MotionEvent e1, @NonNull MotionEvent e2, float velocityX, float velocityY) {
            // TODO: invoke moveTaskToBack(true);
            if (velocityX > 1000 || velocityY > 1000) {
                Log.i("Debug", "onFling");
            }
            return super.onFling(e1, e2, velocityX, velocityY);
        }
        @Override
        public void onLongPress(@NonNull MotionEvent e) {
            // TODO: load default/saved values
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