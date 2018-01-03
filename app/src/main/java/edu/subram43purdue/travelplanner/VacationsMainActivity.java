package edu.subram43purdue.travelplanner;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;

/**
 * This Activity is the activity that contains a majority of the app's features, as this is the home
 * page for a specific vacation. This activity contains the vacation's name, location, and dates, as well
 * as buttons to delete a vacation, create a new event in the vacation, and a list view of all the events in
 * the vacation. In terms of complexity, this was the toughest Activity of this project
 */
public class VacationsMainActivity extends AppCompatActivity implements View.OnClickListener {
    // Similar to other activities, these fields represent the features of our layout file
    private TextView vacationName;
    private TextView location;
    private TextView dates;
    private ImageButton deleteButton;
    private Intent intent;
    private Button newTransportationEvent;
    private Button newLeisureEvent;

    /** This list view is similar to the one in the main activity but this one is a list of our events
    of a specific vacation
     */
    private ListView listView;

    /** This field represents the vacation that belongs to this activity
     */
    public static Vacation vacation;

    /** This is the index of the vacation field that is passed in from the Array List
     */
    private int index;

    /** Similar to the main activity, this event key is used to identify the event that was passed
     * from the list view of events. An event list adapter is needed for the same reason a vacation list
     * adapter was needed, to work as an Array Adapter to connect events in the array list to a list view
     */
    public static final String EVENT_KEY = "event_key";

    /** This field is the event list adapter, which is the array adapter used to keep the array list
     * of events in sync with the list view in this activity
     */
    private EventListAdapter eventListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacations_main);

        /* We need to read the list of vacations from external storage here to make sure that
        we have the most updated list of vacations, in case the user exits the app on this activity
         */
        MainActivity.vacations = FileSaver.readVacationList();

        /* Here, the index is assigned to the index of the vacation that was passed from the main
        activity, so that this activity will show a view customized for that specific vacation. Then
        we use that index to get the vacation that belongs to this activity
         */
        index = getIntent().getIntExtra(MainActivity.VACATION_KEY, -1);
        vacation = MainActivity.vacations.get(index);


        vacationName = (EditText) findViewById(R.id.vacations_main_name_text);
        location = (EditText) findViewById(R.id.vacations_main_location_text);
        dates = (EditText) findViewById(R.id.vacations_main_dates_text);
        deleteButton = (ImageButton) findViewById(R.id.delete_vacation_btn);
        newTransportationEvent = (Button)findViewById(R.id.new_transportation_event);
        newLeisureEvent = (Button)findViewById(R.id.new_leisure_event);
        listView = (ListView)findViewById(R.id.vacations_main_event_list);

        /* In the next few lines, we set the text field headers at the top of this activity to
        the correct vacation's name, location, and dates
         */
        String vacationLocationText = "Location: " + vacation.getLocation();
        String vacationDatesText = vacation.getStartDate() + " - " + vacation.getEndDate();

        vacationName.setText(vacation.getName());
        location.setText(vacationLocationText);
        dates.setText(vacationDatesText);

        // Here we have set action listeners for each of these features on our activity
        vacationName.setOnClickListener(this);
        location.setOnClickListener(this);
        dates.setOnClickListener(this);
        deleteButton.setOnClickListener(this);
        newTransportationEvent.setOnClickListener(this);
        newLeisureEvent.setOnClickListener(this);

        /* Here we create an event list adapter to set the list view of this activity to
        the events array list of our vacation's events field
         */
        eventListAdapter = new EventListAdapter(VacationsMainActivity.this, vacation.events);
        listView.setAdapter(eventListAdapter);

        /* We need an on item click action listener here for our events in our array list of events.
        If one of the items is clicked on, the user should be taken to the corresponding event's activity
        to edit that event. Since our event list can only contain events of two types, a transportation
        event or a leisure event, we need to get that specific event and figure out whether it is an
        instance of TransportationEvent or LeisureEvent, and create the corresponding event to open up
        a TransportationEventActivity or LeisureEventActivity
         */
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Event obj = (Event)vacation.events.get(position);

                if(obj instanceof TransportationEvent) {
                    intent = new Intent(VacationsMainActivity.this, TransportationEventActivity.class);
                    intent.putExtra(EVENT_KEY, position);
                }
                else if (obj instanceof LeisureEvent) {
                    intent = new Intent(VacationsMainActivity.this, LeisureEventActivity.class);
                    intent.putExtra(EVENT_KEY, position);
                }
                startActivity(intent);

            }
        });

        refreshEventList();
    }


    /**
     * This method, which has been overridden from the View.OnClickListener interface,
     * is used to register an Action Event and respond in a certain way.
     * @param v The view passed into the onClick method
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /* If the delete vacation button is pressed, the a dialog pops up to the user asking them
            if they really want to delete the vacation. If they click yes, it will remove the vacation
            from the list of vacations and take the user back to the Main Activity
             */
            case R.id.delete_vacation_btn:
                AlertDialog alertDialog = new AlertDialog.Builder(VacationsMainActivity.this).create();
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
                        MainActivity.vacations.remove(MainActivity.vacations.indexOf(vacation));
                        dialog.dismiss();
                        onBackPressed();


                    }
                });
                alertDialog.show();
                break;
            /* If either the vacation name, location, or dates text boxes are pressed, the user will be
            prompted to that vacation's vacation activity, and they will be allowed to make changes to those
            vacation's fields there
             */
            case R.id.vacations_main_name_text:
                intent = new Intent(VacationsMainActivity.this, VacationActivity.class);
                intent.putExtra(MainActivity.VACATION_KEY, MainActivity.vacations.indexOf(vacation));
                startActivity(intent);
                break;
            case R.id.vacations_main_location_text:
                intent = new Intent(VacationsMainActivity.this, VacationActivity.class);
                intent.putExtra(MainActivity.VACATION_KEY, MainActivity.vacations.indexOf(vacation));
                startActivity(intent);
                break;
            case R.id.vacations_main_dates_text:
                intent = new Intent(VacationsMainActivity.this, VacationActivity.class);
                intent.putExtra(MainActivity.VACATION_KEY, MainActivity.vacations.indexOf(vacation));
                startActivity(intent);
                break;
            /* Based on whether the user wants to create a new leisure event or a new transportation
            event, the user will be taken to that specific event's Activity page to create a new one
             */
            case R.id.new_transportation_event:
                intent = new Intent(VacationsMainActivity.this, TransportationEventActivity.class);
                startActivity(intent);
                break;
            case R.id.new_leisure_event:
                intent = new Intent(VacationsMainActivity.this, LeisureEventActivity.class);
                startActivity(intent);
                break;
        }

    }

    /**
     * This method refeshes the list of events in the list view every time this activity is started,
     * and sorts them in chronological order
     */
    public void refreshEventList(){
        /* The following lines use the VacationListAdapter to sort the vacations in chronological order,
        Using the sortByDates() method from the Vacation.java class
         */
        Comparator comparator = Event.sortByDates();
        Collections.sort(vacation.events, comparator);
        eventListAdapter.setData(vacation.events);
        eventListAdapter.notifyDataSetChanged();

    }

    /**
     * This method is used to make sure the activity is refreshed and updated every time it is started,
     * even if it is resumed from before by pressing the back button
     */
    @Override
    public void onResume() {
        super.onResume();
        vacationName.setText(vacation.getName());
        String vacationLocationText = "Location: " + vacation.getLocation();
        String vacationDatesText = vacation.getStartDate() + " - " + vacation.getEndDate();
        location.setText(vacationLocationText);
        dates.setText(vacationDatesText);
        refreshEventList();

    }

    /**
     * This method makes sure the array list of vacations is written to external storage before
     * the activity is paused, or stopped by closing the app
     */
    @Override
    protected void onPause() {
        super.onPause();
        FileSaver.writeVacationList(MainActivity.vacations);
    }
}
