package edu.subram43purdue.travelplanner;

/*
 * Created by subram43 on 12/21/17.
 */

/**
 * This class represents a Leisure Event of a vacation, which is one of the two subclasses of Event,
 * and contains five fields, three of which are inherited from the Event class (name, date, and time).
 * The two extra fields are event location and event note, which are traits specific to a leisure event
 */
public class LeisureEvent extends Event {
    private String location;
    private String note;

    /**
     * This is the constructor for the Leisure Event class, which takes in 5 parameters: name, date,
     * time, location, and a note
     * @param name Name of the Leisure Event
     * @param date Date of the Leisure Event
     * @param time Time of the Leisure Event
     * @param location Location the leisure event is taking place
     * @param note Optional note for the leisure event
     */
    public LeisureEvent(String name, String date, String time, String location, String note) {
        /*
        This first line uses constructor chaining with the super keyword to call the name, date, and
        time fields from the superclass Event's constructor
         */
        super(name, date, time);
        this.location = location;
        this.note = note;
    }

    /**
     * This method is an accessor method that returns the leisure event's location in a String
     * @return String location
     */
    public String getLocation() {
        return location;
    }

    /**
     * This method is a mutator method that sets the event's location field to the location parameter passed in
     * @param location Location of the leisure event
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * This method is an accessor method that returns the leisure event's note in a String
     * @return String note
     */
    public String getNote() {
        return note;
    }

    /**
     * This method is a mutator method that sets the event's note field to the note parameter passed in
     * @param note Optional note for the Leisure Event
     */
    public void setNote(String note) {
        this.note = note;
    }

}
