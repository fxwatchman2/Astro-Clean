package com.astrocharts;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TickerList {
	private List<Ticker> tickers = null;

	public TickerList(String filename) throws IOException {
		this.tickers = new ArrayList<>();

		// Read file line by line
		try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
			String line;
			while ((line = reader.readLine()) != null) {
				String[] parts = line.split("\t"); // Split on tab character
				/*
				if (parts.length >= 2) {
					String ticker = parts[0];
					String companyName = parts[1];
					this.tickers.add(new Ticker(ticker, companyName));
				} 
				*/
				String ticker = parts[0];
				this.tickers.add(new Ticker(ticker, "No Name"));
			}
		}
	}

	public TickerList() throws IOException {
	}

	public static List<String> getTickerList12 (String filename) throws IOException {
		List<String> tickers12 = new ArrayList<String>();

		// Read file line by line
		try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
			String line;
			while ((line = reader.readLine()) != null) {
				tickers12.add(line);
			} 
		}
		return tickers12;
	}

	public List <String> getListOfSymbols() {
		ArrayList<String> allTickers =  new ArrayList<String>();

		for (int counter=0; counter<tickers.size(); counter++) {
			allTickers.add(tickers.get(counter).getSymbol());
		}
		return allTickers;
	}

	public Iterator<Ticker> getIterator() {
		return tickers.iterator();
	}

	public String buildTickerString(int startIndex, int numTickers) {
		if (startIndex < 1 || numTickers <= 0) {
			throw new IllegalArgumentException("Invalid arguments: startIndex must be >= 1 and numTickers must be positive.");
		}

		StringBuilder sb = new StringBuilder();
		int endIndex = Math.min(startIndex + numTickers - 1, tickers.size()); // Use list size

		for (int i = startIndex - 1; i < endIndex; i++) {
			sb.append(tickers.get(i).getSymbol());
			sb.append(", ");
		}

		// Remove trailing comma and space
		if (sb.length() > 2) {
			sb.delete(sb.length() - 2, sb.length());
		}

		return sb.toString();
	}

	private static class Ticker {
		private final String symbol;
		private final String companyName;

		public Ticker(String symbol, String companyName) {
			this.symbol = symbol;
			this.companyName = companyName;
		}

		public String getSymbol() {
			return symbol;
		}

		public String getCompanyName() {
			return companyName;
		}
	}

	public static void readAndFilterFile() throws IOException {
		// Define file paths
		String inputFile = "C:/stockdata/lists/MostActiveRaw.txt";
		String outputFile = "C:/stockdata/lists/MostActiveReady.txt";

		// Create BufferedReader and BufferedWriter objects
		BufferedReader reader = new BufferedReader(new FileReader(inputFile));
		BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));

		String line;
		try {
			// Read file line by line
			while ((line = reader.readLine()) != null) {
				// Split line using tab character or any desired delimiter
				String[] parts = line.split("\t"); // Replace with your delimiter if needed

				// Check if there are at least two parts
				if (parts.length >= 2) {
					// Write first two columns to output file
					writer.write(parts[0] + "\t" + parts[1] + "\n");
				}
			}
		} finally {
			// Close resources
			reader.close();
			writer.close();
		}

		System.out.println("File processing completed!");
	}
}
