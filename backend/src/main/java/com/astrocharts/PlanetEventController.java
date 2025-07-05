package com.astrocharts;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@RestController
@RequestMapping("/api/planet-event")
public class PlanetEventController {

    private final PlanetEventService planetEventService;

    @Autowired
    public PlanetEventController(PlanetEventService planetEventService) {
        this.planetEventService = planetEventService;
    }

    // Example: GET /api/planet-event/retro-dates?p=5&shapeType=Circle&color=Red&thickness=Medium
    @GetMapping("/retro-dates")
    public List<String> getPlanetRetroDates(
            @RequestParam("p") String planetStr,
            @RequestParam("fromDate") String fromDate,
            @RequestParam("toDate") String toDate,
            @RequestParam("shapeType") String shapeType,
            @RequestParam("color") String color,
            @RequestParam(value = "thickness", required = false, defaultValue = "Medium") String thickness
    ) {
        int planet = parsePlanet(planetStr);
        // Pass all params to the service
        // Returns two dates in MM/dd/yyyy format for each retrograde period
        return planetEventService.getPlanetRetroDates(planet, fromDate, toDate, shapeType, color, thickness);
    }

    // Helper method to convert planet string to int index (customize as needed)
    private int parsePlanet(String planetStr) {
        try {
            return Integer.parseInt(planetStr);
        } catch (NumberFormatException e) {
            // TODO: Add logic to map planet names to indices if needed
            throw new IllegalArgumentException("Invalid planet: " + planetStr);
        }
    }
}
