package com.kalwit.services.dto;

public class PlanetLongitudeEvent {
    private String date;
    private int planet;
    private double degrees;
    private String shapeType;
    private String color;
    private String thickness;

    public PlanetLongitudeEvent() {}

    public PlanetLongitudeEvent(String date, int planet, double degrees, String shapeType, String color, String thickness) {
        this.date = date;
        this.planet = planet;
        this.degrees = degrees;
        this.shapeType = shapeType;
        this.color = color;
        this.thickness = thickness;
    }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public int getPlanet() { return planet; }
    public void setPlanet(int planet) { this.planet = planet; }

    public double getDegrees() { return degrees; }
    public void setDegrees(double degrees) { this.degrees = degrees; }

    public String getShapeType() { return shapeType; }
    public void setShapeType(String shapeType) { this.shapeType = shapeType; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public String getThickness() { return thickness; }
    public void setThickness(String thickness) { this.thickness = thickness; }
}
