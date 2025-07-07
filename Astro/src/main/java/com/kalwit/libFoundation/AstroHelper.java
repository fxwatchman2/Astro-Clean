package com.kalwit.libFoundation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AstroHelper {

	static double[] aspectArray = {0.0,30.0,60.0,90.0,120.0,180.0};
	static double[] nmFMaspectArray = {0.0,180.0};
	static long waitTimeAfterAnAspect = 6*60*60*1000; // 6 hours
	
	final static double auToKm = 149597870.7;	
	final static int planetSun = 0;
	final static int planetMoon = 1;
	
	public static String[] planetNames = {
			"Su","Mo","Me","Ve","Ma","Ju","Sa","Ur","Ne","Pl","sN","nN","","","Ea"
	};

	public static String[] signNames = {
			"Aries","Taurus","Gemini","Cancer","Leo","Virgo","Libra","Scorpio",
			"Sagittarius","Capricorn","Aquarius","Pisces"
	};
	public static String[] rashiNames = {
			"Mesh","Vrishabh","Mithun","Kark","Simha","Kanya","Tula","Vrischik","Dhanu",
			"Makar","Kumbh","Meen"
	};
	public static String[] nakshatraNames = {
			"Ashvini","Bharani","Krittika","Rohini","Mrigashirsha","Ardra","Punarvasu","Pushya",
			"Ashlesha","Magha","Purva Phalguni","Uttara Phalguni","Hasta","Chitra","Swati",
			"Vishakha","Anuradha","Jyeshtha","Mula","Purva Ashadha","Uttara Ashadha","Shravana",
			"Dhanishtha","Shatabhisha","Purva Bhadrapada","Uttara Bhadrapada","Revati"};

	ArrayList<DailySkySnap> calendar = null;
	static Map<String, DailySkySnap> dataMap = new HashMap<String, DailySkySnap>(100000);

	//ArrayList<ArrayList<PlotBandRecord>> allRetro = new ArrayList<ArrayList<PlotBandRecord>>();

	// a simple optimization to reduce  the number of searches to find the required SkySnap
	// int startingFrom = getFirstBarCounter(firstBarDate);
	// DailySkySnap dss = getDailySkySnap(startingFrom, currentBarDate);

	// Not used
	public int getFirstBarCounter(long date) {
		for (int counter= 0; counter<calendar.size(); counter++) {
			if (calendar.get(counter).date==date) return counter; 
		}
		return -1;
	}

	// Not used
	public DailySkySnap getDailySkySnap(int startingFrom, long date) {
		for (int counter= startingFrom; counter<calendar.size(); counter++) {
			if (calendar.get(counter).date==date) return calendar.get(counter); 
		}
		return null;
	}

	public static String getPlanetName(int planetNum) {
		return planetNames[planetNum];
	}

	public static String get1BasedPlanetName(int planetNum) {
		return getPlanetName(planetNum-1);
	}

	public static String getSignName(int num) {
		return signNames[num];
	}

	public static String get1BasedSignName(int num) {
		return getSignName(num-1);
	}

	public static String getNakshatraName(int num) {
		return nakshatraNames[num];
	}

	public static String get1BasedNakshatraName(int num) {
		return getNakshatraName(num-1);
	}

	/*
	public ArrayList<int> decorateForRetro(ArrayList<RowRecord> allRecords, int planetNum) {
		boolean retroFound =  false;
		int totalFound = 0;

		int startingCounter = getFirstBarCounter(allRecords.get(0).getDate());

		for (int counter=0; counter<allRecords.size(); counter++) {
			allRecords.get(counter).setSequenceNumber(0);

			System.out.println("counter = " + counter + " " + Utilities.getDisplayDate(allRecords.get(counter).getDate()));

			//DailySkySnap dailySkySnap = dataMap.get(allRecords.get(counter).getDate());
			DailySkySnap dailySkySnap = getDailySkySnap(startingCounter, allRecords.get(counter).getDate());

			if (dailySkySnap == null) {
				System.out.println("dailySkySnap is null ");
			}

			if ((!retroFound) && dailySkySnap.allPlanets.get(planetNum).xpSidereal[3] < 0.0) {
				retroFound = true;
				allRecords.get(counter).setSequenceNumber(1);
			} else if ((retroFound) && dailySkySnap.allPlanets.get(planetNum).xpSidereal[3] > 0.0) {
				retroFound = false;
				allRecords.get(counter).setSequenceNumber(2);
				totalFound++;
			}
		}
		System.out.println("totalFound = " + totalFound);
		return allRecords;
	}
	*/
	
	public AstroHelper(ArrayList<DailySkySnap> calendar) {
		this.calendar = calendar;

		for (int counter=0; counter<calendar.size(); counter++) {
			DailySkySnap dailySkySnap = calendar.get(counter); 
			dataMap.put(Utilities.getDisplayDate(dailySkySnap.date), dailySkySnap);

			if (counter==0) System.out.println("Utilities.getDisplayDate(dailySkySnap.date) = " + Utilities.getDisplayDate(dailySkySnap.date));
			if (counter==(calendar.size()-1)) System.out.println("Utilities.getDisplayDate(dailySkySnap.date) = " + Utilities.getDisplayDate(dailySkySnap.date));
		}
		long startMillis = System.currentTimeMillis();
		System.out.println("Calling generateRetroRecordsForAllPlanets()");
		long endMillis = System.currentTimeMillis();
		System.out.println("Done calling generateRetroRecordsForAllPlanets() " + (endMillis-startMillis));
		System.out.println("dataMap.size() = " + dataMap.size());
		//System.exit(0);
	}

	/*	
	private ArrayList<Double> getDataForAllDaysForPlanet(int planetNumber, String startingDate, int numOfDays, boolean geo, int xpIndex) {
		ArrayList<Double> degreeReadings =  new ArrayList<Double>();
		String runningDate = startingDate;
		for (int counter=0; counter< numOfDays; counter++) {
			runningDate =  Utilities.getNextDateString(runningDate);
			DailySkySnap dailySkySnap = dataMap.get(runningDate);
			if (null!=dailySkySnap) {
				double degrees = getDegrees(dailySkySnap, planetNumber, geo);
				degreeReadings.add(degrees);
			}
		}
		return degreeReadings;
	}

	public ArrayList<Double> getLongitudeDegreesForPlanet(ArrayList<RowRecord> allRows, int planetNumber, boolean geo) {
		ArrayList<Double> returnArray =  new ArrayList<Double>();
		int xpIndex = 0;
		for (int counter=0; counter<allRows.size(); counter++) {
			String runningDate = Utilities.getDisplayDate(allRows.get(counter).getDate());
			//System.out.println("runningDate = " + runningDate);
			DailySkySnap dailySkySnap = dataMap.get(runningDate);
			if (null!=dailySkySnap) {
				double degrees = getDegrees(dailySkySnap, planetNumber, geo);
				returnArray.add(degrees);
				System.out.println("degrees = " + Utilities.getDblStr(degrees));
			}
		}
		return returnArray;
	}
	
	public ArrayList<Double> getLongitudeDegreesForAllDaysForPlanet(int planetNumber, String startingDate, int numOfDays, boolean geo) {
		int xpIndex = 0;
		return getDataForAllDaysForPlanet(planetNumber, startingDate, numOfDays, geo, xpIndex);
	}

	public ArrayList<Double> getLattitudeDegreesForAllDaysForPlanet(int planetNumber, String startingDate, int numOfDays, boolean geo) {
		int xpIndex = 1;
		return getDataForAllDaysForPlanet(planetNumber, startingDate, numOfDays, geo, xpIndex);
	}

	public ArrayList<Double> getDistanceForAllDaysForPlanet(int planetNumber, String startingDate, int numOfDays, boolean geo) {
		int xpIndex = 2;
		return getDataForAllDaysForPlanet(planetNumber, startingDate, numOfDays, geo, xpIndex);
	}

	public ArrayList<Double> getLongitudeSpeedForAllDaysForPlanet(int planetNumber, String startingDate, int numOfDays, boolean geo) {
		int xpIndex = 3;
		return getDataForAllDaysForPlanet(planetNumber, startingDate, numOfDays, geo, xpIndex);
	}

	public ArrayList<Double> getLattitudeSpeedForAllDaysForPlanet(int planetNumber, String startingDate, int numOfDays, boolean geo) {
		int xpIndex = 4;
		return getDataForAllDaysForPlanet(planetNumber, startingDate, numOfDays, geo, xpIndex);
	}

/*
	private double getDegrees(DailySkySnap dailySkySnap, int planetNumber, boolean geo) {
		double degrees = 0.0;
		if (geo) {
			degrees = dailySkySnap.allPlanets.get(planetNumber).xpSidereal[0];
		} else {
			degrees = dailySkySnap.allPlanets.get(planetNumber).xpHelio[0];
		}
		return degrees;
	}
	
	public boolean areConjunct (int planetNumber1, int planetNumber2,long dateTime,boolean geo) {
		String runningDate =  Utilities.getDisplayDate(dateTime);
		DailySkySnap dailySkySnap = dataMap.get(runningDate);
		return areConjunct (planetNumber1, planetNumber2, dailySkySnap, geo);
	}

	public boolean areConjunct (int planetNumber1, int planetNumber2,DailySkySnap dailySkySnap,boolean geo) {
		if (null!=dailySkySnap) {
			double degrees1 = getDegrees(dailySkySnap, planetNumber1, geo);
			double degrees2 = getDegrees(dailySkySnap, planetNumber2, geo);
			if (Utilities.areConjunct(degrees1, degrees2, Utilities.getOrb(planetNumber1, planetNumber2))) {
				return true;
			}
		}
		return false;
	}

	public boolean isRetro (int planetNumber1, long dateTime) {
		String runningDate =  Utilities.getDisplayDate(dateTime);
		DailySkySnap dailySkySnap = dataMap.get(runningDate);
		return isRetro (planetNumber1,dailySkySnap);
	}

	public boolean isRetro(int planetNum,DailySkySnap dailySkySnap) {
		return (dailySkySnap.allPlanets.get(planetNum).xpSidereal[3] < 0.0);
	}
	
	public double getLongitudeSpeed(int planetNum,DailySkySnap dailySkySnap) {
		return (dailySkySnap.allPlanets.get(planetNum).xpSidereal[3]);
	}
	

	public ArrayList<Integer> getConjunctions (int planetNumber1, int planetNumber2, ArrayList<RowRecord> allRows, boolean geo) {
		ArrayList<Integer> conjunctions =  new ArrayList<Integer>();
		String runningDate = "";
		for (int counter=0; counter< allRows.size(); counter++) {
			runningDate =  Utilities.getDisplayDate(allRows.get(counter).getDate());
			DailySkySnap dailySkySnap = dataMap.get(runningDate);
			if (null!=dailySkySnap) {
				double degrees1 = getDegrees(dailySkySnap, planetNumber1, geo);
				double degrees2 = getDegrees(dailySkySnap, planetNumber2, geo);
				if (Utilities.areConjunct(degrees1, degrees2, Utilities.getOrb(planetNumber1, planetNumber2))) {
					conjunctions.add(counter);
				}
			}
		}
		return conjunctions;
	}


	public ArrayList<String> getOppositions(int planetNumber1, int planetNumber2, ArrayList<RowRecord> allRows, boolean geo) {
		ArrayList<String> oppositions =  new ArrayList<String>();
		String runningDate = "";
		for (int counter=0; counter < allRows.size(); counter++) {
			runningDate =  Utilities.getDisplayDate(allRows.get(counter).getDate());
			DailySkySnap dailySkySnap = dataMap.get(runningDate);
			if (null!=dailySkySnap) {
				double degrees1 = getDegrees(dailySkySnap, planetNumber1, geo);
				double degrees2 = getDegrees(dailySkySnap, planetNumber2, geo);
				if (Utilities.areOpposed(degrees1, degrees2, Utilities.getOrb(planetNumber1, planetNumber2))) {
					oppositions.add(runningDate);
				}
			}
		}
		return oppositions;
	}

	public void printDates(ArrayList<String> array) {
		for (int counter=0; counter<array.size(); counter++) {
			Utilities.logMsgLn("" + counter + " " + array.get(counter));
		}
	}

	public static String getReportLine(DailySkySnap dailySkySnap) {
		String report = "";
		double sunDegrees = dailySkySnap.allPlanets.get(planetSun).xpSidereal[0];
		double moonDegrees = dailySkySnap.allPlanets.get(planetMoon).xpSidereal[0];
		
		int sunInSign = dailySkySnap.planetInSign(planetSun);
		int moonInSign = dailySkySnap.planetInSign(planetMoon);

		int sunInSiderealSign = dailySkySnap.planetInSiderealSign(planetSun);
		int moonInSiderealSign = dailySkySnap.planetInSiderealSign(planetMoon);
		
		int sunInNakshatra = dailySkySnap.planetInNakshatra(planetSun);
		int moonInNakshatra = dailySkySnap.planetInNakshatra(planetMoon);
		
		double sunDistance = dailySkySnap.planetDistance(planetSun);
		double moonDistance = dailySkySnap.planetDistance(planetMoon);

		report = report + "\t" + "S = " + Utilities.getDblStr(sunDegrees) + "\t";
		report = report + "M = " + Utilities.getDblStr(moonDegrees) + "\t";

		report = report + "S-T = " + getSignName(sunInSign-1) + "\t";
		report = report + "M-T = " + getSignName(moonInSign-1) + "\t";

		report = report + "S-SR = " + getSignName(sunInSiderealSign-1) + "\t";
		report = report + "M-SR = " + getSignName(moonInSiderealSign-1) + "\t";
		
		report = report +  "S-N = " + getNakshatraName(sunInNakshatra-1) + "\t";
		report = report +  "M-N = " + getNakshatraName(moonInNakshatra-1) + "\t";

		report = report + "S-Dist = " + Utilities.getDblStr(sunDistance*auToKm) + "\t";
		report = report + "M-Dist = " + Utilities.getDblStr(moonDistance*auToKm) + "\t";
		
		return report;
	}
	
	public static String getAspectPrefix(int aspectNumber) {
		switch (aspectNumber) {
			case 0: return "Cnj\t";
			case 1: return "A30\t";
			case 2: return "A60\t";
			case 3: return "Sqr\t";
			case 4: return "Tri\t";
			case 5: return "Opp\t";
		}
		return "";
	}
	
	public static String getNMFMAspectPrefix(int aspectNumber) {
		if (aspectNumber==0) {
			return "NM\t";
		} 
		return "FM\t";
	}

*/

	
/*	
	public static void main(String[] args) {
	//getDistanceForAllDaysForPlanet
		//generateVoidOfPlanetCalendar(0);
		//if (true) return;
		
		SkySnapShot skySnapShot =  new SkySnapShot();
		String fileName = System.getProperty("user.home") + System.getProperty("file.separator") + 
				AllConfig.configFolder + "calendar" + System.getProperty("file.separator");         
		
		Utilities.logMsgLn(fileName);
		
		FileWriter fWriterNMorFM = Utilities.openFile(fileName + "NMorFM.csv");
		FileWriter fWriterMoonAspects = Utilities.openFile(fileName + "moonAspects.csv");
		FileWriter fWriterAll = Utilities.openFile(fileName + "allPlanetsAspects.csv");
		

		long millis = Utilities.getTimeInMillisDateAndTime("1900-01-01:10:30");
		SkipMaster skipMaster = new SkipMaster();
		
		for (int counter1= 0; counter1<planetNames.length-2; counter1++) {
			for (int counter2= counter1+1; counter2<planetNames.length-1; counter2++) {
				skipMaster.createEntry(""+counter1+"-"+counter2,millis,waitTimeAfterAnAspect);		
			}
		}
		
		String report = "";

		int maxDays = 10000;
		String startingDateStr = "2001-01-01";
		String dateString = startingDateStr;
		
		for (int dayCounter=0; dayCounter < maxDays; dayCounter++) {
			dateString = Utilities.getNextDateString(dateString); 
			Utilities.logMsgLn("dayCounter = " + dayCounter);
			for (int hourCounter=0; hourCounter < 24; hourCounter++) {
				String dateTime;
				DailySkySnap dailySkySnap = generateGeoAndHelioCalendar(skySnapShot,dateString, hourCounter);
				long aspectTime = Utilities.getTimeInMillis(dateString);
				
				for (int aspectCounter=0; aspectCounter < nmFMaspectArray.length; aspectCounter++) {
					if (dailySkySnap.areAtAngle(nmFMaspectArray[aspectCounter],planetSun,planetMoon, Utilities.getOrbForAnHour(planetSun,planetMoon))) {
						if (!skipMaster.okToProceed(""+planetSun+"-"+planetMoon, aspectTime)) {
							continue;
						} else {
							skipMaster.updateEntry(""+planetSun+"-"+planetMoon, aspectTime,waitTimeAfterAnAspect);
							dateTime = Utilities.getDisplayDateTime(dailySkySnap.getDate());
							report = getNMFMAspectPrefix(aspectCounter) + dateTime + getReportLine(dailySkySnap);
							Utilities.writeLine(fWriterNMorFM,report);
						}
					}
				}
				
				for (int aspectCounter=0; aspectCounter < aspectArray.length; aspectCounter++) {
					if (dailySkySnap.areAtAngle(aspectArray[aspectCounter],planetSun,planetMoon, Utilities.getOrbForAnHour(planetSun,planetMoon))) {
						if (!skipMaster.okToProceed(""+planetSun+"-"+planetMoon, aspectTime)) {
							continue;
						} else {
							skipMaster.updateEntry(""+planetSun+"-"+planetMoon, aspectTime,waitTimeAfterAnAspect);
							dateTime = Utilities.getDisplayDateTime(dailySkySnap.getDate());
							report = getAspectPrefix(aspectCounter) + dateTime + getReportLine(dailySkySnap);
							Utilities.writeLine(fWriterMoonAspects,report);
						}
					}
				}
				
				for (int pCounter1=0; pCounter1 < planetNames.length-2; pCounter1++) {
					for (int pCounter2=pCounter1+1; pCounter2 < planetNames.length-1; pCounter2++) {
						dateTime = Utilities.getDisplayDateTime(dailySkySnap.getDate());

						for (int aspectCounter=0; aspectCounter < 6; aspectCounter++) {
							if (dailySkySnap.areAtAngle(aspectArray[aspectCounter],pCounter1,pCounter2, Utilities.getOrbForAnHour(pCounter1,pCounter2))) {
								if (!skipMaster.okToProceed(""+pCounter1+"-"+pCounter2, aspectTime)) {
									continue;
								} else {
									skipMaster.updateEntry(""+pCounter1+"-"+pCounter2, aspectTime,waitTimeAfterAnAspect);
									dateTime = Utilities.getDisplayDateTime(dailySkySnap.getDate());

									report = getAspectPrefix(aspectCounter) + "=" + dateTime + "," + getPlanetName(pCounter1) + "=" + Utilities.getDblStr(dailySkySnap.getDegrees(pCounter1)) + 
											"," + getPlanetName(pCounter2) + "=" + Utilities.getDblStr(dailySkySnap.getDegrees(pCounter2));
									Utilities.writeLine(fWriterAll,report);
								}
							}
						}
					}
				}
			}
		}

		Utilities.closeFile(fWriterMoonAspects);
		Utilities.closeFile(fWriterAll);
		Utilities.closeFile(fWriterNMorFM);
	}
	
*/
	
}

