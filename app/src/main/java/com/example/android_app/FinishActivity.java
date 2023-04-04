package com.example.android_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class FinishActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_finish);
        setContentView(R.layout.coordinator_layout);
        Toolbar toolbar = findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);

        // Floating Action Button
        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() { // onclick is attached
            @Override
            public void onClick(View view) {
                // cannot use "this" so use getApplcationContext()
                Toast.makeText(getApplicationContext(), "Item added", Toast.LENGTH_LONG).show();

                // Snackbar:
                // Snackbar.make(view, "Item Added", Snackbar.LENGTH_LONG).show();
                // snackbar pops up on the bottom of the screen. It may overlay in the constraint layout.

                // when fab button is clicked, the listeber undolistener is activated.
                // Snackbar.make(view, "item added", Snackbar.LENGTH_LONG).setAction("undo", UndoListener).show();
            }
        });
    }
//    View.OnClickListener UndoListener  = new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            // when the fab button is clicked it replaces the text on the mainActivity
//
//            TextView myText = findViewById(R.id.text);
//            myText.setText("undo clicked");
//        }
//    };
}