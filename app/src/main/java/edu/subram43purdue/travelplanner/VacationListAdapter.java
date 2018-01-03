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
 * Created by subram43 on 12/23/17.
 */

/**
 * This class is used to adapt our Array List of vacations to our list view in the Main Activity class
 * to provide an accurate and updated version of our vacation list every time. For that reason, this class
 * is a subclass of Array Adapter.
 */
public class VacationListAdapter extends ArrayAdapter {
    /**
     * This is the context in which our adapter will be used and it cannot be null
     */
    private Context context;
    /**
     * This is the list of vacations which we want to adapt to the list view
     */
    private List<Vacation> vacationList;

    /**
     * This is the constructor for our VacationListAdapter class. It takes in the context and list as
     * parameters and initializes both of them, as well as the id of the list template from the superclass
     * ArrayAdapter's constructor
     * @param context The context in which our adapter will be used, which cannot be null
     * @param vacationList the list of vacations which we want to adapt to list view
     */
    public VacationListAdapter(@NonNull Context context, List<Vacation> vacationList) {
        super(context, R.layout.vacation_list_layout);
        this.vacationList = vacationList;
        this.context = context;
    }

    /**
     * This method uses the Array Adapter we have created to return a new view with each item in the list view
     * following the design of the template in vacation_list_layout.xml
     * @param position the position/index in the list view
     * @param convertView the view that is converted into the parent view, which would be the template
     *                    in the vacation_list_layout.xml file
     * @param parent the parent View, which would be the list view in the Main Activity
     * @return View
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        /*
         * Here we create a view for one item in the list view, and we set each item to the linear
         * layout template we created in vacation_list_layout and resize it to match the parent
         */
        View rowView = inflater.inflate(R.layout.vacation_list_layout, parent, false);
        /*
        This is where we find the specific vacation based on which position it is placed in the list view
         */
        Vacation vacationItem = vacationList.get(position);

        /*
        In the next three lines, we create text view variables and set them to the fields in the
        vacation_list_layout.xml file to hold our vacation's name, start date, and end date
         */
        TextView vacation = (TextView) rowView.findViewById(R.id.item_vacationName);
        TextView startDate = (TextView) rowView.findViewById(R.id.item_startdate);
        TextView endDate = (TextView) rowView.findViewById(R.id.item_enddate);

        /*
        Here we set the text views in the vacation's list view item to its name, start date, and end date
        and return the view with those attributes
         */
        vacation.setText(vacationItem.getName());
        startDate.setText(vacationItem.getStartDate());
        endDate.setText(vacationItem.getEndDate());

        return rowView;
    }

    /**
     * This method returns the number of vacations in the list view
     * @return int
     */
    @Override
    public int getCount(){
        return vacationList !=null ? vacationList.size() : 0;
    }

    /**
     * This mutator method sets the list passed in as the updated vacation list field
     * @param vacationItems the updated list of vacations
     */
    public void setData(List<Vacation> vacationItems) {
        this.vacationList = vacationItems;
    }
}
