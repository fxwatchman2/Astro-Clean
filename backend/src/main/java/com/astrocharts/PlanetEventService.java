package com.astrocharts;

import com.kalwit.services.AstroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import com.kalwit.services.dto.*;

@Service
public class PlanetEventService {

    private final AstroService astroService;

    @Autowired
    public PlanetEventService(AstroService astroService) {
        this.astroService = astroService;
    }

    public List<String> getPlanetRetroDates(int planet, String fromDate, String toDate) {
        // This is dummy data for testing purposes.
        // The real implementation will call the AstroService.
        List<String> dates = new ArrayList<>();
        dates.add("2024-04-01");
        dates.add("2024-08-15");
        dates.add("2024-12-01");
        return dates;
    }

    public List<String> getPlanetDirectDates(int planet, String fromDate, String toDate) {
        // Dummy data as requested
        List<String> dates = new ArrayList<>();
        dates.add("2024-03-15");
        dates.add("2024-07-20");
        dates.add("2024-11-25");
        return dates;
    }

    public List<com.kalwit.services.dto.PlanetLongitudeEvent> getPlanetLongitudeDates(PlanetLongitudeDatesRequest req) {
        System.out.println("[PlanetEventService] Request for longitude-dates: " + req.toString());

        // Create the request DTO for the kalwit AstroService
        com.kalwit.services.dto.PlanetLongitudeDatesRequest kalwitReq = new com.kalwit.services.dto.PlanetLongitudeDatesRequest();
        kalwitReq.setPlanet(req.getPlanet());
        kalwitReq.setDegrees(req.getDegrees());
        kalwitReq.setOldestBarDate(req.getOldestBarDate());
        kalwitReq.setNewestBarDate(req.getNewestBarDate());
        kalwitReq.setShapeType(req.getShapeType());
        kalwitReq.setColor(req.getColor());
        kalwitReq.setThickness(req.getThickness());

        // Call the kalwit AstroService
        List<com.kalwit.services.dto.PlanetLongitudeEvent> kalwitEvents = astroService.getPlanetLongitudeDates(kalwitReq);

        System.out.println("[PlanetEventService] Returning " + kalwitEvents.size() + " longitude events.");
        return kalwitEvents;
    }
}
