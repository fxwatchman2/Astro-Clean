package com.astrocharts;

public class PlanetLongitudeDatesRequest {
    private int planet;
    private double degrees;
    private String oldestBarDate;
    private String newestBarDate;
    private String shapeType;
    private String color;
    private String thickness;
    private String planetName; // Add this field

    // Getters and setters
    public int getPlanet() { return planet; }
    public void setPlanet(int planet) { this.planet = planet; }

    public double getDegrees() { return degrees; }
    public void setDegrees(double degrees) { this.degrees = degrees; }

    public String getOldestBarDate() { return oldestBarDate; }
    public void setOldestBarDate(String oldestBarDate) { this.oldestBarDate = oldestBarDate; }

    public String getNewestBarDate() { return newestBarDate; }
    public void setNewestBarDate(String newestBarDate) { this.newestBarDate = newestBarDate; }

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
        return "PlanetLongitudeDatesRequest{" +
                "planet=" + planet +
                ", degrees=" + degrees +
                ", oldestBarDate='" + oldestBarDate + '\'' +
                ", newestBarDate='" + newestBarDate + '\'' +
                ", shapeType='" + shapeType + '\'' +
                ", color='" + color + '\'' +
                ", thickness='" + thickness + '\'' +
                ", planetName='" + planetName + '\'' +
                '}';
    }
}
