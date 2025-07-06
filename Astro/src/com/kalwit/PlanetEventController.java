package com.kalwit;

import com.kalwit.services.AstroService;
import com.kalwit.services.dto.PlanetLongitudeDatesRequest;
import com.kalwit.services.dto.PlanetLongitudeEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/planet-event")
public class PlanetEventController {
    @Autowired
    private AstroService astroService;

    @PostMapping("/longitude-dates")
    public List<PlanetLongitudeEvent> getPlanetLongitudeDates(@RequestBody PlanetLongitudeDatesRequest req) {
        System.out.println("[PlanetEventController] Received POST /api/planet-event/longitude-dates: " + req);
        return astroService.getPlanetLongitudeDates(req);
    }

    @GetMapping("/health")
    public String health() {
        System.out.println("[PlanetEventController] Health check hit");
        return "PlanetEventController is active";
    }
}
