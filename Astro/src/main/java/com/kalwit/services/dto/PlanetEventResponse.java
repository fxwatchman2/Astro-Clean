package com.kalwit.services.dto;

import java.util.List;

public class PlanetEventResponse {

    private List<String> dates;
    private String shapeType;
    private String color;
    private int thickness;

    public PlanetEventResponse(List<String> dates, String shapeType, String color, int thickness) {
        this.dates = dates;
        this.shapeType = shapeType;
        this.color = color;
        this.thickness = thickness;
    }

    // Getters and setters
    public List<String> getDates() {
        return dates;
    }

    public void setDates(List<String> dates) {
        this.dates = dates;
    }

    public String getShapeType() {
        return shapeType;
    }

    public void setShapeType(String shapeType) {
        this.shapeType = shapeType;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getThickness() {
        return thickness;
    }

    public void setThickness(int thickness) {
        this.thickness = thickness;
    }
}
