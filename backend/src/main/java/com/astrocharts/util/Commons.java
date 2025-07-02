package com.astrocharts.util;

import java.util.List;
import java.io.IOException;


import java.io.IOException;
import java.util.List;

import com.astrocharts.TickerList;
import com.astrocharts.util.GlobalSettings;

public class Commons {

	public static List<String> getTickerList() {
    TickerList tickerList = null;
    try {
        tickerList = new TickerList(GlobalSettings.getListFilePath());
    } catch (IOException e) {
        e.printStackTrace();
        return java.util.Collections.emptyList();
    }

    if (tickerList == null) {
        return java.util.Collections.emptyList();
    }

    System.out.println("Calling tickerList.getListOfSymbols()");
    List<String> allTickers = tickerList.getListOfSymbols();
    System.out.println("allTickers.size) = " + allTickers.size());
    return allTickers;
}

    public static String extractFileName(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            throw new IllegalArgumentException("File path must not be null or empty.");
        }

        // Find the position of the last backslash
        int lastBackslash = filePath.lastIndexOf('\\');
        // Find the position of the last dot
        int lastDot = filePath.lastIndexOf('.');

        // Validate the positions
        if (lastBackslash == -1 || lastDot == -1 || lastDot <= lastBackslash) {
            throw new IllegalArgumentException("Invalid file path format.");
        }

        // Extract and return the substring between the last backslash and the last dot
        return filePath.substring(lastBackslash + 1, lastDot);
    }


}
