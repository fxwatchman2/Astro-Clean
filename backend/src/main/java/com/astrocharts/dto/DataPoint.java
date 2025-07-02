package com.astrocharts.dto;

/**
 * Helper class for drawLongitude data.
 */
public class DataPoint {
    private String dateTime;
    private double value;

    // Getters and Setters
    public String getDateTime() { return dateTime; }
    public void setDateTime(String dateTime) { this.dateTime = dateTime; }
    public double getValue() { return value; }
    public void setValue(double value) { this.value = value; }
}
