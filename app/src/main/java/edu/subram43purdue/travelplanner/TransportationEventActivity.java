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


/** This activity is very similar to the LeisureEventActivity class, but this is to create or modify
 * a transportation activity, which is an activity that does require some type of commute from place
 * to place
 */
public class TransportationEventActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText transEventName;
    private EditText transEventDate;
    private EditText transEventTime;
    private EditText transEventMode;
    private EditText transEventOrigin;
    private EditText transEventDestination;
    private Calendar calendar;
    private Button transCancel;
    private Button transCreate;
    private ImageButton transDelete;

    /** This field represents the transportation event that corresponds to this activity in the case that
     * this activity is used to modify an existing Transportation Event
     */
    private TransportationEvent event;

    /** This field is the index in the array list of events of the event field
     */
    private int passedEventIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transportation_event);

        passedEventIndex = getIntent().getIntExtra(VacationsMainActivity.EVENT_KEY, -1);

        transEventName = (EditText)findViewById(R.id.trans_event_name);
        transEventDate = (EditText)findViewById(R.id.trans_event_date);
        transEventTime = (EditText)findViewById(R.id.trans_event_time);
        transEventMode = (EditText)findViewById(R.id.trans_event_mode);
        transEventOrigin = (EditText)findViewById(R.id.trans_event_origin);
        transEventDestination = (EditText)findViewById(R.id.trans_event_destination);
        transCancel = (Button)findViewById(R.id.trans_cancel_btn);
        transCreate = (Button)findViewById(R.id.trans_create_btn);
        transDelete = (ImageButton)findViewById(R.id.delete_trans_event_btn);

        /* This if statement checks if the passed event index is not -1, meaning it is one that already
         exists, and if so, it will set the text fields in this activity to the fields of the event that
         is being modified
         */
        if(passedEventIndex != -1) {
            event = (TransportationEvent) VacationsMainActivity.vacation.events.get(passedEventIndex);
            transEventName.setText(event.getName());
            transEventDate.setText(event.getDate());
            transEventTime.setText(event.getTime());
            transEventMode.setText(event.getMode());
            transEventOrigin.setText(event.getOrigin());
            transEventDestination.setText(event.getDestination());
        }

        // This is used to set an action listener for the buttons in this activity
        transCancel.setOnClickListener(this);
        transCreate.setOnClickListener(this);
        transDelete.setOnClickListener(this);

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
            case R.id.trans_cancel_btn:
                onBackPressed();
                break;
            case R.id.trans_create_btn:
                /* When a user either creates a new transportation event or modifies an existing one, this
                case collects everything in the text fields and assigns them to the fields of the event
                 */
                String eventName = transEventName.getText().toString();
                String eventDate = transEventDate.getText().toString();
                String eventTime = transEventTime.getText().toString();
                String eventMode = transEventMode.getText().toString();
                String eventOrigin = transEventOrigin.getText().toString();
                String eventDestination = transEventDestination.getText().toString();

                /*
                Before the user creates a new event, we need to check to make sure that none of the required
                fields are empty, and also to make sure that the date is within the range of the vacation, if these
                tests fail, we need to pop up an alert dialog to let the user know what went wrong, and let them change
                it before creating the event
                 */
                if(eventName.isEmpty() || eventDate.isEmpty() || eventTime.isEmpty() ||
                        eventMode.isEmpty() || eventOrigin.isEmpty() || eventDestination.isEmpty()) {
                    AlertDialog alertDialog = new AlertDialog.Builder(TransportationEventActivity.this).create();
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
                    AlertDialog alertDialog = new AlertDialog.Builder(TransportationEventActivity.this).create();
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
                        event.setMode(eventMode);
                        event.setOrigin(eventOrigin);
                        event.setDestination(eventDestination);
                        onBackPressed();
                    }
                    else {
                        Event event = new TransportationEvent(eventName, eventDate, eventTime, eventMode, eventOrigin, eventDestination);
                        VacationsMainActivity.vacation.events.add(event);
                        onBackPressed();
                    }

                }
                break;
            case R.id.delete_trans_event_btn:
                /*
                If the user clicks the delete button to delete a transportation event, we pop an alert dialog to confirm
                with the user that it is okay to delete the event, and remove it from the events array list if it is
                 */
                AlertDialog alertDialog = new AlertDialog.Builder(TransportationEventActivity.this).create();
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
     * in order to select the transportation event time
     */
    private void timeChooser() {
        /*
        An action listener is set for the transEventTime field as this is the field we want to be registered
        to the time chooser. If the minute of the hour selected is between 0 and 9, inclusive, we need to add a
        zero to the text before the number. The text is set to military time
         */
        transEventTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(TransportationEventActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        if(selectedMinute < 10) {
                            transEventTime.setText(selectedHour + ":0" + selectedMinute);
                        }
                        else {
                            transEventTime.setText(selectedHour + ":" + selectedMinute);
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
     * picker dialog for the transportation event date text field.
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

        transEventDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(TransportationEventActivity.this, date, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    /**
     * This method sets the text in the transportation event date text field to the selected date in the
     * date picker dialog
     */
    private void updateDate() {
        String sdf = "MM/dd/yy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(sdf);

        transEventDate.setText(simpleDateFormat.format(calendar.getTime()));
    }

}
