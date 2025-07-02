package com.astrocharts.dto;

/**
 * Represents a single, self-contained instruction for the frontend to draw a
 * decoration on the chart.
 */
public class DrawingInstruction {

    private String command;
    private String targetPanel;
    private InstructionParams params;
    private InstructionStyle style;
    private InstructionMeta meta;

    // Getters and Setters
    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getTargetPanel() {
        return targetPanel;
    }

    public void setTargetPanel(String targetPanel) {
        this.targetPanel = targetPanel;
    }

    public InstructionParams getParams() {
        return params;
    }

    public void setParams(InstructionParams params) {
        this.params = params;
    }

    public InstructionStyle getStyle() {
        return style;
    }

    public void setStyle(InstructionStyle style) {
        this.style = style;
    }

    public InstructionMeta getMeta() {
        return meta;
    }

    public void setMeta(InstructionMeta meta) {
        this.meta = meta;
    }
}
