package edu.subram43purdue.travelplanner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * This activity is the first thing that shows up when the app is started after the splash screen.
 * This is like the main menu of the app. This activity welcomes the user to the vacation planner app,
 * and gives the user two options, to either view their vacations, or create a new vacation
 */
public class StartActivity extends AppCompatActivity {
    /*
     * These two buttons are fields to represent the buttons we created in our activity_start layout
     * resource file. In order to make them work as buttons, we also need to define them in java
     */
    private Button viewVacations;
    private Button createVacation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        /* This line is used to read vacations from external storage. To be able to do that,
          we use the static method we created in the FileSaver class using java's object I/O capabilities.
          This allows us to restore our vacations even after we close down the app and restart
         */
        MainActivity.vacations = FileSaver.readVacationList();

        // Assign the button fields we created in Java to the button we made in the layout xml file
        viewVacations = (Button)findViewById(R.id.view_vacations_btn);
        createVacation = (Button)findViewById(R.id.create_vacation_btn);


        /* This registers the click of the viewVacations button and sends the user to the main
           activity where they can view all of their existing vacations
         */
        viewVacations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


        /* This registers the click of the createVacation button and sends the user to the vacation
           activity where they can create a new vacation
         */
        createVacation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this, VacationActivity.class);
                startActivity(intent);
            }
        });




    }


    /**
     * This method is used when the user exits the application, so that the vacations can be written out
     * to external storage before the app gets closed
     */
    @Override
    protected void onPause() {
        super.onPause();
        FileSaver.writeVacationList(MainActivity.vacations);

    }
}
