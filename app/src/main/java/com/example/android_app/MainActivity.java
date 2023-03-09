package com.example.android_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void showMessage(View view) {
        TextView softwareTextView = findViewById(R.id.unit_software);
        TextView idTextView = findViewById(R.id.unit_code);
        TextView nameTextView = findViewById(R.id.unit_name);

        ITUnit itUnit = new ITUnit("Mobile Application Development", "FIT2081", "Android Studio");
        softwareTextView.setText(itUnit.getSoftware());
        idTextView.setText(itUnit.getID());
        nameTextView.setText(itUnit.getName());
    }

    public void goToNext(View view) {
        Intent myIntent = new Intent(MainActivity.this, bookManagementActivity.class);
        startActivity(myIntent);
    }
}