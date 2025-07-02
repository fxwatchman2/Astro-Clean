package com.astrocharts.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.text.DecimalFormat;

import com.astrocharts.util.RowRecord;
import com.astrocharts.util.AllListsManager;

/**
 * A class that processes a list of RowRecord objects,
 * fills in missing daily records (weekends and holidays) by using the data
 * from the previous valid trading day, and returns a new list with the completed data.
 */
public class DailyDataCompleter {

    // Formatter for the date part (YYYY-MM-DD) for internal LocalDate conversions
    private static final DateTimeFormatter DATE_PART_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;
    // Suffix to add back for the full date string in RowRecord (T00:00:00)
    private static final String FULL_DATE_TIME_SUFFIX = "T00:00:00";

    // Decimal formatter for prices to ensure two decimal places when creating new records
    private static final DecimalFormat PRICE_FORMATTER = new DecimalFormat("0.00");

    /**
     * Processes a list of RowRecord objects, fills in missing daily records
     * (weekends and holidays) by using the data from the previous valid trading day,
     * and returns a new list with the completed data.
     *
     * @param inputRecords The list of original RowRecord objects.
     * @return A new List of RowRecord objects with missing days filled.
     * @throws IllegalArgumentException If the input list is null, empty, or dates are unparseable.
     */
    public List<RowRecord> calendarDailyData(List<RowRecord> inputRecords) throws IllegalArgumentException {
        if (inputRecords == null || inputRecords.isEmpty()) {
            throw new IllegalArgumentException("Input list of RowRecord objects cannot be null or empty.");
        }

        // TreeMap to store original records, mapping LocalDate to RowRecord.
        // This helps in sorting records by date and efficiently checking for date existence.
        TreeMap<LocalDate, RowRecord> originalRecordsMap = new TreeMap<>();
        LocalDate earliestDate = null;
        LocalDate latestDate = null;

        System.out.println("Beginning processing of " + inputRecords.size() + " input records...");

        // Populate the TreeMap and find the earliest and latest dates
        for (RowRecord record : inputRecords) {
            try {
                // Extract only the YYYY-MM-DD part from the RowRecord's date string
                // and parse it into a LocalDate object for internal calculations.
                LocalDate date = LocalDate.parse(record.getDate().split("T")[0], DATE_PART_FORMATTER);
                originalRecordsMap.put(date, record);

                // Update the overall earliest and latest dates encountered in the input data.
                if (earliestDate == null || date.isBefore(earliestDate)) {
                    earliestDate = date;
                }
                if (latestDate == null || date.isAfter(latestDate)) {
                    latestDate = date;
                }
            } catch (Exception e) {
                // Catch any parsing errors and provide a descriptive message.
                throw new IllegalArgumentException("Error parsing date '" + record.getDate() + "' from RowRecord. Expected format YYYY-MM-DDTHH:MM:SS", e);
            }
        }

        // Validate that we found at least one valid record and a date range.
        if (originalRecordsMap.isEmpty() || earliestDate == null || latestDate == null) {
            throw new IllegalArgumentException("No valid trading records could be parsed or date range determined from the input list.");
        }

        System.out.println("Determined data period from: " + earliestDate + " to " + latestDate);

        // This list will store all records, including the newly generated ones for missing days.
        List<RowRecord> completedRecords = new ArrayList<>();

        // This variable will keep track of the last valid RowRecord found in the original
        // data. Its values (open, high, low, close, volume) will be used to fill
        // missing days.
        RowRecord lastValidOriginalRecord = null;

        // Iterate day by day from the earliest date to the latest date.
        // This loop ensures every day within the range is considered.
        LocalDate currentDate = earliestDate;
        while (!currentDate.isAfter(latestDate)) {
            // Check if a record for the current date exists in the original data.
            RowRecord recordForCurrentDate = originalRecordsMap.get(currentDate);

            if (recordForCurrentDate != null) {
                // If a record exists for currentDate, add it to the completed list.
                completedRecords.add(recordForCurrentDate);
                // Update the last valid record to the current record.
                lastValidOriginalRecord = recordForCurrentDate;
            } else {
                // If no record exists for currentDate, it's a missing day (weekend/holiday).
                if (lastValidOriginalRecord != null) {
                    // Create a new RowRecord for the missing day.
                    // The date is set to the currentDate, and other fields are copied
                    // from the `lastValidOriginalRecord`.
                    // Prices are formatted to two decimal places using PRICE_FORMATTER.
                    RowRecord newRecord = new RowRecord(
                            currentDate.format(DATE_PART_FORMATTER) + FULL_DATE_TIME_SUFFIX, // Reconstruct the date string
                            Double.parseDouble(PRICE_FORMATTER.format(lastValidOriginalRecord.getOpen())),
                            Double.parseDouble(PRICE_FORMATTER.format(lastValidOriginalRecord.getHigh())),
                            Double.parseDouble(PRICE_FORMATTER.format(lastValidOriginalRecord.getLow())),
                            Double.parseDouble(PRICE_FORMATTER.format(lastValidOriginalRecord.getClose())),
                            lastValidOriginalRecord.getVolume()
                    );
                    completedRecords.add(newRecord);
                } else {
                    // This situation occurs if `earliestDate` itself is a missing day
                    // and there's no preceding record in `originalRecordsMap` to copy from.
                    // This warning indicates that some initial days might not be filled
                    // if the very first record is not at the start of the desired range.
                    System.err.println("Warning: Could not find a previous valid record to fill data for " + currentDate + ". This date will be skipped in the output.");
                }
            }
            currentDate = currentDate.plusDays(1); // Move to the next calendar day.
        }

        System.out.println("Data completion successful. Total records generated: " + completedRecords.size());
        return completedRecords;
    }

    /**
     * Main method for testing the DailyDataCompleter class.
     * This example demonstrates how to use the completeDailyData method.
     */
    public static void main(String[] args) {
        // --- Example Usage ---
        List<RowRecord> rawData = new ArrayList<>();

        boolean displayCounter = false;
        boolean intraday = false;
        AllListsManager allListsManager = new AllListsManager(displayCounter,intraday);
        List<RowRecord> applData = allListsManager.getList("AAPL");
        
        // TimeKeeper tk = new TimeKeeper(); 
        DailyDataCompleter completer = new DailyDataCompleter();

        System.out.println("\napplData.size() : " + applData.size());
        // tk.printlnReset();
        List<RowRecord> completedData = completer.calendarDailyData(applData);
        System.out.println("\ncompletedData.size() : " + completedData.size());
        // tk.println();
        
        if (true) return;
    }
}
