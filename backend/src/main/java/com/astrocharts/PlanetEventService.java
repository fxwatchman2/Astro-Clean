package com.astrocharts;

import com.astrocharts.services.AstroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class PlanetEventService {

    private final AstroService astroService;

    @Autowired
    public PlanetEventService(AstroService astroService) {
        this.astroService = astroService;
    }

    // Stub for getPlanetRetroDates
    public List<String> getPlanetRetroDates(int planet, String fromDate, String toDate, String shapeType, String color, String thickness) {
        // TODO: Integrate with your actual logic and data sources
        // Ancillary params (shapeType, color, thickness) can be logged or used as needed
        // For now, return two trading days in January 2024 and 2025 for testing
        return Arrays.asList("01/02/2024", "01/02/2025");
    }

    public List<PlanetLongitudeEvent> getPlanetLongitudeDates(PlanetLongitudeDatesRequest req) {
        System.out.println("[PlanetEventService] Request for longitude-dates: " + req.toString());

        String fromDateStr = req.getOldestBarDate() != null && req.getOldestBarDate().length() > 10 ? req.getOldestBarDate().substring(0, 10) : req.getOldestBarDate();
        String toDateStr = req.getNewestBarDate() != null && req.getNewestBarDate().length() > 10 ? req.getNewestBarDate().substring(0, 10) : req.getNewestBarDate();

        List<String> dates = astroService.getLongitudeDates(
                req.getPlanet(),
                req.getDegrees(),
                fromDateStr,
                toDateStr
        );

        List<PlanetLongitudeEvent> events = new ArrayList<>();
        if (dates != null) {
            for (String date : dates) {
                PlanetLongitudeEvent event = new PlanetLongitudeEvent();
                event.setDate(date);
                event.setPlanet(req.getPlanet());
                event.setDegrees(req.getDegrees());
                event.setShapeType(req.getShapeType());
                event.setColor(req.getColor());
                event.setThickness(req.getThickness());
                try {
                    event.getClass().getMethod("setPlanetName", String.class).invoke(event, req.getPlanetName());
                } catch (Exception ignored) {
                }
                events.add(event);
            }
        }

        System.out.println("[PlanetEventService] Returning " + events.size() + " longitude-dates events.");
        for (PlanetLongitudeEvent ev : events) {
            String name = "";
            try {
                name = (String) ev.getClass().getMethod("getPlanetName").invoke(ev);
            } catch (Exception ignored) {
            }
            System.out.println("  date=" + ev.getDate() + ", planet=" + ev.getPlanet() + " (" + name + "), degrees=" + ev.getDegrees() + ", color=" + ev.getColor() + ", type=" + ev.getShapeType());
        }

        return events;
    }
}
