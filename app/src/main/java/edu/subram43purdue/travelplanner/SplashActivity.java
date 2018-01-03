package edu.subram43purdue.travelplanner;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * This activity represents the Splash Screen for our Vacation Planner App. This is going to be the
 * activity that shows up when the user opens up the app. All that this java class contains
 * is an event handler thread that runs the splash screen for 1.5 seconds before creating and
 * starting a new Intent activity that switches the activity to the Start Activity
 */
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, StartActivity.class);
                startActivity(intent);
                finish();


            }
        }, 1500);
    }
}
