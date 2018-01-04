package edu.subram43purdue.travelplanner;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/** This activity is used to create a new leisure event inside a vacation. A leisure event is basically
 * any event in a vacation that does not involve a form of commute. This activity is used when a user wants
 * to create a new leisure event, which will contain the fields for event name, date, time, location, and
 * optional notes, or modify an existing leisure event
 */
public class LeisureEventActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText leisureEventName;
    private EditText leisureEventDate;
    private EditText leisureEventTime;
    private EditText leisureEventLocation;
    private EditText leisureEventNote;
    private Calendar calendar;
    private Button leisureCancel;
    private Button leisureCreate;
    private ImageButton leisureDelete;

    /** This field represents the leisure event that corresponds to this activity in the case that
     * this activity is used to modify an existing Leisure Event
     */
    private LeisureEvent event;

    /** This field is the index in the array list of events of the event field
     */
    private int passedEventIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leisure_event);

        passedEventIndex = getIntent().getIntExtra(VacationsMainActivity.EVENT_KEY, -1);

        leisureEventName = (EditText)findViewById(R.id.leisure_event_name);
        leisureEventDate = (EditText)findViewById(R.id.leisure_event_date);
        leisureEventTime = (EditText)findViewById(R.id.leisure_event_time);
        leisureEventLocation = (EditText)findViewById(R.id.leisure_event_location);
        leisureEventNote = (EditText)findViewById(R.id.leisure_event_notes);
        leisureCancel = (Button)findViewById(R.id.leisure_cancel_btn);
        leisureCreate = (Button)findViewById(R.id.leisure_create_btn);
        leisureDelete = (ImageButton)findViewById(R.id.delete_leisure_event_btn);

        /* This if statement checks if the passed event index is not -1, meaning it is one that already
         exists, and if so, it will set the text fields in this activity to the fields of the event that
         is being modified
         */
        if(passedEventIndex != -1) {
            event = (LeisureEvent) VacationsMainActivity.vacation.events.get(passedEventIndex);
            leisureEventName.setText(event.getName());
            leisureEventDate.setText(event.getDate());
            leisureEventTime.setText(event.getTime());
            leisureEventLocation.setText(event.getLocation());
            leisureEventNote.setText(event.getNote());
        }

        // This is used to set an action listener for the buttons in this activity
        leisureCancel.setOnClickListener(this);
        leisureCreate.setOnClickListener(this);
        leisureDelete.setOnClickListener(this);

        dateChooser();
        timeChooser();
    }

    /**
     * This method, which has been overridden from the View.OnClickListener interface,
     * is used to register an Action Event and respond in a certain way.
     * @param v the view passed into the onClick method
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.leisure_cancel_btn:
                onBackPressed();
                break;
            case R.id.leisure_create_btn:
                /* When a user either creates a new leisure event or modifies an existing one, this
                case collects everything in the text fields and assigns them to the fields of the event
                 */
                String eventName = leisureEventName.getText().toString();
                String eventDate = leisureEventDate.getText().toString();
                String eventTime = leisureEventTime.getText().toString();
                String eventLocation = leisureEventLocation.getText().toString();
                String eventNotes = leisureEventNote.getText().toString();

                /*
                Before the user creates a new event, we need to check to make sure that none of the required
                fields are empty, and also to make sure that the date is within the range of the vacation, if these
                tests fail, we need to pop up an alert dialog to let the user know what went wrong, and let them change
                it before creating the event
                 */
                if(eventName.isEmpty() || eventDate.isEmpty() || eventTime.isEmpty() || eventLocation.isEmpty()) {
                    AlertDialog alertDialog = new AlertDialog.Builder(LeisureEventActivity.this).create();
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
                else if(dateOutsideRange(eventDate)) {
                    AlertDialog alertDialog = new AlertDialog.Builder(LeisureEventActivity.this).create();
                    alertDialog.setTitle("Error");
                    alertDialog.setMessage("Date must be within dates of vacation");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "CLOSE",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
                else {
                    /*
                    If both the tests pass, we check if it is an already existing event, or one being modified.
                    If it is an existing event, we use our mutator methods to change the event's fields, otherwise,
                    we create a new event with the given fields and add it to our vacation's events array list field
                     */
                    if(passedEventIndex != -1) {
                        event.setName(eventName);
                        event.setDate(eventDate);
                        event.setTime(eventTime);
                        event.setLocation(eventLocation);
                        event.setNote(eventNotes);
                        onBackPressed();
                    }
                    else {
                        Event event = new LeisureEvent(eventName, eventDate, eventTime, eventLocation, eventNotes);
                        VacationsMainActivity.vacation.events.add(event);
                        onBackPressed();
                    }
                }
                break;
            case R.id.delete_leisure_event_btn:
                /*
                If the user clicks the delete button to delete a leisure event, we pop an alert dialog to confirm
                with the user that it is okay to delete the event, and remove it from the events array list if it is.
                However, if this activity is started to create a new event, we don't set an on click listener for this
                delete button because they shouldn't be allowed to delete an event that doesn't exist.
                 */

                if(passedEventIndex == -1) {
                    break;
                }

                AlertDialog alertDialog = new AlertDialog.Builder(LeisureEventActivity.this).create();
                alertDialog.setTitle("Confirm");
                alertDialog.setMessage("Are you sure you want to delete this item?");
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        VacationsMainActivity.vacation.events.remove(VacationsMainActivity.vacation.events.indexOf(event));

                        dialog.dismiss();
                        onBackPressed();


                    }
                });
                alertDialog.show();

                break;
        }
    }

    /**
     * This helper method checks to make sure that the event is inside the range of the vacation's
     * start date and end date. If it is not, the user will have to change the event's date
     */
    private boolean dateOutsideRange(String date) {
        /*
        This method parses the passed in date and checks to see if it is either before the start
        date or after the end date of the vacation. A try catch is needed as a Parse Exception is a
        checked exception and must be handled for
         */
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");

            Date d = sdf.parse(date);
            Date startDate = sdf.parse(VacationsMainActivity.vacation.getStartDate());
            Date endDate = sdf.parse(VacationsMainActivity.vacation.getEndDate());

            return d.before(startDate) || d.after(endDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * This time chooser method creates a time picker dialog for the user to pick a time from a clock dialog
     * in order to select the leisure event time
     */
    private void timeChooser() {
        /*
        An action listener is set for the leisureEventTime field as this is the field we want to be registered
        to the time chooser. If the minute of the hour selected is between 0 and 9, inclusive, we need to add a
        zero to the text before the number. The text is set to military time
         */
        leisureEventTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(LeisureEventActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        if(selectedMinute < 10) {
                            leisureEventTime.setText(selectedHour + ":0" + selectedMinute);
                        }
                        else {
                            leisureEventTime.setText(selectedHour + ":" + selectedMinute);
                        }
                    }
                }, hour, minute, false);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
    }


    /**
     * This method is exactly like the dateChooser() in the vacation activity class, which pops up a date
     * picker dialog for the leisure event date text field.
     */
    private void dateChooser() {
        calendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDate();
            }

        };

        leisureEventDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(LeisureEventActivity.this, date, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    /**
     * This method sets the text in the leisure event date text field to the selected date in the
     * date picker dialog
     */
    private void updateDate() {
        String sdf = "MM/dd/yy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(sdf);

        leisureEventDate.setText(simpleDateFormat.format(calendar.getTime()));
    }
}
