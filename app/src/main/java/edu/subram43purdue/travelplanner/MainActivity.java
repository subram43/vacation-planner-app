package edu.subram43purdue.travelplanner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * This is our Main Activity, where the user can view all their vacations, and click on them to view
 * that specific vacation's main activity
 */
public class MainActivity extends AppCompatActivity {
    /** This is the vacation key, which is used when a specific vacation is clicked on, to track
     * that specific vacation with this identification key
     */
    public static final String VACATION_KEY = "vacation_key";
    /** This is the main ArrayList of vacations, which holds all the vacations, we read the list of vacations
     * from external storage, using the readVacationList() static method from the FileSaver class
     */
    public static ArrayList<Vacation> vacations = FileSaver.readVacationList();

    /** This is our field for the listView of all the vacations we created in the xml file
     */
    private ListView listView;

    /** Here we instantiate a new VacationListAdapter object which will connect the ArrayList of our
     *vacations to the list view, so we can see our vacations in the list view
     */
    private VacationListAdapter vacationListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Assigning our list view field to the list view we created in the activity_main.xml file
        listView = (ListView)findViewById(R.id.main_list_view);


        /* This creates a new VacationListAdapter object and connects the vacation list adapter to
        the vacations array list, then the second line sets our list view field to the vacation list
        adapter, connecting our vacations array list to the list view properly
        */
        vacationListAdapter = new VacationListAdapter(MainActivity.this, vacations);
        listView.setAdapter(vacationListAdapter);

        /* This puts an on item click listener on our list view, allowing the user to click a certain
        item in the array list, and take the user to the VacationsMainActivity for that designated vacation.
        In order to identify the correct vacation in the Vacations Main Activity, we need to send the
        vacation key and position with the intent, which is why we use the putExtra() method
         */
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this,VacationsMainActivity.class);
                intent.putExtra(VACATION_KEY,position);
                startActivity(intent);

            }
        });
        refreshVacationList();
    }


    /**
     * This method refreshes the current vacation list every time a new vacation is added or a vacation
     * is deleted, hence the list adapter having to set the data and notifying that the data set has changed.
     * We also want to sort the vacations in chronological order for the user, which is why we also call the
     * comparator we created in the Vacation class, to resort the vacations chronologically everytime a change
     * is made.
     */
    public void refreshVacationList(){
        // The following lines use the VacationListAdapter to sort the vacations in chronological order,
        // Using the sortByDates() method from the Vacation.java class
        Comparator comparator = Vacation.sortByDates();
        Collections.sort(vacations, comparator);
        vacationListAdapter.setData(vacations);
        vacationListAdapter.notifyDataSetChanged();

    }


    /** This method is used whenever the main activity is resumed by clicking back from something else.
    This ensures that if a user deletes a vacation, and onBackPressed brings them back to the Main Activity,
    the vacation won't show up anymore. This is a very important method to make sure that the Main Activity
    constantly gets refreshed so that the app won't crash
     */
    @Override
    public void onResume()
    {
        super.onResume();
        refreshVacationList();

    }


    /** This method writes the user's vacations to external storage if the user closes the app from this activity
     */
    @Override
    protected void onPause() {
        super.onPause();
        FileSaver.writeVacationList(vacations);

    }
}
