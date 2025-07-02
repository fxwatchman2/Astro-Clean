package com.astrocharts.dto;

import com.astrocharts.util.RowRecord;
import java.util.List;

/**
 * The main container class for the API response. This object holds all the data
 * needed by the frontend to render a complete chart with price, volume, and all
 * study-driven decorations.
 */
public class ChartDataResponse {

    private List<RowRecord> ohlcvData;
    private List<DrawingInstruction> drawingInstructions;

    // Getters and Setters
    public List<RowRecord> getOhlcvData() {
        return ohlcvData;
    }

    public void setOhlcvData(List<RowRecord> ohlcvData) {
        this.ohlcvData = ohlcvData;
    }

    public List<DrawingInstruction> getDrawingInstructions() {
        return drawingInstructions;
    }

    public void setDrawingInstructions(List<DrawingInstruction> drawingInstructions) {
        this.drawingInstructions = drawingInstructions;
    }
}
