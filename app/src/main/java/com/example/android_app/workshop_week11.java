package com.example.android_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.gesture.Gesture;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.TextView;

public class workshop_week11 extends AppCompatActivity {

    View touchConstraint;
    TextView textView;

    GestureDetector gestureDetector;
    ScaleGestureDetector scaleGestureDetector;

    // for 2 finger tracking the ids to get multiple x distances
    int finger1_id, finger2_id;

//    Convenience class is to simply the method or class used int he application and keep
//    the rest that is out of interest(not important and do not want to be used) hidden.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workshop_week11);

        touchConstraint = findViewById(R.id.touchConstraint);
        textView = findViewById(R.id.textView);
        gestureDetector = new GestureDetector(this, new MyGestureDetector());
        scaleGestureDetector = new ScaleGestureDetector(this, new MyScaleGestureDetector());

        touchConstraint.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // you need to add below to let the gestureDector know what event it is on
                gestureDetector.onTouchEvent(event);
                scaleGestureDetector.onTouchEvent(event);

                // multi-pointers: multiple finger detection

                // It looks like below (for three fingers);
                // Finger 1: = index 0; id = 11111 <- random ID
                // Finger 2: = index 1; id = 22222
                // Finger 3: = index 2; id = 33333
                // Why do we need index and id
                // Id is something assigned uniquely to each finger, index changes depending on the motion
                // leave finger 2, index will be changed, but ID still REMAINS
                // SCENARIO EXAMPLE:
                // index of finger 1 = index 0 and the index of finger 3 will be changed to 1
                // once you release all the fingers, all the index will be gone

                // demo
                if (event.getPointerCount() == 1) { // one finger event
                    textView.setText("X1: " + String.valueOf(event.getX()));
                } else if (event.getPointerCount() == 2) {
                    // how to get multiple X distances from 2 fingers?
                    // it cannot be tested on the emulator
                    finger1_id = event.getPointerId(0);
                    finger2_id = event.getPointerId(1);
                    textView.setText("X1: " + String.valueOf(event.getX(finger1_id)) +
                            "\n X2: " +String.valueOf(event.getX(finger2_id)));
                }
                return true;
                // if false, parent takes over, and the final outcome will be shown
            }
        });
    }

    class MyScaleGestureDetector extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(@NonNull ScaleGestureDetector detector) {
            // retrieve pinching information
//            TextView.setText(String.valueOf(detector.getScaleFactor()));
            // pinching, such as zoom in and zoom out
            // how to test on the emulator?
            // click control and move your mouse
            Log.i("week11", "MyScaleGestureDetector");
            return super.onScale(detector);
        }

        @Override
        public boolean onScaleBegin(@NonNull ScaleGestureDetector detector) {
            Log.i("week11", "MyScaleGestureDetector");
            return super.onScaleBegin(detector);
        }

        @Override
        public void onScaleEnd(@NonNull ScaleGestureDetector detector) {
            Log.i("week11", "MyScaleGestureDetector");
            super.onScaleEnd(detector);
        }
    }

    // this is how the convinience class works, you can just choose a method of gestures you want to use
    // inner class (convinient class) for invoking a specific method of GestureDetector methods
    // command + N to generate override methods/implement
    class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapUp(@NonNull MotionEvent e) {
            // or you could use onSingleTapUpConfirmed
            Log.i("Week 11", "OnSingleTapUp");
            return super.onSingleTapUp(e);
        }

        @Override
        public void onLongPress(@NonNull MotionEvent e) {
            Log.i("Week 11", "onLongPress");
            super.onLongPress(e);
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
            // onFling is invoked after onScroll after achieving certain momentum
            // similar to scroll, but with some momentum required
            // if onFling is too sensitive
            // -> if velocity is more than the certain velocity then invoke
            if (velocityX > 1000 || velocityY > 1000) {
                Log.i("Week 11", "onFling");
            }
            return super.onFling(e1, e2, velocityX, velocityY);
        }

        @Override
        public void onShowPress(@NonNull MotionEvent e) {
            Log.i("Week 11", "onShowPress");
            super.onShowPress(e);
        }

        @Override
        public boolean onDoubleTap(@NonNull MotionEvent e) {
            // onSingleTapUp -> onDoubleTap
            Log.i("Week 11", "onDoubleTap");
            return super.onDoubleTap(e);
        }
    }
}