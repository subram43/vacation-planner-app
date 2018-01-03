package edu.subram43purdue.travelplanner;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/*
 * Created by subram43 on 12/29/17.
 */

/**
 * This class is used to adapt our Array List of events to our list view in the Vacations Main Activity class
 * to provide an accurate and updated version of our event list every time. For that reason, this class
 * is a subclass of Array Adapter.
 */
public class EventListAdapter extends ArrayAdapter {
    /**
     * This is the context in which our adapter will be used and it cannot be null
     */
    private Context context;
    /**
     * This is the list of events which we want to adapt to the list view
     */
    private List<Event> eventList;

    /**
     * This is the constructor for our EventListAdapter class. It takes in the context and list as
     * parameters and initializes both of them, as well as the id of the list template from the superclass
     * ArrayAdapter's constructor
     * @param context The context in which our adapter will be used, which cannot be null
     * @param eventList the list of events which we want to adapt to list view
     */
    public EventListAdapter(@NonNull Context context, List<Event> eventList) {
        super(context, R.layout.event_list_layout);
        this.eventList = eventList;
        this.context = context;
    }

    /**
     * This method uses the Array Adapter we have created to return a new view with each item in the list view
     * following the design of the template in event_list_layout.xml
     * @param position the position/index in the list view
     * @param convertView the view that is converted into the parent view, which would be the template
     *                    in the event_list_layout.xml file
     * @param parent the parent View, which would be the list view in the Vacations Main Activity
     * @return View
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        /*
         * Here we create a view for one item in the list view, and we set each item to the linear
         * layout template we created in event_list_layout and resize it to match the parent
         */
        View rowView = inflater.inflate(R.layout.event_list_layout, parent, false);
        /*
        This is where we find the specific event based on which position it is placed in the list view
         */
        Event eventItem = eventList.get(position);

        /*
        In the next three lines, we create text view variables and set them to the fields in the
        event_list_layout.xml file to hold our event's name, date, and time
         */
        TextView eventName = (TextView) rowView.findViewById(R.id.event_name);
        TextView eventDate = (TextView) rowView.findViewById(R.id.event_date);
        TextView eventTime = (TextView) rowView.findViewById(R.id.event_time);

        /*
        Here we set the text views in the event's list view item to its name, date, and time
        and return the view with those attributes
         */
        eventName.setText(eventItem.getName());
        eventDate.setText(eventItem.getDate());
        eventTime.setText(eventItem.getTime());

        return rowView;
    }


    /**
     * This method returns the number of events in the list view
     * @return int
     */
    @Override
    public int getCount(){
        return eventList !=null ? eventList.size() : 0;
    }

    /**
     * This mutator method sets the list passed in as the updated event list field
     * @param events The updated list of events
     */
    // This mutator method sets the list passed in as the event list field
    public void setData(List<Event> events) {
        this.eventList = events;
    }
}
