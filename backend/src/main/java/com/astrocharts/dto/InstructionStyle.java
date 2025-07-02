package com.astrocharts.dto;

/**
 * Defines the visual styling for a drawing instruction.
 * Fields can be null if a default style should be used or if the style
 * is not applicable to the command.
 */
public class InstructionStyle {

    private String color;
    private Integer thickness;
    private String lineStyle; // "solid", "dashed", "dotted"
    private String pattern;
    private Integer fontSize;
    private String fontWeight; // "normal", "bold"
    private String fontFamily;
    private String startSymbol; // For trendlines
    private String endSymbol;   // For trendlines
    private Boolean smooth;     // For drawLongitude
    private String type; // e.g., "vertical-line", "circle", "trend-line"

    // Getters and Setters
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    public Integer getThickness() { return thickness; }
    public void setThickness(Integer thickness) { this.thickness = thickness; }
    public String getLineStyle() { return lineStyle; }
    public void setLineStyle(String lineStyle) { this.lineStyle = lineStyle; }
    public String getPattern() { return pattern; }
    public void setPattern(String pattern) { this.pattern = pattern; }
    public Integer getFontSize() { return fontSize; }
    public void setFontSize(Integer fontSize) { this.fontSize = fontSize; }
    public String getFontWeight() { return fontWeight; }
    public void setFontWeight(String fontWeight) { this.fontWeight = fontWeight; }
    public String getFontFamily() { return fontFamily; }
    public void setFontFamily(String fontFamily) { this.fontFamily = fontFamily; }
    public String getStartSymbol() { return startSymbol; }
    public void setStartSymbol(String startSymbol) { this.startSymbol = startSymbol; }
    public String getEndSymbol() { return endSymbol; }
    public void setEndSymbol(String endSymbol) { this.endSymbol = endSymbol; }
    public Boolean getSmooth() { return smooth; }
    public void setSmooth(Boolean smooth) { this.smooth = smooth; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}
