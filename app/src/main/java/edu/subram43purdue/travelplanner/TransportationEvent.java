package edu.subram43purdue.travelplanner;

/*
 * Created by subram43 on 12/21/17.
 */

/**
 * This class represents a Transportation Event of a vacation, which is one of the two subclasses of Event,
 * and contains six fields, three of which are inherited from the Event class (name, date, and time).
 * The three extra fields are event mode of travel, origin, and destination, which are traits specific to a
 * transportation event
 */
public class TransportationEvent extends Event {
    private String mode;
    private String origin;
    private String destination;

    /**
     * This is the constructor for the Transportation Event class, which takes in 6 parameters: name, date,
     * time, mode, origin, and destination
     * @param name Name of the event
     * @param date Date of the event
     * @param time Time the event takes place
     * @param mode The mode of transportation
     * @param origin Where the travel departs
     * @param destination Where the travel arrives
     */
    public TransportationEvent(String name, String date, String time, String mode, String origin, String destination) {
        super(name, date, time);
        this.mode = mode;
        this.origin = origin;
        this.destination = destination;
    }

    /**
     * This method is an accessor method that returns the transportation event's mode in a String
     * @return String
     */
    public String getMode() {
        return mode;
    }

    /**
     * This method is a mutator method that sets the event's mode field to the mode parameter passed in
     * @param mode The mode of transportation
     */
    public void setMode(String mode) {
        this.mode = mode;
    }

    /**
     * This method is an accessor method that returns the transportation event's origin in a String
     * @return String
     */
    public String getOrigin() {
        return origin;
    }

    /**
     * This method is a mutator method that sets the event's origin field to the origin parameter passed in
     * @param origin Where the travel departs
     */
    public void setOrigin(String origin) {
        this.origin = origin;
    }

    /**
     * This method is an accessor method that returns the transportation event's destination in a String
     * @return String
     */
    public String getDestination() {
        return destination;
    }

    /**
     * This method is a mutator method that sets the event's destination field to the destination parameter passed in
     * @param destination Where the travel arrives
     */
    public void setDestination(String destination) {
        this.destination = destination;
    }


}
