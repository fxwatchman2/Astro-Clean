package com.astrocharts.studies;

import com.astrocharts.dto.StudyResult;

public class PlanetaryIngressStudy extends Study {
    private String planet;
    private String sign;


    @Override
    public StudyResult execute(String chartFirstDate, String chartLastDate) {
        System.out.println("Executing Planetary Ingress Study for " + planet + " into " + sign);
        // TODO: Implement the actual logic for the Planetary Ingress Study.
        return new StudyResult();
    }

    // Getters and Setters
    public String getPlanet() { return planet; }
    public void setPlanet(String planet) { this.planet = planet; }
    public String getSign() { return sign; }
    public void setSign(String sign) { this.sign = sign; }

}
