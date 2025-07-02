package com.astrocharts.util;

import com.astrocharts.util.RowRecord;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException;

//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;

public class DataParser {

	public static <T> List<T> getLastNItems(List<T> list, int n) {
        int startIndex = Math.max(0, list.size() - n);
        return list.subList(startIndex, list.size());
    }
	
	public static String getAnotherDisplayDateOnly(long daysBefore) {
		long dateNum = System.currentTimeMillis();
		daysBefore = daysBefore * 24 * 60 * 60 * 1000;
		dateNum = dateNum - daysBefore; 
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		String date1 = format1.format(dateNum);
		return date1;
	}

	public static String getTomorrowsDate() {
		long dateNum = System.currentTimeMillis() + 24*60*60*1000; // for the next day. Testing to include today in the download limit
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		String date1 = format1.format(dateNum);
		return date1;
	}
	
	public void appendRecordToFile(String filePath, RowRecord record) throws IOException {
		StringBuilder fileContent = new StringBuilder();

		fileContent.append(record.getDate())
		.append(",")
		.append(record.getOpen())
		.append(",")
		.append(record.getHigh())
		.append(",")
		.append(record.getLow())
		.append(",")
		.append(record.getClose())
		.append(",")
		.append(record.getVolume())
		.append("\n");

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
			writer.write(fileContent.toString());
			writer.newLine(); // Add a new line after the appended content
		}
	}

	public void writeRecordsToFile(String filePath, ArrayList<RowRecord> allRecords) {
		if (allRecords == null) return;
		if (allRecords.size() == 0) return;
		
		StringBuilder fileContent = new StringBuilder();

		Collections.reverse(allRecords); // oldest to newest

		// Add header row (optional)
		// fileContent.append("Date, Open, High, Low, Close, Volume\n");

		// Loop through each RowRecord and format data
		for (RowRecord record : allRecords) {
			fileContent.append(record.getDate())
			.append(",")
			.append(record.getOpen())
			.append(",")
			.append(record.getHigh())
			.append(",")
			.append(record.getLow())
			.append(",")
			.append(record.getClose())
			.append(",")
			.append(record.getVolume())
			.append("\n");
		}

		// Write content to file
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
			writer.write(fileContent.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void createOrAppendFile(String filePath, ArrayList<RowRecord> allRecords) {
	    if (allRecords == null) return;
	    if (allRecords.size() == 0) return;

	    StringBuilder fileContent = new StringBuilder();

	    Collections.reverse(allRecords); // oldest to newest

	    // Loop through each RowRecord and format data
	    for (RowRecord record : allRecords) {
	        fileContent.append(record.getDate())
	            .append(",")
	            .append(record.getOpen())
	            .append(",")
	            .append(record.getHigh())
	            .append(",")
	            .append(record.getLow())
	            .append(",")
	            .append(record.getClose())
	            .append(",")
	            .append(record.getVolume())
	            .append("\n");
	    }

	    // Open FileWriter in append mode (true)
	    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
	        writer.write(fileContent.toString());
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	public static void writeSymbolsToFile(String filePath, List<String> allSymbols) {
		if (allSymbols == null) return;
		if (allSymbols.size() == 0) return;
		
		StringBuilder fileContent = new StringBuilder();

		// Loop through each RowRecord and format data
		for (String symbol : allSymbols) {
			fileContent.append(symbol)
			.append("\n");
		}

		// Write content to file
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
			writer.write(fileContent.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static List<RowRecord> fixClosingPrice(List<RowRecord> allRecords) {
		  ArrayList<RowRecord> fixedRecords = new ArrayList<>();
		  
		  for (RowRecord record : allRecords) {
		    double close = record.getClose();
		    double volume = record.getVolume();
		    
		    // Create a new RowRecord with adjusted close price if necessary
		    RowRecord newRecord;
		    if (close == 0.0 || volume == 0) {
		      newRecord = new RowRecord(record.getDate(), record.getOpen(), record.getHigh(), 
		                                 record.getLow(), record.getHigh(), record.getVolume());
		    } else {
		      newRecord = record; // No change needed, use original record
		    }
		    
		    fixedRecords.add(newRecord);
		  }
		  
		  return fixedRecords;
		}
	
	public List<RowRecord> readRecordsFromFile(String filePath) throws IOException {
		List<RowRecord> records = new ArrayList<>();

		File file = new File(filePath);
		if (!file.exists()) {
			return records;
		}

		
		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String line;

			// Skip the header row (optional)
			//reader.readLine(); // Skip the first line if it's a header

			while ((line = reader.readLine()) != null) {
				String[] tokens = line.split(",");

				if (tokens.length != 6) {
					// Handle invalid lines (optional)
					// You can throw an exception, log a warning, or skip the line
					System.out.println("Warning: Invalid line format in file: " + filePath);
					continue;
				}

				// Parse data from each token
				String date = tokens[0];
				double open = Double.parseDouble(tokens[1]);
				double high = Double.parseDouble(tokens[2]);
				double low = Double.parseDouble(tokens[3]);
				double close = Double.parseDouble(tokens[4]);
				long volume = Long.parseLong(tokens[5]); // Assuming volume is a long

				// Create a RowRecord object
				RowRecord record = new RowRecord(date, open, high, low, close, volume);

				// Add the record to the list
				records.add(record);
			}
		}

		// fixes the problem of zero value for closing prices where volume is zero, especially for the minute bars
		List<RowRecord> correctArray = fixClosingPrice(records);
		return correctArray;
	}

	public ArrayList<String> getSymbolsList(String symbolsString) {
		ArrayList<String> symbols = new ArrayList<>();

		// Split the string using comma as delimiter
		String[] symbolArray = symbolsString.split(",");

		// Add each symbol to the ArrayList (trimming whitespaces)
		for (String symbol : symbolArray) {
			symbols.add(symbol.trim());
		}

		return symbols;
	}

	
	public ArrayList<RowRecord> parseCSVResponseForOneTicker(String responseBody,String tickerSymbol) {
		ArrayList<RowRecord> records = new ArrayList<>();

		String[] lines = responseBody.split("\n");
		
		for (int counter= 1; counter<lines.length; counter++) {
			//System.out.println(lines[counter]);
			RowRecord r = new RowRecord(lines[counter]);
			records.add(r);
		}
		return records;
	}
	
	public ArrayList<String> parseCSVResponseForSymbolList(String responseBody) {
		ArrayList<String> records = new ArrayList<>();

		String[] lines = responseBody.split("\n");
		
		for (int counter= 1; counter<lines.length; counter++) {
			//System.out.println(lines[counter]);
			String[] parts = lines[counter].split(";");
			records.add(parts[0]);
		}
		return records;
	}
	
	
	
	public static ArrayList<RowRecord> parseResponseForOneTicker(String responseBody,String tickerSymbol) {

		ArrayList<RowRecord> records = new ArrayList<>();

		// Create a Gson instance for JSON parsing
		Gson gson = new Gson();

		// Parse JSON using Gson and extract data array
		JsonObject jsonObject = gson.fromJson(responseBody.toString(), JsonObject.class);
		JsonArray dataArray = jsonObject.has("data") ? jsonObject.get("data").getAsJsonArray() : null;

		// Check if data array exists before iterating
		if (dataArray == null) {
			throw new RuntimeException("Missing data array in response"); // Handle missing data
		}

		// Iterate through each object in the data array
		for (JsonElement element : dataArray) {
			JsonObject rowObject = element.getAsJsonObject();

			String symbol = rowObject.has("symbol") ? rowObject.get("symbol").getAsString() : null;
			if (symbol.equalsIgnoreCase(tickerSymbol)) {
				String date = rowObject.has("date") ? rowObject.get("date").getAsString() : null;
				
				double open = 0.0;
				try { 
					open = rowObject.has("open") ? rowObject.get("open").getAsDouble() : 0.0;
				} catch (Exception e) {
					continue;
				}
				
				if (rowObject.get("high")==null) continue;				
				double high = rowObject.has("high") ? rowObject.get("high").getAsDouble() : 0.0;
				
				if (rowObject.get("low")==null) continue;				
				double low = rowObject.has("low") ? rowObject.get("low").getAsDouble() : 0.0;

				if (rowObject.get("close")==null) continue;				
				double close = rowObject.has("close") ? rowObject.get("close").getAsDouble() : 0.0;

				long volume = 0;
				if (rowObject.get("volume")==null) continue;
				
				if (rowObject.get("volume").isJsonNull()) {
					  // Handle the missing value (e.g., set a default value or log a warning)
						continue;
				} else {
					volume = rowObject.get("volume").getAsLong();
				}
				

				// Create RowRecord object and add it to the list
				RowRecord record = new RowRecord(date, open, high, low, close, volume);
				records.add(record);
			}
		}
		return records;
	}

	public ArrayList<RowRecord> parseResponse(String responseBody,String symbolsString) {

		ArrayList<String> symbols = getSymbolsList(symbolsString);
		ArrayList<RowRecord> allRecords = null;

		for (int counter = 0; counter < symbols.size(); counter++) {
			String tickerSymbol = symbols.get(counter);
			// Assuming parseResponseForOneTicker processes the responseBody for the given tickerSymbol
			// and returns only its records.
			ArrayList<RowRecord> recordsForSymbol = parseResponseForOneTicker(responseBody, tickerSymbol);
			
			if (recordsForSymbol != null && !recordsForSymbol.isEmpty()) {
				// String filePath = ... (no valid file path, so skip writing to file)
				// this.writeRecordsToFile(filePath, recordsForSymbol); // Use recordsForSymbol here
				allRecords = recordsForSymbol; // Update allRecords, it will hold the last processed set
			}
		}
		return allRecords;
		// Save data. Overwrite the file if needed. 
		// This will need to be done only once per ticker.
		// After this, we will only append the daily data for each ticker
	}	
}
