package com.kalwit.libFoundation;

import java.util.ArrayList;import java.util.List;

import swisseph.*;

public class TithiCalculator {

	// -A is Ashubh, others are Shubh
	static String yogNames[] = {"empty", 
							"Vishkambh-A","Preeti","Ayushman","Soubhagya","Shobhan","Atiganda-A","Skarma","Dhriti","Shul-A",
							"Ganda-A","Vridhhi","Druv","Vyaghat-A","Harshan","Vajra-A","Siddhi","Vyatipat-A","Variyan",
							"Parigh-A","Shiv","Siddha","Sadhya","Shubh","Shukl","Bhahma","Aindra","Vaidhruti-A"
						};
	
	
	public static double getDiff(double moonLongi, double sunLongi) {
		double diff = moonLongi - sunLongi;
		if (diff < 0) {
			diff = diff + 360;
		} 
		return diff;
	}

	public static int getYog(long millis) {
		SweDate sdCurrDate = Conversions.getSweDate(millis);
		double sunLongi = SwissHelper.getSiderealLongitude(sdCurrDate, 0); 
		double moonLongi = SwissHelper.getSiderealLongitude(sdCurrDate, 1);
		return 1 + (int)(((sunLongi + moonLongi)%359.991)/13.333); 
	}
	
	public static String getYogName(long millis) {
		int yog = getYog(millis);
		System.out.println("Yog # " + yog);
		return yogNames[yog];
	}
	
	public static int getTithi(long millis) {
		SweDate sdCurrDate = Conversions.getSweDate(millis);
		double sunLongi = SwissHelper.getSiderealLongitude(sdCurrDate, 0); 
		double moonLongi = SwissHelper.getSiderealLongitude(sdCurrDate, 1);
		
		int tithi = 0;
		double diff = getDiff(moonLongi,sunLongi);
		tithi = (int) (diff/12.0);
		return tithi;
	}

	public static int getNavamsha(long millis, int planet) {
		SweDate sdCurrDate = Conversions.getSweDate(millis);
		double longi = SwissHelper.getSiderealLongitude(sdCurrDate, planet);
		int navRashiNum = Conversions.getZeroBasedNavamansh(longi);
		return navRashiNum;
	}
	
	public static int getNakshatra(long millis, int planet) {
		SweDate sdCurrDate = Conversions.getSweDate(millis);
		double longi = SwissHelper.getSiderealLongitude(sdCurrDate, planet);
		int nakshatraNum = (int) (longi/13.333);
		return nakshatraNum;
	}
	
	
	public static List<Long> processTithis(String startDateString, String endDateString) {
        List<Long> listOfTithis = new ArrayList<>();
        double tithiDiff = 12.0;
        
		long startMillis = Utilities.getTimeInMillis(startDateString);
		long endMillis = Utilities.getTimeInMillis(endDateString);

		for (long currMillis = startMillis; currMillis < endMillis; currMillis += (Conversions.millisInADay/2)) {

			SweDate sdCurrDate = Conversions.getSweDate(currMillis);
			double sunLongi = SwissHelper.getSiderealLongitude(sdCurrDate, 0); 
			double moonLongi = SwissHelper.getSiderealLongitude(sdCurrDate, 1);
			int tithi = 0;
			double diff = getDiff(moonLongi,sunLongi);
			tithi = (int) (diff/12.0);

			currMillis += (Conversions.millisInADay/2);
			sdCurrDate = Conversions.getSweDate(currMillis);
			sunLongi = SwissHelper.getSiderealLongitude(sdCurrDate, 0); 
			moonLongi = SwissHelper.getSiderealLongitude(sdCurrDate, 1);
			int tithi2 = 0;
			diff = getDiff(moonLongi,sunLongi);
			tithi2 = (int) (diff/12.0);
			
			listOfTithis.add(currMillis);
			System.out.println("Tithi begin = " + tithi + "\tTithi end = " + tithi2 + "\tdiff:" + Conversions.getDblStr(diff)  
					+ "\t\tsunLongi:" + Conversions.getDblStr(sunLongi) + "\tmoonLongi:" + Conversions.getDblStr(moonLongi));
		}

        return listOfTithis;
    }
    
    public static void main(String[] args) {
        String startDateString = "2023-01-01";
        String endDateString = "2024-01-01";
        List<Long> tithis = processTithis(startDateString, endDateString);
        for (Long tithi : tithis) {
            //System.out.println(Conversions.getDisplayDate(tithi));
        }
    }
}
