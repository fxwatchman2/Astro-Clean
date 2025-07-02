package com.astrocharts.dto;

import java.util.List;

public class IndicatorResponse {
    private String indicatorType;
    private String panel;
    private List<Double> values;
    private String color;
    private String lineStyle;

    public String getIndicatorType() { return indicatorType; }
    public void setIndicatorType(String indicatorType) { this.indicatorType = indicatorType; }
    public String getPanel() { return panel; }
    public void setPanel(String panel) { this.panel = panel; }
    public List<Double> getValues() { return values; }
    public void setValues(List<Double> values) { this.values = values; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    public String getLineStyle() { return lineStyle; }
    public void setLineStyle(String lineStyle) { this.lineStyle = lineStyle; }
}
