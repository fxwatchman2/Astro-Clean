package com.astrocharts.dto;

import java.util.Map;

public class IndicatorRequest {
    public IndicatorRequest() {} // Explicit no-arg constructor
    private String symbol;
    private String fromDate;
    private String toDate;
    private IndicatorSpec indicator;

    public static class IndicatorSpec {
        public IndicatorSpec() {} // Explicit no-arg constructor
        private String type;
        private Map<String, Object> params;
        private String panel;

        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public Map<String, Object> getParams() { return params; }
        public void setParams(Map<String, Object> params) { this.params = params; }
        public String getPanel() { return panel; }
        public void setPanel(String panel) { this.panel = panel; }
    }

    public String getSymbol() { return symbol; }
    public void setSymbol(String symbol) { this.symbol = symbol; }
    public String getFromDate() { return fromDate; }
    public void setFromDate(String fromDate) { this.fromDate = fromDate; }
    public String getToDate() { return toDate; }
    public void setToDate(String toDate) { this.toDate = toDate; }
    public IndicatorSpec getIndicator() { return indicator; }
    public void setIndicator(IndicatorSpec indicator) { this.indicator = indicator; }
}
