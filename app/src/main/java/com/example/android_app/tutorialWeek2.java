package com.example.android_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class tutorialWeek2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial_week2);
        Log.i("lab3", "onCreate");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("lab3", "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("lab3", "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("lab3", "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("lab3", "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("lab3", "onDestroy");
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i("lab3", "onSaveInstanceState");
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i("lab3", "onRestoreInstanceState");
    }
}