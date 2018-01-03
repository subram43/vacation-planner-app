package edu.subram43purdue.travelplanner;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


/**
 * This activity is used for users to create a new vacation and add it to their current list of
 * vacations. This activity will prompt the user to enter a name, location, start date, and end date
 * for their vacation, and a button to create the vacation, or to cancel and go back to start menu.
 * This class also implements the View.OnClickListener interface in order to register a click with the
 * onClick(View v) method on multiple buttons which we have in this activity
 */
public class VacationActivity extends AppCompatActivity implements View.OnClickListener{
    /*
     * These are the fields for our Vacation Activity, similar to our StartActivity, where we had buttons,
     * but now we have EditText fields as well, which is where the user can type things in and get user
     * input. We also have a calendar object, which we will use to create a date picker dialog.
     */
    private EditText vacationNameText;
    private EditText locationText;
    private EditText startDateText;
    private EditText endDateText;
    private Button createVacationBtn;
    private Button cancelBtn;
    private Calendar calendar;

    /** This field represents a the vacation that is passed into this activity if this activity is used
     * to modify an existing vacation rather than create a new one
     */
    private Vacation vacation;
    /** This field is the index of the vacation that is passed in to be modified, and -1 if it is to
     * create a new vacation
     */
    private int passedVacationIndex;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacation);

        /* This is where we assign a value to the passedVacationIndex field based on which vacation
           was selected in the VacationsMainActivity class. If it is something selected from that class,
           meaning it already exists in the Array List, it will be the index of that value, otherwise,
           if it is a new vacation to be created, the value will be set to the default, -1
         */
        passedVacationIndex = getIntent().getIntExtra(MainActivity.VACATION_KEY, -1);


        // Assigns the fields we created in the Java class to the names of those fields in the xml file
        vacationNameText = (EditText)findViewById(R.id.vacation_name);
        locationText = (EditText)findViewById(R.id.vacation_location);
        startDateText = (EditText)findViewById(R.id.vacation_start_date);
        endDateText = (EditText)findViewById(R.id.vacation_end_date);
        createVacationBtn = (Button)findViewById(R.id.create_vacation_btn);
        cancelBtn = (Button)findViewById(R.id.cancel_btn);


        /* If the passedVacationIndex isn't -1, meaning it's already an existing vacation, set the
           EditText fields in to the current values of the vacation that's passed in
         */
        if(passedVacationIndex != -1) {
            vacation = MainActivity.vacations.get(passedVacationIndex);
            vacationNameText.setText(vacation.getName());
            locationText.setText(vacation.getLocation());
            startDateText.setText(vacation.getStartDate());
            endDateText.setText(vacation.getEndDate());
        }

        // This sets an action listener for the buttons in this activity, a click to be detected
        createVacationBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);

        /* These two methods are called to pop up a date picker dialog for the user when the start
           and end date EditText boxes are clicked
         */
        startDateChooser();
        endDateChooser();






    }

    /**
     * This is the method overridden from the View.OnClickListener interface which we have implemented
     * to register the click of a button and respond in a certain way.
     * @param v The view passed into the onClick method
     */
    /* This method uses a switch statement with the Id of the button as the parameter. In this scenario, if
       the cancel button is pressed, it just takes the user back to the previous screen and doesn't
       save anything. However, if the create vacation button is pressed, it checks to make sure that
       the fields are not empty, and that the start date of the vacation is correctly put in before
       the end date. If either of these conditions aren't met, an Alert Dialog is created and will pop
       up to tell the user that they need to fix something before creating a vacation
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel_btn:
                onBackPressed();
                break;
            case R.id.create_vacation_btn:
                String vacationName = vacationNameText.getText().toString();
                String location = locationText.getText().toString();
                String startDate = startDateText.getText().toString();
                String endDate = endDateText.getText().toString();

                if(vacationName.isEmpty() || location.isEmpty() || startDate.isEmpty() || endDate.isEmpty()) {
                    AlertDialog alertDialog = new AlertDialog.Builder(VacationActivity.this).create();
                    alertDialog.setTitle("Error");
                    alertDialog.setMessage("Fields cannot be empty.");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "CLOSE",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
                else if(endBeforeStartDate(startDate, endDate)) {
                    AlertDialog alertDialog = new AlertDialog.Builder(VacationActivity.this).create();
                    alertDialog.setTitle("Error");
                    alertDialog.setMessage("End date cannot be before start date!");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "CLOSE",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
                else{
                    /* After the user has properly typed in all fields and saves their vacation, this
                    if statement checks if the vacation is one they are changing, or a new one all together,
                    and if it is one they are changing, it will just set each field to the changed values.
                    Otherwise, a new vacation will be created with the given fields and be added to the vacations list
                     */
                    if(passedVacationIndex != -1) {
                        vacation.setName(vacationName);
                        vacation.setLocation(location);
                        vacation.setStartDate(startDate);
                        vacation.setEndDate(endDate);
                        MainActivity.vacations = FileSaver.readVacationList();
                        onBackPressed();
                    }
                    else {
                        ArrayList<Event> events = new ArrayList<>();
                        Vacation vacation = new Vacation(vacationName, location, startDate, endDate, events);

                        MainActivity.vacations.add(vacation);
                        MainActivity.vacations = FileSaver.readVacationList();
                        onBackPressed();
                    }
                }
        }
    }

    /** This helper method is used to check whether the end date is before the start date, and is
     * used in the onClick(View v) method. A try catch is necessary as a ParseException is a checked
     * exception and must be caught
     */
    private boolean endBeforeStartDate(String date1, String date2) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");

            Date d1 = sdf.parse(date1);
            Date d2 = sdf.parse(date2);

            return d1.after(d2);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * The startDateChooser() and endDateChooser() methods are used to pop up a date picker dialog
     * if either the startDateText or endDateText fields are clicked, allowing the user to pick a date
     * from a calender instead of typing one in.
     */
    private void startDateChooser() {
        calendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateStartDate();
            }

        };

        startDateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(VacationActivity.this, date, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    /** This method sets the text in the EditText fields for the date based on the date the user selects
    in the calendar
     */
    private void updateStartDate() {
        String sdf = "MM/dd/yy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(sdf);

        startDateText.setText(simpleDateFormat.format(calendar.getTime()));
    }

    /**
     * This method does the same thing as startDateChooser() but for the end date field
     */
    private void endDateChooser() {
        calendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateEndDate();
            }

        };

        endDateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(VacationActivity.this, date, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void updateEndDate() {
        String sdf = "MM/dd/yy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(sdf);

        endDateText.setText(simpleDateFormat.format(calendar.getTime()));
    }
}
