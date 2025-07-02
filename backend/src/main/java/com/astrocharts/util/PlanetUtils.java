package com.astrocharts.util;

import java.util.Map;
import java.util.HashMap;

public class PlanetUtils {
    private static final Map<String, Integer> PLANET_MAP = new HashMap<>();
    static {
        PLANET_MAP.put("Sun", 0);
        PLANET_MAP.put("Moon", 1);
        PLANET_MAP.put("Mercury", 2);
        PLANET_MAP.put("Venus", 3);
        PLANET_MAP.put("Mars", 4);
        PLANET_MAP.put("Jupiter", 5);
        PLANET_MAP.put("Saturn", 6);
        PLANET_MAP.put("Uranus", 7);
        PLANET_MAP.put("Neptune", 8);
        PLANET_MAP.put("Pluto", 9);
        // Add more planets as needed
    }

    public static Integer getPlanetNumber(String planetName) {
        return PLANET_MAP.get(planetName);
    }
}
