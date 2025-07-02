package com.astrocharts.studies;

import java.util.ArrayList;
import java.util.List;

public class StudyGroup {
    private String name;
    private List<Study> studies = new ArrayList<>();
    private String symbol;

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Study> getStudies() {
        return studies;
    }

    public void setStudies(List<Study> studies) {
        this.studies = studies;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}
