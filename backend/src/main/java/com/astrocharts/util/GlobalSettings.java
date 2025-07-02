package com.astrocharts.util;

public class GlobalSettings { 
	public static String baseFolder = "c:/stockdata/";
	public static String researchFolder = "research/new/";
	
	public static String planetsFolder = "C:/Data/planets/";
	public static String datesFolder = "C:/Data/planets/OnlyDates/";
	public static String codeFolder = baseFolder + "TradingViewCode/";
	
	// Dates for conjunctions of planets and Fixed star 
	public static String fixedStarsDatesFolder = "C:/Data/fixedStars/FixedStars-OnlyDates/";

	// Dates for full moon and new moon events by nakshatras 
	public static String fmnmDatesFolder = "C:/Data/fixedStars/FMNM-N-OnlyDates/";
	
	public static String pivotCounterFolder = "pivotCounter/";
	public static String dailyPivotCounterFile = "dailyPivot.txt";
	public static String weeklyPivotCounterFile = "weeklyPivot.txt";
	
	public static String listFolder = "lists/";
	
	// ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	// intraBaseList is manually updated
	// intraOptionVolList will need to be updated programmatically every day (EOD)
	// intraRSIObOsList is generated and updated by RSI extreme calculator (option 8 in main)
	//
	// intraRecentList is created and saved on start. It is the merged list created from the three lists above 
	// ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	// mergeAndDeduplicateFiles merges and de-duplicates these to produce a final list (intraRecentList) used for getting intra-day 5 minute data for real-time RSI pattern matching
	// ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	public static String intraBaseList = baseFolder + listFolder + "intraBaseList.txt"; 
	public static String intraOptionVolList = baseFolder + listFolder + "intraOptionVolList.txt"; 
	public static String intraRSIObOsList = baseFolder + listFolder + "intraRSIObOsList.txt"; 
	public static String intraRecentList = baseFolder + listFolder + "intraRecentList.txt"; 

	// ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	
	public static String eodFolder = "eod/";
	public static String eowFolder = "eow/";
	public static String intra1Folder = "intra1/";
	public static String intra5Folder = "intra5/";
	public static String stockfileExt = ".txt";
	public static String eventHighDataFile = "eventHighData.txt";
	public static String eventLowDataFile = "eventLowData.txt";
	//public static int rateLimitPerMinute = 50;
	public static int rateLimitPerMinute = 600;

	static private String twelveKey = "b320cc7822e948dcbd26ec35963e79eb"; 
	
	public static enum ProjectionStatus { INITIATED,TRIGGERED, MET, PARTIALLYMET,INVALIDATED,NOPROJECTION };
	public static enum ProjectionType { UP,DOWN };

	public static enum actions { SHORTSELL,BUY, COVER, CLOSE};

	public static enum StudyType { L2HP, HP2L, LP2H, H2LP }; // L - Low, H- High, 2 - to, HP - High Pivot, LP - Low Pivot
	
	public static String getTDKey() {
		return twelveKey;
	}
	public static String getBaseFolder() {
		return baseFolder; 
	}

	public static String getDailyPivotCounterFile() {
		return baseFolder + pivotCounterFolder + dailyPivotCounterFile; 
	}

	public static String getWeeklyPivotCounterFile() {
		return baseFolder + pivotCounterFolder + weeklyPivotCounterFile; 
	}

	public static String getListFolder() {
		return baseFolder + listFolder; 
	}

	public static String getNyseListName() {
		return baseFolder + listFolder + "nyse.txt"; 
	}

	public static String getListFilePath() {
		return getListFolder() + "tickerlist.txt";
	}
}
