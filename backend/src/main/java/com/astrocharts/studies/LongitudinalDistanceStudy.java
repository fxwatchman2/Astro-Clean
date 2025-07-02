package com.astrocharts.studies;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kalwit.services.AstroService;
import swisseph.SweConst;


import com.astrocharts.dto.StudyResult;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LongitudinalDistanceStudy extends Study {

    private static final Map<String, Integer> PLANET_TO_SWE_CONST = new HashMap<>();
    static {
        PLANET_TO_SWE_CONST.put("Sun", SweConst.SE_SUN);
        PLANET_TO_SWE_CONST.put("Moon", SweConst.SE_MOON);
        PLANET_TO_SWE_CONST.put("Mercury", SweConst.SE_MERCURY);
        PLANET_TO_SWE_CONST.put("Venus", SweConst.SE_VENUS);
        PLANET_TO_SWE_CONST.put("Mars", SweConst.SE_MARS);
        PLANET_TO_SWE_CONST.put("Jupiter", SweConst.SE_JUPITER);
        PLANET_TO_SWE_CONST.put("Saturn", SweConst.SE_SATURN);
        PLANET_TO_SWE_CONST.put("Uranus", SweConst.SE_URANUS);
        PLANET_TO_SWE_CONST.put("Neptune", SweConst.SE_NEPTUNE);
        PLANET_TO_SWE_CONST.put("Pluto", SweConst.SE_PLUTO);
        PLANET_TO_SWE_CONST.put("Rahu", SweConst.SE_MEAN_NODE);
        PLANET_TO_SWE_CONST.put("Vesta", SweConst.SE_VESTA);
        PLANET_TO_SWE_CONST.put("Chiron", SweConst.SE_CHIRON);
    }

    /**
     * Utility method to map a planet name (e.g., "Mars") to its integer code as per Swiss Ephemeris constants.
     * This will be needed in many places for backend computations.
     * @param planetName Name of the planet (e.g., "Mars")
     * @return Integer code for the planet, or null if not found.
     */
    public static Integer getPlanetNumber(String planetName) {
        Integer id = PLANET_TO_SWE_CONST.get(planetName);
        if (id == null) {
            System.err.println("Warning: Unmapped planet name: " + planetName);
        }
        return id;
    }

    // --- Fictitious call for subject matter computation ---
    // TODO: Implement this call using the real library.


    // Example usage (replace with real call):
    // double diff = getLongitudeDifference(int planetNum1, int planetNum2, double degrees); // Uncomment and use when implementation is ready
    // Where planetNum1 and planetNum2 are obtained from getPlanetNumber(...), and degrees is the user input.
    // This placeholder is for reference. Update with actual implementation.
    //
    /**
     * Computes the longitude difference between two planets for the given aspect degree.
     * TODO: Implement this using the real computation library.
     * @param planetNum1 Integer code for planet 1
     * @param planetNum2 Integer code for planet 2
     * @param degrees Aspect degree (user input)
     * @return The computed longitude difference (currently a stub)
     */
    /**
     * Computes the longitude difference between two planets for the given aspect degree and date range.
     * TODO: Implement this using the real computation library.
     * @param planetNum1 Integer code for planet 1
     * @param planetNum2 Integer code for planet 2
     * @param degrees Aspect degree (user input)
     * @param fromDate Start date for the computation (inclusive)
     * @param toDate End date for the computation (inclusive)
     * @return The computed longitude difference (currently a stub)
     */
    /**
     * Computes the longitude difference between two planets for the given aspect degree and date range.
     * TODO: Implement this using the real computation library.
     * @param planetNum1 Integer code for planet 1
     * @param planetNum2 Integer code for planet 2
     * @param degrees Aspect degree (user input)
     * @param fromDate Start date for the computation (inclusive), must be in yyyy-MM-dd format
     * @param toDate End date for the computation (inclusive), must be in yyyy-MM-dd format
     * @return The computed longitude difference (currently a stub)
     * @throws IllegalArgumentException if fromDate or toDate is not in yyyy-MM-dd format
     */
    /**
     * Computes the longitude difference between two planets for the given aspect degree and date range.
     * Ensures fromDate and toDate are formatted as yyyy-MM-dd. Attempts to normalize common formats.
     * TODO: Implement this using the real computation library.
     * @param planetNum1 Integer code for planet 1
     * @param planetNum2 Integer code for planet 2
     * @param degrees Aspect degree (user input)
     * @param fromDate Start date for the computation (inclusive), any common date string (will be formatted to yyyy-MM-dd)
     * @param toDate End date for the computation (inclusive), any common date string (will be formatted to yyyy-MM-dd)
     * @return The computed longitude difference (currently a stub)
     * @throws IllegalArgumentException if fromDate or toDate cannot be parsed as a date
     */
    public double getLongitudeDifference(int planetNum1, int planetNum2, double degrees, String fromDate, String toDate) {
        java.time.format.DateTimeFormatter outputFormatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd");
        java.time.LocalDate from;
        java.time.LocalDate to;
        try {
            from = java.time.LocalDate.parse(fromDate, outputFormatter);
        } catch (java.time.format.DateTimeParseException e) {
            // Try parsing other common formats
            try {
                from = java.time.LocalDate.parse(fromDate);
            } catch (Exception ex) {
                throw new IllegalArgumentException("fromDate could not be parsed as a date", ex);
            }
        }
        try {
            to = java.time.LocalDate.parse(toDate, outputFormatter);
        } catch (java.time.format.DateTimeParseException e) {
            // Try parsing other common formats
            try {
                to = java.time.LocalDate.parse(toDate);
            } catch (Exception ex) {
                throw new IllegalArgumentException("toDate could not be parsed as a date", ex);
            }
        }
        String formattedFromDate = from.format(outputFormatter);
        String formattedToDate = to.format(outputFormatter);
        // Now formattedFromDate and formattedToDate are guaranteed to be yyyy-MM-dd
        // TODO: Call to real computation library goes here, using formattedFromDate and formattedToDate
        return 0.0;
    }


    private String planet1;
    private String planet2;
    private int distance;

    @Override
    public StudyResult execute(String chartFirstDate, String chartLastDate) {
        // Set tupleText for this study instance
        String t1 = getPlanet1();
        String t2 = getPlanet2();
        int d = getDistance();
        String t3 = (d != 0) ? Integer.toString(d) : "?";
        setTupleText((t1 != null && t2 != null && d != 0) ? (t1 + "-" + t2 + " " + t3 + "°") : "Aspect Study");
        System.out.println("Executing Longitudinal Distance Study for " + getPlanet1() + " and " + getPlanet2() +
                           " with distance " + getDistance() + " over chart range: " + chartFirstDate + " to " + chartLastDate);
        StudyResult result = new StudyResult();

        AstroService astroServices = new AstroService();
        Integer intPlanet1 = getPlanetNumber(getPlanet1());
        Integer intPlanet2 = getPlanetNumber(getPlanet2());

        if (intPlanet1 == null || intPlanet2 == null || chartFirstDate == null || chartLastDate == null) {
            System.err.println("Could not execute study due to missing planet mapping or date range.");
            return result; // Return empty result
        }

        // Call the real computation method
        System.out.println("[LongitudinalDistanceStudy] About to call getDegreeSeparation with: " +
            "planetNum1=" + intPlanet1 + ", planetNum2=" + intPlanet2 +
            ", chartFirstDate=" + chartFirstDate + ", chartLastDate=" + chartLastDate +
            ", distance=" + getDistance());
        List<String> dates;
        try {
            dates = astroServices.getDegreeSeparation(
                intPlanet1,
                intPlanet2,
                chartFirstDate,
                chartLastDate,
                getDistance()
            );
            System.out.println("[LongitudinalDistanceStudy] getDegreeSeparation called with parameters: " +
                "planetNum1=" + intPlanet1 + ", planetNum2=" + intPlanet2 +
                ", chartFirstDate=" + chartFirstDate + ", chartLastDate=" + chartLastDate +
                ", distance=" + getDistance());
            java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd");
            for (String dateStr : dates) {
                java.time.LocalDate localDate = java.time.LocalDate.parse(dateStr, formatter);
                Date date = Date.from(localDate.atStartOfDay(java.time.ZoneId.systemDefault()).toInstant());
                result.getSingleDates().add(date);
            }
            System.out.println("[LongitudinalDistanceStudy] getDegreeSeparation returned " + dates.size() + " dates: " + dates);
        } catch (Exception e) {
            System.err.println("Error during LongitudinalDistanceStudy execution: " + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    // Getters and Setters
    public String getPlanet1() { return planet1; }
    public void setPlanet1(String planet1) { this.planet1 = planet1; }
    public String getPlanet2() { return planet2; }
    public void setPlanet2(String planet2) { this.planet2 = planet2; }
    public int getDistance() { return distance; }
    public void setAngle(int distance) { this.distance = distance; }
}
