package com.kalwit.libFoundation;

import swisseph.SweConst;
import swisseph.SweDate;
import swisseph.SwissEph;
import swisseph.TCPlanet;
import swisseph.TransitCalculator;
import java.util.ArrayList;
import java.util.Calendar;

public class AspectManager {

	static SwissEph sw = SwissHelper.getSwissEph();

	ArrayList<DayMapTuple> dayMap = new ArrayList<DayMapTuple>();

	// -------------------------------------------------------------------------------
	// Just a wrapper for getting ingresses in signs
	// -------------------------------------------------------------------------------
	public SweDate getIngressIn(int planet, long afterTime, int zeroBasedSignNumber, boolean sidereal) {
		zeroBasedSignNumber = zeroBasedSignNumber % 12;
		return getConjunctDegree(planet, afterTime, zeroBasedSignNumber * 30, sidereal);
	}

	public SweDate getHelioIngressIn(int planet, long afterTime, int zeroBasedSignNumber) {
		zeroBasedSignNumber = zeroBasedSignNumber % 12;
		return getHelioConjunctDegree(planet, afterTime, zeroBasedSignNumber * 30);
	}

	public SweDate getConjunctTropicalDegree(int planet, long afterTime, double degrees) {
		boolean sidereal = false;
		return getConjunctDegree(planet, afterTime, degrees, sidereal);
	}

	public SweDate getConjunctSiderealDegree(int planet, long afterTime, double degrees) {
		boolean sidereal = true;
		return getConjunctDegree(planet, afterTime, degrees, sidereal);
	}

	public SweDate getIngressInNakshatra(int planet, long afterTime, double degrees) {
		return getConjunctSiderealDegree(planet, afterTime, degrees);
	}

	// -------------------------------------------------------------------------------
	// Can be used for getting a planet's conjunction with a specific degree
	// And by implication, can be used for ingresses
	// time returned in ET
	// -------------------------------------------------------------------------------
	public static SweDate getConjunctDegree(int planet, long afterTime, double degrees, boolean sidereal) {
		int hour = -4;
		int flags = 0;

		if (sidereal) {
			flags = SweConst.SEFLG_SWIEPH | SweConst.SEFLG_SIDEREAL | SweConst.SEFLG_TRANSIT_LONGITUDE;
		} else {
			flags = SweConst.SEFLG_SWIEPH | SweConst.SEFLG_TRANSIT_LONGITUDE;
		}

		boolean backwards = false;

		/*
		 * -----------------------------------------------------------------------------
		 * ---------------------- 6/25/2022 TCPlanet is an implementation of
		 * TransiTCalculator Set the planet and degree to find when this planet will
		 * cross that degree with a call to getTransitUT first time after the specified
		 * time. This is useful in finding when a planet will arrive at the degree of
		 * where an eclipse occurred in future. This can result in a precipitous drop in
		 * the market at that time. Will also be useful in FM-NM monthly market behavior
		 * -----------------------------------------------------------------------------
		 * ----------------------
		 */

		TransitCalculator thePlanet = new TCPlanet(sw, planet, flags, degrees);
		SweDate sd = Conversions.getSweDate(afterTime, hour);
		double nextTransit = sw.getTransitUT(thePlanet, sd.getJulDay(), backwards);
		return new SweDate(nextTransit);
	}

	public static SweDate getHelioConjunctDegree(int planet, long afterTime, double degrees) {
		int hour = -5;
		int flags = SweConst.SEFLG_SWIEPH | SweConst.SEFLG_HELCTR;

		boolean backwards = false;
		TransitCalculator thePlanet = new TCPlanet(sw, planet, flags, degrees);
		SweDate sd = Conversions.getSweDate(afterTime, hour);
		double nextTransit = sw.getTransitUT(thePlanet, sd.getJulDay(), backwards);
		return new SweDate(nextTransit);
	}
	
	int getTithi(long millis) {
		double sunL = SwissHelper.getSiderealLongitude(Utilities.getSweDate(millis, 0.0), SweConst.SE_SUN);
		double moonL = SwissHelper.getSiderealLongitude(Utilities.getSweDate(millis, 0.0), SweConst.SE_MOON);
		
	    // Calculate the Tithi
        double tithi = (moonL - sunL) / 12.0;
        tithi = (tithi - Math.floor(tithi)) * 30;
		return (int)tithi;
	}

	public void printAllNakshatraIngresses() {
		String startDateString = "1901-01-01";
		String endDateString = "2100-01-01";

		long atTime = Conversions.getTimeInMillis(startDateString);
		long endTime = Conversions.getTimeInMillis(endDateString);

		SwissEph sw = new SwissEph("C:/Jars");

		int maxPlanets = (Utilities.planetsList.length) - 1;

		boolean sidereal = true;
		for (int planetCounter = 1; planetCounter < maxPlanets; planetCounter++) {
			if (planetCounter == 1)
				continue;
			int planetToStudy = Utilities.planetsList[planetCounter];

			System.out.println();
			System.out.println("Planet\t" + Utilities.getPlanetName(planetCounter));
			System.out.println("--------------------------------------------");
			atTime = Conversions.getTimeInMillis(startDateString);

			while (atTime < endTime) {
				for (int nakCounter = 0; nakCounter < 27; nakCounter++) {
					SweDate nextTransit = getIngressInNakshatra(planetToStudy, atTime, nakCounter);
					System.out.print(Utilities.getNakshatraName(nakCounter) + "\t");
					System.out.println(Conversions.getDisplayDate(nextTransit));
					atTime = Conversions.getTimeInMillisDateAndTime(nextTransit);
				}
				System.out.println();
			}
		}
	}

	// 1 is Sunday and 7 is Saturday
    public int getDayOfTheWeek(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        return dayOfWeek;
    }
	    
    // TODO: Calculate numbers here with 3 calls rather than 5
	public DayMapTuple buildDayMap(long currMillis, int planet, DayMapTuple dmt) {
		int planetEarth = 14;
		dmt.dateTime = currMillis;
		dmt.rawData[planet] = SwissHelper.getSiderealRawData(Utilities.getSweDate(currMillis, 0.0), planet);
		dmt.rawHelioData[planet] = SwissHelper.getHelioRawData(Utilities.getSweDate(currMillis, 0.0), planet);
		dmt.earthLongi = SwissHelper.getHelioLongitude(Utilities.getSweDate(currMillis, 0.0), planetEarth);
		System.out.print(Utilities.getPlanetName(planet) + "  ");
		System.out.println(Utilities.getDblStr(dmt.rawData[planet][0]));
		
		double tropicalLongi = SwissHelper.getTropicalLongitude(Utilities.getSweDate(currMillis, 0.0), planet);
		double siderealLongi = SwissHelper.getSiderealLongitude(Utilities.getSweDate(currMillis, 0.0), planet);

		dmt.tropicalRashi[planet] = (int) (tropicalLongi / 30.0);
		dmt.siderealRashi[planet] = (int) (siderealLongi / 30.0);
		dmt.siderealNakshatra[planet] = (int) (siderealLongi / 13.33333333);
		dmt.declination[planet] = SwissHelper.getDeclination(Utilities.getSweDate(currMillis, 0.0), planet);
		
		dmt.tithiMarketStart = TithiCalculator.getTithi(dmt.dateTime);
		dmt.tithiMarketEnd = TithiCalculator.getTithi(dmt.dateTime+Conversions.millisInADay/2);
		dmt.yogMarketStart = TithiCalculator.getYogName(dmt.dateTime);
		dmt.yogMarketEnd = TithiCalculator.getYogName(dmt.dateTime+Conversions.millisInADay/2);
		
		
		dmt.retrograde[planet] = (dmt.rawData[planet][3] <= 0.002);
		dmt.dayOfTheWeek = getDayOfTheWeek(dmt.dateTime);

		return dmt;
	}

	public void printAllSignIngresses() {
		String startDateString = "1991-01-01";
		String endDateString = "1992-01-01";

		long millis1 = Utilities.getTimeInMillis(startDateString);
		double d1 = SwissHelper.getTropicalLongitude(Utilities.getSweDate(millis1, 0.0), 2);
		long millis2 = Utilities.getTimeInMillis(endDateString);
		// double d2 =
		// SwissHelper.getSiderealLongitude(Utilities.getSweDate(millis2,0.0), 4);
		double d2 = SwissHelper.getTropicalLongitude(Utilities.getSweDate(millis2, 0.0), 2);

		long millisInADay = 24 * 60 * 60 * 1000;
		for (long currMillis = millis1; currMillis < millis2; currMillis += millisInADay) {
			d1 = SwissHelper.getTropicalLongitude(Utilities.getSweDate(currMillis, 0.0), 2);
			System.out.println(Utilities.getDisplayDateTime(currMillis) + " d1 = " + d1);
		}

		System.out.println("----------------------------------------------------------------------------------");

		double ayanamsha = sw.swe_get_ayanamsa_ut(System.currentTimeMillis());
		System.out.println("Current Ayanamsha Value: " + ayanamsha);

		System.out.println("d1 = " + d1);
		System.out.println("d2 = " + d2);
		System.out.println("d1-d2 = " + Math.abs(d1 - d2));

		// if (true) System.exit(0);

		long startTime = Conversions.getTimeInMillis(startDateString);
		long endTime = Conversions.getTimeInMillis(endDateString);

		SweDate sd = Conversions.getSweDate(startTime, 0);

		SwissHelper tempHelper = new SwissHelper();
		// tempHelper.getAllPlanetsData(sd);

		if (true)
			return;

		SwissEph sw = new SwissEph("C:/Jars");

		int maxPlanets = (Utilities.planetsList.length) - 1;

		boolean sidereal = true;
		for (int planetCounter = (maxPlanets - 2); planetCounter < maxPlanets; planetCounter++) {
			if (planetCounter == 1)
				continue;

			int planetToStudy = Utilities.planetsList[planetCounter];

			System.out.println();
			System.out.println("Planet = " + Utilities.getPlanetName(planetCounter));
			System.out.println();
			long atTime = Conversions.getTimeInMillis(startDateString);

			while (atTime < endTime) {
				for (int signCounter = 0; signCounter < 12; signCounter++) {
					SweDate nextTransit = getIngressIn(planetToStudy, atTime, signCounter, sidereal);
					System.out.println(
							Utilities.getSignName(signCounter) + " " + Conversions.getDisplayDate(nextTransit));
					// System.out.println(Conversions.getDisplayDate(nextTransit));
					// System.out.println("date = " + Conversions.getDisplayDateTime(nextTransit));
					atTime = Conversions.getTimeInMillisDateAndTime(nextTransit);
				}
				System.out.println();
			}
		}
	}

	public static void main(String... args) {
		long currMillis =  System.currentTimeMillis();
		for (int i=0; i< 12; i++) {
			double tropicalLongi = SwissHelper.getTropicalLongitude(Utilities.getSweDate(currMillis, 0.0), i);
			System.out.print(Utilities.getPlanetName(i) + "  ");
			System.out.println(Utilities.getDblStr(tropicalLongi));
		}
		
		System.out.println();
		System.out.println();
		
		for (int i=0; i< 12; i++) {
			double siderealLongi = SwissHelper.getSiderealLongitude(Utilities.getSweDate(currMillis, 0.0), i);
			System.out.print(Utilities.getPlanetName(i) + "  ");
			System.out.println(Utilities.getDblStr(siderealLongi));
		}
		
		//AspectManager aspectManager = new AspectManager();
		// aspectManager.printAllNakshatraIngresses();
		//System.out.println("Starting...");
		// aspectManager.printAllSignIngresses();
		//System.out.println("Done!");
	}
}

// flags = SweConst.SEFLG_SWIEPH | SweConst.SEFLG_HELCTR | SweConst.SEFLG_TRANSIT_LONGITUDE;

