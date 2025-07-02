package com.astrocharts.model;

import java.util.Map;

public class Study {
    private String type; // e.g., "planet-ingress-sign", "planet-in-sign", "planet-planet-angle"
    private Map<String, Object> parameters; // Flexible for all study types

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public Map<String, Object> getParameters() { return parameters; }
    public void setParameters(Map<String, Object> parameters) { this.parameters = parameters; }
}
