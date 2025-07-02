package com.astrocharts.csg;

import java.util.Objects;

/**
 * Study object for CSG. Supported displayType values (for drawing shapes):
 *   - vLine
 *   - circle
 *   - square
 *   - star
 *   - downArrow
 */
public class Study {
    private String type;
    // Longitude study fields
    private String planet1;
    private String planet2;
    private Double angle; // Use 'angle' for longitudinal distance studies
    private String frameOfReference; // Added field
    private String color;
    private String displayType;
    // Add other study-specific fields as needed

    public Study() {}

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public String getPlanet1() {
        return planet1;
    }
    public void setPlanet1(String planet1) {
        this.planet1 = planet1;
    }

    public String getPlanet2() {
        return planet2;
    }
    public void setPlanet2(String planet2) {
        this.planet2 = planet2;
    }

    public Double getAngle() {
        return angle;
    }
    public void setAngle(Double angle) {
        this.angle = angle;
    }

    public String getFrameOfReference() {
        return frameOfReference;
    }
    public void setFrameOfReference(String frameOfReference) {
        this.frameOfReference = frameOfReference;
    }

    public String getColor() {
        return color;
    }
    public void setColor(String color) {
        this.color = color;
    }

    public String getDisplayType() {
        return displayType;
    }
    public void setDisplayType(String displayType) {
        this.displayType = displayType;
    }

    // Unique tuple string for each study type
    public String getTuple() {
        String t = type != null ? type.toLowerCase() : "";
        String p1 = planet1 != null ? planet1.toLowerCase() : "";
        String p2 = planet2 != null ? planet2.toLowerCase() : "";
        String a = angle != null ? angle.toString() : "";
        String f = frameOfReference != null ? capitalizeFirst(frameOfReference) : "";
        String tuple = t + "-" + p1 + "-" + p2 + "-" + a + (f.isEmpty() ? "" : ("-" + f));
        System.out.println("[Study Tuple] " + tuple);
        return tuple;
    }

    private String capitalizeFirst(String s) {
        if (s == null || s.isEmpty()) return "";
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Study study = (Study) o;
        String tuple1 = getTuple();
        String tuple2 = study.getTuple();
        System.out.println("Comparing Study Tuples: this=" + tuple1 + " other=" + tuple2);
        return tuple1.equalsIgnoreCase(tuple2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTuple());
    }
}
