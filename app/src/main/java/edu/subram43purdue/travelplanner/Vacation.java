package edu.subram43purdue.travelplanner;

import java.io.Serializable;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

/*
 * Created by subram43 on 12/23/17.
 */


/**
 * This class represents a vacation, which has 5 different fields/attributes: a name, location, start date,
 * end date, and an array list of events. This class implements the Serializable interface, so that it can be
 * written to external storage using java's object output stream capabilities
 */
public class Vacation implements Serializable {
    private String name;
    private String location;
    private String startDate;
    private String endDate;
    public ArrayList events;

    /**
     * This is the constructor for the vacation class and it requires 5 parameters, which are all the same
     * as the five fields of this class
     * @param name Name of the vacation
     * @param location The main location of the vacation
     * @param startDate Start Date of the vacation
     * @param endDate End date of the vacation
     * @param events the list of events in the vacation
     */
    public Vacation(String name, String location, String startDate, String endDate, ArrayList<Event> events) {
        this.name = name;
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;
        this.events = events;
    }

    /**
     * This method is an accessor method that returns the vacation's name in a String
     * @return String name
     */
    public String getName() {
        return this.name;
    }

    /**
     * This method is a mutator method that sets the vacation's name field to the name parameter passed in
     * @param name Name of the vacation
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * This method is an accessor method that returns the vacation's location in a String
     * @return String location
     */
    public String getLocation() {
        return this.location;
    }

    /**
     * This method is a mutator method that sets the vacation's location field to the name parameter passed in
     * @param location Location of the vacation
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * This method is an accessor method that returns the vacation's start date in a String
     * @return String startDate
     */
    public String getStartDate() {
        return this.startDate;
    }

    /**
     * This method is a mutator method that sets the vacation's start date field to the startDate parameter passed in
     * @param startDate Start Date of the vacation
     */
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    /**
     * This method is an accessor method that returns the vacation's end date in a String
     * @return String endDate
     */
    public String getEndDate() {
        return this.endDate;
    }

    /**
     * This method is a mutator method that sets the vacation's end date field to the end date parameter passed in
     * @param endDate End Date of the vacation
     */
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    /**
     * This method is an accessor method that returns the vacation's events Array List
     * @return ArrayList events
     */
    public ArrayList getEvents() {
        return events;
    }

    /**
     * This method is a mutator method that sets the vacation's events field to the events parameter passed in
     * @param events List of events of a vacation
     */
    public void setEvents(ArrayList events) {
        this.events = events;
    }

    /**
     * This method compares all the vactions in the vacation list by dates and sorts them chronologically
     * @return Comparator<Vacation>
     */
    static Comparator<Vacation> sortByDates() {
        return new Comparator<Vacation>() {
            /**
             * This method compares two vacations' dates and returns -1 if the first one comes before the second
             * and 1 if vice versa
             * @param v1 The first vacation to be compared
             * @param v2 The second vacation to be compared
             * @return int
             */
            @Override
            public int compare(Vacation v1, Vacation v2) {
                try {
                    /*
                    A try-catch block is needed when we parse the dates here to compare them as
                    a Parse Exception is a checked exception
                     */
                    ParsePosition position = new ParsePosition(0);
                    SimpleDateFormat df = new SimpleDateFormat("MM/dd/yy");


                    Date vacationOneStartDate = df.parse(v1.startDate);
                    Date vacationTwoStartDate = df.parse(v2.startDate);

                    Date vacationOneEndDate = df.parse(v1.endDate);
                    Date vacationTwoEndDate = df.parse(v2.endDate);


                    if (vacationOneStartDate.before(vacationTwoStartDate)) {
                        return -1;
                    }
                    else if (vacationOneStartDate.after(vacationTwoStartDate)) {
                        return 1;
                    }
                    /*
                    If two vacations have the same start date, it checks the end dates of each one
                    and returns the value of the one which has the earlier end date
                     */
                    else if(vacationOneStartDate.equals(vacationTwoStartDate)) {
                        if(vacationOneEndDate.before(vacationTwoEndDate)) {
                            return -1;
                        }
                        else if(vacationOneEndDate.after(vacationTwoEndDate)) {
                            return 1;
                        }
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return 0;
            }
        };
    }


}
