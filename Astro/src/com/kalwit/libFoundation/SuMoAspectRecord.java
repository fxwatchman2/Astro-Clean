package com.kalwit.libFoundation;


public class SuMoAspectRecord {
	public String dateTime;
    public String date;
    public int number;
    public String status;

    public SuMoAspectRecord(String dateTime, String date, int number, String status) {
        this.dateTime = dateTime;
        this.date = date;
        this.number = number;
        if (status!= null) this.status = status;
    }

    // Getters and Setters

    @Override
    public String toString() {
        return "Record{" +
                "dateTime='" + dateTime + '\'' +
                ", date='" + date + '\'' +
                ", number=" + number +
                ", status='" + status + '\'' +
                '}';
    }
}
