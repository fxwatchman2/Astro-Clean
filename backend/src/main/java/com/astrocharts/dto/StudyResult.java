package com.astrocharts.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StudyResult {
    private List<Date> singleDates = new ArrayList<>();
    private List<DatePair> datePairs = new ArrayList<>();

    // Getters and Setters
    public List<Date> getSingleDates() {
        return singleDates;
    }

    public void setSingleDates(List<Date> singleDates) {
        this.singleDates = singleDates;
    }

    public List<DatePair> getDatePairs() {
        return datePairs;
    }

    public void setDatePairs(List<DatePair> datePairs) {
        this.datePairs = datePairs;
    }
}
