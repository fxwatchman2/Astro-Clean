package com.astrocharts.dto;

/**
 * Contains metadata associated with a drawing instruction, used for logging,
 * tooltips, and tracking the origin of the decoration.
 */
public class InstructionMeta {

    private String studyId;
    private String tooltip;
    private String userId;
    private String studyGenDateTime;
    private String ticker;

    // New fields for aspect details
    private String planet1;
    private String planet2;
    private int distance;
    private String studyGroup;
    private String frameOfReference;

    // Getters and Setters
    public String getStudyId() { return studyId; }
    public void setStudyId(String studyId) { this.studyId = studyId; }
    public String getTooltip() { return tooltip; }
    public void setTooltip(String tooltip) { this.tooltip = tooltip; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getStudyGenDateTime() { return studyGenDateTime; }
    public void setStudyGenDateTime(String studyGenDateTime) { this.studyGenDateTime = studyGenDateTime; }
    public String getTicker() { return ticker; }
    public void setTicker(String ticker) { this.ticker = ticker; }

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

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public String getStudyGroup() {
        return studyGroup;
    }

    public void setStudyGroup(String studyGroup) {
        this.studyGroup = studyGroup;
    }

    public String getFrameOfReference() {
        return frameOfReference;
    }

    public void setFrameOfReference(String frameOfReference) {
        this.frameOfReference = frameOfReference;
    }
}
