package com.astrocharts;

public class PlanetLongitudeEvent {
    // Define fields as needed, e.g.:
    private String date;
    private int planet;
    private double degrees;
    private String shapeType;
    private String color;
    private String thickness;
    private String planetName; // NEW: planet name string

    // Getters and setters
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

    public String getPlanetName() { return planetName; }
    public void setPlanetName(String planetName) { this.planetName = planetName; }

    @Override
    public String toString() {
        return "PlanetLongitudeEvent{" +
                "date='" + date + '\'' +
                ", planet=" + planet +
                ", planetName='" + planetName + '\'' +
                ", degrees=" + degrees +
                ", shapeType='" + shapeType + '\'' +
                ", color='" + color + '\'' +
                ", thickness='" + thickness + '\'' +
                '}';
    }
}
