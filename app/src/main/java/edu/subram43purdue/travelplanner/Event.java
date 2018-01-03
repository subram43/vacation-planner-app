package edu.subram43purdue.travelplanner;

import java.io.Serializable;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

/*
 * Created by subram43 on 12/21/17.
 */

/**
 * This class represents an event of a vacation, which are contained inside an array list of events
 * for each vacation as one of the fields. For each vacation, there are two types of events, a transportation
 * and a leisure event, both subclasses of Event. Since we don't want to instantiate an Event object on its own, however,
 * we have made it an abstract class, containing three fields common to all events, a name, date, and event. This is where
 * I have utilized the characteristics of Polymorphism so that event can take on the form of a transportation or leisure
 * event
 */
public abstract class Event implements Serializable{
    private String name;
    private String date;
    private String time;

    /**
     * This is the constructor for an event object, which cannot be instantiated, but takes on three parameters
     * common to all types of events: name, date, and time
     * @param name Name of the event
     * @param date Date of the event
     * @param time Time of the event
     */
    public Event(String name, String date, String time) {
        this.name = name;
        this.date = date;
        this.time = time;
    }

    /**
     * This method is an accessor method that returns the event's name in a String
     * @return String name
     */
    public String getName() {
        return name;
    }

    /**
     * This method is a mutator method that sets the event's name field to the name parameter passed in
     * @param name Name of the event
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * This method is an accessor method that returns the event's date in a String
     * @return String date
     */
    public String getDate() {
        return date;
    }

    /**
     * This method is a mutator method that sets the event's date field to the date parameter passed in
     * @param date Date of the event
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * This method is an accessor method that returns the event's time in a String
     * @return String time
     */
    public String getTime() {
        return time;
    }

    /**
     * This method is a mutator method that sets the event's time field to the time parameter passed in
     * @param time Time of the event
     */
    public void setTime(String time) {
        this.time = time;
    }

    /**
     * This method compares all the events in the event list by dates and sorts them chronologically
     * @return Comparator<Event>
     */
    static Comparator<Event> sortByDates() {
        return new Comparator<Event>() {
            /**
             * This method compares two events' dates and returns -1 if the first one comes before the second
             * and 1 if vice versa
             * @param e1 First event to be compared
             * @param e2 Second event to be compared
             * @return int
             */
            @Override
            public int compare(Event e1, Event e2) {
                try {
                    /*
                    A try-catch block is needed when we parse the dates here to compare them as
                    a Parse Exception is a checked exception
                     */
                    ParsePosition position = new ParsePosition(0);
                    SimpleDateFormat df = new SimpleDateFormat("MM/dd/yy");

                    Date eventOne = df.parse(e1.getDate());
                    Date eventTwo = df.parse(e2.getDate());

                    if (eventOne.before(eventTwo)) {
                        return -1;
                    }
                    else if (eventOne.after(eventTwo)) {
                        return 1;
                    }
                    /*
                    If two events have the same date, they are then compared by time. Since our time
                    is set to military time, it is really easy to compare. We just split our time string
                    into a String array of length two and add up both elements into a string, then parse it,
                    and compare the time based on which number is bigger
                     */
                    else if(eventOne.equals(eventTwo)) {
                        String[] timeOne = e1.time.split(":");
                        String[] timeTwo = e2.time.split(":");

                        int t1 = Integer.parseInt(timeOne[0] + timeOne[1]);
                        int t2 = Integer.parseInt(timeTwo[0] + timeTwo[1]);

                        if(t1 < t2) {
                            return -1;
                        }
                        else if(t1 > t2) {
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
