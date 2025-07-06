package com.astrocharts;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;


import com.astrocharts.dto.ChartDataResponse;
import com.astrocharts.dto.DrawingInstruction;
import com.astrocharts.dto.InstructionParams;
import com.astrocharts.dto.InstructionStyle;
import com.astrocharts.dto.InstructionMeta;
import com.astrocharts.studies.Study;
import com.astrocharts.studies.StudyGroup;
import com.astrocharts.dto.StudyResult;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.astrocharts.util.AllListsManager;
import com.astrocharts.util.RowRecord;

import java.util.List;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kalwit.services.AstroService;
import com.astrocharts.dto.IndicatorRequest;
import com.astrocharts.dto.IndicatorResponse;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/followstudy")
public class FollowStudyController {

    @PostMapping("/run-csg")
    public List<ChartDataResponse> runCSG(@RequestBody com.astrocharts.dto.RunCSGRequest req) {
        // --- BEGIN DIAGNOSTIC LOGGING ---
        System.out.println("[runCSG] Incoming studies: " + req.getStudies());
        System.out.println("[runCSG] Incoming symbol: " + req.getSymbol());
        // --- END DIAGNOSTIC LOGGING ---
        Logger logger = LoggerFactory.getLogger(FollowStudyController.class);
        logger.info("[runCSG] Received studies: {} symbol: {}", req.getStudies(), req.getSymbol());
        List<ChartDataResponse> responses = new ArrayList<>();
        // ... (existing logic populates responses)

        if (req.getStudies() != null && req.getSymbol() != null) {
            for (Object studyObj : req.getStudies()) {
                try {
                    // Convert LinkedHashMap to JSON string, then to Study using Jackson
                    com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
                    String json = mapper.writeValueAsString(studyObj);
                    com.astrocharts.studies.Study study = mapper.readValue(json, com.astrocharts.studies.Study.class);
                    // Build a StudyGroup with just this study
                    com.astrocharts.studies.StudyGroup singleGroup = new com.astrocharts.studies.StudyGroup();
                    singleGroup.setSymbol(req.getSymbol());
                    java.util.List<com.astrocharts.studies.Study> studies = new java.util.ArrayList<>();
                    studies.add(study);
                    singleGroup.setStudies(studies);
                    // Reuse calculateAspects logic
                    ChartDataResponse resp = calculateAspects(singleGroup);
                    responses.add(resp);
                } catch (Exception ex) {
                    logger.error("Failed to process study in runCSG", ex);
                    responses.add(new ChartDataResponse());
                }
            }
        }
        // --- BEGIN DIAGNOSTIC LOGGING ---
        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            String json = mapper.writeValueAsString(responses);
            logger.info("[runCSG] Returning responses (JSON): {}", json);
        } catch (Exception ex) {
            logger.error("[runCSG] Failed to serialize responses", ex);
        }
        // --- END DIAGNOSTIC LOGGING ---
        return responses;
    }


    private static final Logger logger = LoggerFactory.getLogger(FollowStudyController.class);

    private final AstroService astroService;

    @Autowired
    public FollowStudyController(AstroService astroService) {
        this.astroService = astroService;
    }

    @GetMapping("/dates")
    public List<String> getDatesForFollows(
            @RequestParam String planet1,
            @RequestParam String planet2,
            @RequestParam double degrees,
            @RequestParam String fromDate,
            @RequestParam String toDate) {
        // Convert planet names to numbers
        Integer planetNum1 = com.astrocharts.studies.LongitudinalDistanceStudy.getPlanetNumber(planet1);
        Integer planetNum2 = com.astrocharts.studies.LongitudinalDistanceStudy.getPlanetNumber(planet2);
        if (planetNum1 == null || planetNum2 == null) {
            throw new IllegalArgumentException("Invalid planet name(s): " + planet1 + ", " + planet2);
        }

        // The frontend might send a full ISO string (e.g., "2023-01-01T00:00:00").
        // We only need the date part.
        String fromDateStr = fromDate.length() > 10 ? fromDate.substring(0, 10) : fromDate;
        String toDateStr = toDate.length() > 10 ? toDate.substring(0, 10) : toDate;

        System.out.println("[FollowStudyController] About to call getDegreeSeparation with: " +
            "planetNum1=" + planetNum1 + ", planetNum2=" + planetNum2 +
            ", fromDate=" + fromDateStr + ", toDate=" + toDateStr +
            ", degrees=" + degrees);

        List<String> dates = astroService.getDegreeSeparation(
            planetNum1,
            planetNum2,
            fromDateStr,
            toDateStr,
            degrees
        );
        System.out.println("[FollowStudyController] getDegreeSeparation returned " + dates.size() + " dates: " + dates);
        return dates;
    }

    @PostMapping("/aspects")
    public ChartDataResponse calculateAspects(@RequestBody StudyGroup studyGroup) {
        logger.info("Received request for study group: {}", studyGroup.getName());
        List<DrawingInstruction> allInstructions = new ArrayList<>();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");

        // TODO: Replace hardcoded dates with a call to a real data service
        // that can fetch the date range for the given symbol.
        String symbol = studyGroup.getSymbol();
        logger.info("Processing studies for symbol: {}", symbol);

        if (symbol == null || symbol.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Symbol is required but was not provided in the request body.");
        }

            // Dynamically get the date range from the symbol's data
            AllListsManager allListsManager = new AllListsManager(symbol);
            List<RowRecord> stockData = allListsManager.getList(symbol);

            if (stockData == null || stockData.isEmpty()) {
                System.err.println("No data found for symbol: " + symbol + ". Cannot execute study.");
                // Optionally return an error response to the client
                return new ChartDataResponse(); // Return empty response
            }

            String chartFirstDate = stockData.get(0).getDate();
            String chartLastDate = stockData.get(stockData.size() - 1).getDate();

            System.out.println("Using dynamic date range for " + symbol + ": " + chartFirstDate + " to " + chartLastDate);

        // Color palette for CSG lines (not used if study color is set)
// String[] colorPalette = {"#e6194b", "#3cb44b", "#ffe119", "#4363d8", "#f58231", "#911eb4", "#46f0f0", "#f032e6", "#bcf60c", "#fabebe", "#008080", "#e6beff", "#9a6324", "#fffac8", "#800000", "#aaffc3", "#808000", "#ffd8b1", "#000075", "#808080"};
// int colorIdx = 0;
for (Study study : studyGroup.getStudies()) {
            // Log the received study details to confirm deserialization
            logger.info("Processing study. Type: {}, DisplayType: {}, Color: {}, Tuple: {}",
                    study.getClass().getSimpleName(), study.getDisplayType(), study.getColor(), study.getTuple());
            if (study.getColor() == null || study.getColor().trim().isEmpty()) {
                logger.warn("Study color is missing or empty for tuple: {}. Defaulting to blue.", study.getTuple());
            }
            if (study.getDisplayType() == null || study.getDisplayType().trim().isEmpty()) {
                logger.warn("DisplayType is missing for tuple: {}. Defaulting to vLine.", study.getTuple());
            }

            // The execute method currently returns hardcoded dates. This is fine for now.
            StudyResult result = study.execute(chartFirstDate, chartLastDate);

            for (java.util.Date date : result.getSingleDates()) {
                DrawingInstruction instruction = new DrawingInstruction();
                instruction.setCommand("line"); // Only allow line commands
                instruction.setTargetPanel("main");
                InstructionParams params = new InstructionParams();
                params.setDateTime(sdf.format(date));
                String shapeType = study.getDisplayType();
                if (shapeType == null || !(shapeType.equals("vLine") || shapeType.equals("circle") || shapeType.equals("square") || shapeType.equals("star") || shapeType.equals("downArrow"))) {
                    logger.warn("Unsupported or missing displayType for study: {}. Defaulting to vLine.", shapeType);
                    shapeType = "vLine";
                }
                // Set yValue=0.0 and y=0.0 for all shapes except vLine
                if (!"vLine".equals(shapeType)) {
                    params.setyValue(0.0);
                    params.setY(0.0);
                }
                instruction.setParams(params);
                InstructionStyle style = new InstructionStyle();
                style.setType(shapeType);
                style.setColor(study.getColor() != null && !study.getColor().trim().isEmpty() ? study.getColor() : "blue");
                instruction.setStyle(style);
                // Set meta with tooltip text (Tuple text)
                InstructionMeta meta = new InstructionMeta();
                // Simple diagnostic logging for tuple construction
                logger.info("[CSG DEBUG] Study class: {}, tuple: {}, color: {}, displayType: {}, frameOfReference: {}",
                    study.getClass().getSimpleName(),
                    study.getTuple(),
                    study.getColor(),
                    study.getDisplayType(),
                    study.getFrameOfReference());
                String tooltip = study.getTuple();
                if (study.getFrameOfReference() != null && !study.getFrameOfReference().trim().isEmpty()) {
                    tooltip += " (" + study.getFrameOfReference() + ")";
                }
                logger.info("Setting tooltip/meta for tuple: {} | Tooltip: {}", study.getTuple(), tooltip);
                meta.setTooltip(tooltip);
                meta.setFrameOfReference(study.getFrameOfReference());
                instruction.setMeta(meta);
                System.out.println("[calculateAspects] Set meta.frameOfReference=" + meta.getFrameOfReference() + " for tupleText=" + study.getTuple());
                // Also set params.text for frontend label rendering if supported
                params.setText(study.getTuple());
                allInstructions.add(instruction);
            }

            // For date pairs, only draw lines at the start and end dates (no zones or markAreas)
        for (com.astrocharts.dto.DatePair pair : result.getDatePairs()) {
            DrawingInstruction startLine = new DrawingInstruction();
            startLine.setCommand("line");
            startLine.setTargetPanel("main");
            InstructionParams startParams = new InstructionParams();
            startParams.setDateTime(sdf.format(pair.getStartDate()));
            startLine.setParams(startParams);
            InstructionStyle style1 = new InstructionStyle();
            style1.setType(study.getDisplayType() != null ? study.getDisplayType().toLowerCase().replace(" ", "-") : "vertical-line");
            style1.setColor(study.getColor() != null && !study.getColor().trim().isEmpty() ? study.getColor() : "blue");
            startLine.setStyle(style1);
            InstructionMeta meta1 = new InstructionMeta();
            String tooltipStart = "Start: " + pair.getDescription();
if (study.getFrameOfReference() != null && !study.getFrameOfReference().trim().isEmpty()) {
    tooltipStart += " (" + study.getFrameOfReference() + ")";
}
meta1.setTooltip(tooltipStart);
            meta1.setFrameOfReference(study.getFrameOfReference());
            startLine.setMeta(meta1);
            System.out.println("[calculateAspects] Set meta1.frameOfReference=" + meta1.getFrameOfReference() + " for Start: " + pair.getDescription());
            allInstructions.add(startLine);

            DrawingInstruction endLine = new DrawingInstruction();
            endLine.setCommand("line");
            endLine.setTargetPanel("main");
            InstructionParams endParams = new InstructionParams();
            endParams.setDateTime(sdf.format(pair.getEndDate()));
            endLine.setParams(endParams);
            InstructionStyle style2 = new InstructionStyle();
            style2.setType(study.getDisplayType() != null ? study.getDisplayType().toLowerCase().replace(" ", "-") : "vertical-line");
            style2.setColor(study.getColor() != null && !study.getColor().trim().isEmpty() ? study.getColor() : "blue");
            endLine.setStyle(style2);
            InstructionMeta meta2 = new InstructionMeta();
            String tooltipEnd = "End: " + pair.getDescription();
if (study.getFrameOfReference() != null && !study.getFrameOfReference().trim().isEmpty()) {
    tooltipEnd += " (" + study.getFrameOfReference() + ")";
}
meta2.setTooltip(tooltipEnd);
            meta2.setFrameOfReference(study.getFrameOfReference());
            endLine.setMeta(meta2);
            System.out.println("[calculateAspects] Set meta2.frameOfReference=" + meta2.getFrameOfReference() + " for End: " + pair.getDescription());
            allInstructions.add(endLine);
        }
        // colorIdx++; // Move to next color for next study (palette not used if study color is set)
        }

        ChartDataResponse response = new ChartDataResponse();
        response.setDrawingInstructions(allInstructions);
        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            String json = mapper.writeValueAsString(allInstructions);
            logger.info("[calculateAspects] DrawingInstructions (JSON): {}", json);
            System.out.println("[calculateAspects] DrawingInstructions (JSON): " + json);
        } catch (Exception ex) {
            logger.error("[calculateAspects] Failed to serialize drawing instructions", ex);
            System.out.println("[calculateAspects] Failed to serialize drawing instructions: " + ex.getMessage());
        }
        logger.info("Returning {} drawing instructions.", allInstructions.size());
        return response;
    }

    @PostMapping("/indicator")
    public IndicatorResponse getIndicator(@RequestBody IndicatorRequest request) {

        String symbol = request.getSymbol();
        String indicatorType = request.getIndicator().getType();
        Map<String, Object> params = request.getIndicator().getParams();
        String planet = params != null ? (String) params.get("planet") : null;
        String fromDate = request.getFromDate();
        String toDate = request.getToDate();
    Integer planetNum = com.astrocharts.util.PlanetUtils.getPlanetNumber(planet);
    
        int numBars = 200;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate from = LocalDate.parse(request.getFromDate(), formatter);
            LocalDate to = LocalDate.parse(request.getToDate(), formatter);
            numBars = (int) (to.toEpochDay() - from.toEpochDay() + 1);
            if (numBars < 1) numBars = 200;
        } catch (Exception e) {
            // fallback
        }
        IndicatorResponse resp = new IndicatorResponse();
        resp.setIndicatorType(request.getIndicator().getType());
        resp.setPanel(request.getIndicator().getPanel());
        resp.setColor("main".equals(request.getIndicator().getPanel()) ? "#ff9800" : "#1976d2");
        resp.setLineStyle("main".equals(request.getIndicator().getPanel()) ? "solid" : "dashed");
        java.util.List<Double> values = new java.util.ArrayList<>();
        if ("main".equals(request.getIndicator().getPanel())) {
            // Sine wave
            double amplitude = 10.0;
            double center = 100.0;
            int cycles = 3;
            for (int i = 0; i < numBars; i++) {
                double val = center + amplitude * Math.sin(2 * Math.PI * cycles * i / numBars);
                values.add(val);
            }
        } else {
            // Random walk for volume
            double base = 1000.0;
            for (int i = 0; i < numBars; i++) {
                base = base + (Math.random() - 0.5) * 0.08 * base;
                values.add(Math.max(0, base));
            }
        }
        resp.setValues(values);
        return resp;
    }

    @PostMapping("/getPlanetaryLines")
    public List<Map<String, Object>> getPlanetaryLines(@RequestBody com.astrocharts.dto.PlanetaryLinesRequest req) {
        System.out.println("[getPlanetaryLines] Called with: planet=" + req.getPlanet() + ", color=" + req.getColor() + ", type=" + req.getType() + ", thickness=" + req.getThickness() + ", factor=" + req.getFactor() + ", dates.size=" + (req.getDates() != null ? req.getDates().size() : 0) + ", lowest=" + req.getLowest() + ", highest=" + req.getHighest());
        int n = req.getDates() != null ? req.getDates().size() : 0;
        System.out.println("[getPlanetaryLines] n (dates.size): " + n);
        List<Map<String, Object>> overlays = new ArrayList<>();
        String[] planets = {req.getPlanet(), "Jupiter"}; // Demonstrate support for multiple overlays
        for (int p = 0; p < planets.length; p++) {
            List<Map<String, Object>> points = new ArrayList<>();
            double base = req.getLowest() + (req.getHighest() - req.getLowest()) / 2.0 + 10 * p;
            for (int i = 0; i < n; i++) {
                String date = req.getDates().get(i);
                double value = base + Math.sin(i * 0.1 + p) * 10 + (p == 1 ? 20 : 0);
                Map<String, Object> point = new java.util.HashMap<>();
                point.put("date", date);
                point.put("value", value);
                points.add(point);
            }
            Map<String, Object> overlay = new java.util.HashMap<>();
            overlay.put("planet", planets[p]);
            overlay.put("points", points);
            overlays.add(overlay);
            // Debug log first and last 3 points
            System.out.println("[getPlanetaryLines] Overlay for " + planets[p] + ":");
            System.out.println("  points (first 3): " + points.subList(0, Math.min(3, points.size())));
            System.out.println("  points (last 3): " + points.subList(Math.max(0, points.size()-3), points.size()));
        }
        return overlays;
    }
}
