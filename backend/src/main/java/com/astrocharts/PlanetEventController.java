package com.astrocharts;

import com.kalwit.services.dto.PlanetLongitudeDatesRequest;
import com.kalwit.services.dto.PlanetLongitudeEvent;
import com.kalwit.services.dto.PlanetEventResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public PlanetEventResponse getPlanetRetroDates(
            @RequestParam("planet") String planet,
            @RequestParam("studyType") String studyType,
            @RequestParam("fromDate") String fromDate,
            @RequestParam("toDate") String toDate,
            @RequestParam("shapeType") String shapeType,
            @RequestParam("color") String color,
            @RequestParam(value = "thickness", required = false, defaultValue = "Medium") String thickness
    ) {
        Integer planetNum = com.astrocharts.studies.LongitudinalDistanceStudy.getPlanetNumber(planet);
        if (planetNum == null) {
            throw new IllegalArgumentException("Invalid planet name: " + planet);
        }

        List<String> dates = planetEventService.getPlanetRetroDates(planetNum, fromDate, toDate);

        return new PlanetEventResponse(dates, shapeType, color, parseThickness(thickness));
    }

    @GetMapping("/direct-dates")
    public PlanetEventResponse getPlanetDirectDates(
            @RequestParam("planet") String planet,
            @RequestParam("studyType") String studyType,
            @RequestParam("fromDate") String fromDate,
            @RequestParam("toDate") String toDate,
            @RequestParam("shapeType") String shapeType,
            @RequestParam("color") String color,
            @RequestParam(value = "thickness", required = false, defaultValue = "Medium") String thickness
    ) {
        Integer planetNum = com.astrocharts.studies.LongitudinalDistanceStudy.getPlanetNumber(planet);
        if (planetNum == null) {
            throw new IllegalArgumentException("Invalid planet name: " + planet);
        }

        List<String> dates = planetEventService.getPlanetDirectDates(planetNum, fromDate, toDate);

        return new PlanetEventResponse(dates, shapeType, color, parseThickness(thickness));
    }

    // New endpoint for longitude-dates
    @PostMapping("/longitude-dates")
    public List<PlanetLongitudeEvent> getPlanetLongitudeDates(@RequestBody PlanetLongitudeDatesRequest req) {
        System.out.println("[PlanetEventController] Received POST /api/planet-event/longitude-dates: " + req);
        return planetEventService.getPlanetLongitudeDates(req);
    }

    private int parseThickness(String thicknessStr) {
        if (thicknessStr == null) {
            return 2; // Default to medium
        }
        switch (thicknessStr.toLowerCase()) {
            case "thin":
                return 1;
            case "medium":
                return 2;
            case "thick":
                return 3;
            default:
                try {
                    return Integer.parseInt(thicknessStr);
                } catch (NumberFormatException e) {
                    return 2; // Default to medium if parsing fails
                }
        }
    }
}
