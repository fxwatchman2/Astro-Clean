package com.astrocharts.dto;

import java.util.List;

public class RunCSGRequest {
    private List<Object> studies;
    private String symbol;

    public List<Object> getStudies() {
        return studies;
    }

    public void setStudies(List<Object> studies) {
        this.studies = studies;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}
