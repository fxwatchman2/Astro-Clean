package com.astrocharts.util;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.io.IOException;

public class AllListsManager {

	// The main structure to hold a map of ticker names to lists of RowRecord
	private Map<String, List<RowRecord>> allLists;
	List<String> keys = null;
	
	// Used for a single ticker study sa well as a study that involves 1500 or more tickers
	
	// load only one ticker
	public AllListsManager(String ticker) {
		allLists = new HashMap<>();
		List<RowRecord> stockData = readOneTicker(ticker);
		if (stockData == null || stockData.isEmpty()) {
			throw new IllegalArgumentException("Could not find or load stock data for ticker: " + ticker);
		}
		Collections.reverse(stockData);
		addList(ticker, stockData);
	}

	// load all tickers
	
	public AllListsManager() {
		allLists = new HashMap<>();
		//boolean displayCounter = false;
		//loadAllData(displayCounter);
		// default constructor changed to NOT load any data
	}

	public AllListsManager(boolean displayCounter) {
		allLists = new HashMap<>();
		loadAllData(displayCounter);
	}

	public AllListsManager(boolean displayCounter,boolean intraday) {
		allLists = new HashMap<>();
		if (intraday) {
			loadAllIntraData(displayCounter);
		} else {
			loadAllData(displayCounter);	
		}
		
	}

	// Method to add a new List<RowRecord> with a ticker name
	public void addList(String tickerName, List<RowRecord> recordList) {
		if (tickerName == null || tickerName.isEmpty()) {
			throw new IllegalArgumentException("Ticker name cannot be null or empty");
		}
		if (recordList == null) {
			throw new IllegalArgumentException("List cannot be null");
		}
		allLists.put(tickerName, recordList);
	}

	public List<String> getTickerList() {
		return new ArrayList<>(allLists.keySet());
	}
	
	// Method to retrieve a List<RowRecord> by ticker name
	public List<RowRecord> getList(String tickerName) {
		if (tickerName == null || tickerName.isEmpty()) {
			return null;
		}
		return allLists.getOrDefault(tickerName, Collections.emptyList());
	}

	// Method to retrieve all entries using an iterator
	public Iterator<Map.Entry<String, List<RowRecord>>> getIterator() {
		return allLists.entrySet().iterator();
	}

	// Method to get the total number of tickers
	public int getTickerCount() {
		return allLists.size();
	}

	public static List<RowRecord> readOneTicker(String ticker) {
    DataParser dParser = new DataParser();
    try {
        String filePath = GlobalSettings.getBaseFolder() + GlobalSettings.eodFolder + ticker + GlobalSettings.stockfileExt;
        return dParser.readRecordsFromFile(filePath);
    } catch (IOException e) {
        e.printStackTrace();
        return null;
    }
}

	public static List<RowRecord> readOneTickerIntra(String ticker) {
    DataParser dParser = new DataParser();
    try {
        String filePath = GlobalSettings.getBaseFolder() + GlobalSettings.intra5Folder + ticker + GlobalSettings.stockfileExt;
        return dParser.readRecordsFromFile(filePath);
    } catch (IOException e) {
        e.printStackTrace();
        return null;
    }
}

	private void loadAllData(boolean displayCounter) {
		List<String> symbolList = Commons.getTickerList();

		for (int counter=0; counter<symbolList.size(); counter++) {
			String ticker = symbolList.get(counter);
			List<RowRecord> stockData = readOneTicker(ticker);
			if (stockData==null) {
				continue;
			}
			// sanity check
			if (stockData.size()==0) {
				continue;
			}

			Collections.reverse(stockData);
			addList(ticker, stockData);
			if (displayCounter) System.out.println(counter);
		}
	}
	
	private void loadAllIntraData(boolean displayCounter) {
		List<String> symbolList = null;
		//symbolList = TickerList.getTickerList12(// GlobalSettings.getListFilePath12());
		symbolList = Commons.getTickerList();

		for (int counter=0; counter<symbolList.size(); counter++) {
			String ticker = symbolList.get(counter);

			List<RowRecord> stockData = readOneTickerIntra(ticker);
			if (stockData==null) {
				continue;
			}
			// sanity check
			if (stockData.size()==0) {
				continue;
			}
			
			// Filtering out stocks with current price less than 10
			// list is not reversed so the latest record is at index 0
			if (stockData.get(0).getClose() < 10.0) {
				continue;
			}
			// Filtering out stocks with current volume less than 500K
			if (stockData.get(stockData.size()-1).getVolume() < 2000) { // this needs to be controlled better
				continue;
			}
			//Collections.reverse(stockData);
			addList(ticker, stockData);
			if (displayCounter) System.out.println(counter);
		}
	}
	
	public static void main(String[] args) {
		long sTime = System.currentTimeMillis();
		boolean displayCounter = true;
		boolean intraday = false;
		
		AllListsManager allListsManager = new AllListsManager(displayCounter,intraday);
		long eTime = System.currentTimeMillis();
		System.out.println("Time Taken = " + (eTime-sTime) + "\tTicker Count = " + allListsManager.getTickerCount());
	}
}
