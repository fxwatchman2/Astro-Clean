package com.astrocharts.dto;

import java.util.List;

/**
 * A container for all possible parameters across all drawing commands.
 * For any given command, only the relevant fields will be populated.
 * All other fields will be null and should be ignored by the frontend renderer
 * for that command.
 */
public class InstructionParams {
    @com.fasterxml.jackson.annotation.JsonProperty("y")
    private Double y; // for custom shapes

    // Common
    private String dateTime;
    private String fromDateTime;
    private String toDateTime;

    // For drawHLine
    private Double yValue;
    private Integer laneNumber;

    // For drawTLine
    private Double fromYValue;
    private Double toYValue;

    // For drawLongitude
    private List<DataPoint> dataPoints;

    // For markArea
    private Integer areaToCover;

    // For drawText
    private String text;
    private String position;

    // For drawArc
    private Integer numOfBars;
    private Double arcHeight;
    private Boolean repeat;
    private String direction;

    // Getters and Setters
    public String getDateTime() { return dateTime; }
    public void setDateTime(String dateTime) { this.dateTime = dateTime; }
    public String getFromDateTime() { return fromDateTime; }
    public void setFromDateTime(String fromDateTime) { this.fromDateTime = fromDateTime; }
    public String getToDateTime() { return toDateTime; }
    public void setToDateTime(String toDateTime) { this.toDateTime = toDateTime; }
    public Double getY() { return y; }
    @com.fasterxml.jackson.annotation.JsonProperty("y")
    public void setY(Double y) { this.y = y; }
    public Double getyValue() { return yValue; }
    public void setyValue(Double yValue) { this.yValue = yValue; }
    public Integer getLaneNumber() { return laneNumber; }
    public void setLaneNumber(Integer laneNumber) { this.laneNumber = laneNumber; }
    public Double getFromYValue() { return fromYValue; }
    public void setFromYValue(Double fromYValue) { this.fromYValue = fromYValue; }
    public Double getToYValue() { return toYValue; }
    public void setToYValue(Double toYValue) { this.toYValue = toYValue; }
    public List<DataPoint> getDataPoints() { return dataPoints; }
    public void setDataPoints(List<DataPoint> dataPoints) { this.dataPoints = dataPoints; }
    public Integer getAreaToCover() { return areaToCover; }
    public void setAreaToCover(Integer areaToCover) { this.areaToCover = areaToCover; }
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }
    public Integer getNumOfBars() { return numOfBars; }
    public void setNumOfBars(Integer numOfBars) { this.numOfBars = numOfBars; }
    public Double getArcHeight() { return arcHeight; }
    public void setArcHeight(Double arcHeight) { this.arcHeight = arcHeight; }
    public Boolean getRepeat() { return repeat; }
    public void setRepeat(Boolean repeat) { this.repeat = repeat; }
    public String getDirection() { return direction; }
    public void setDirection(String direction) { this.direction = direction; }
}
