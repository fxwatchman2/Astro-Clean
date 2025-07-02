package com.astrocharts.dto;

import java.util.List;

public class PlanetaryLinesRequest {
    private String planet;
    private String color;
    private String type;
    private String thickness;
    private String factor;
    private List<String> dates;
    private double lowest;
    private double highest;

    // Getters and setters
    public String getPlanet() { return planet; }
    public void setPlanet(String planet) { this.planet = planet; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getThickness() { return thickness; }
    public void setThickness(String thickness) { this.thickness = thickness; }
    public String getFactor() { return factor; }
    public void setFactor(String factor) { this.factor = factor; }
    public List<String> getDates() { return dates; }
    public void setDates(List<String> dates) { this.dates = dates; }
    public double getLowest() { return lowest; }
    public void setLowest(double lowest) { this.lowest = lowest; }
    public double getHighest() { return highest; }
    public void setHighest(double highest) { this.highest = highest; }
}
