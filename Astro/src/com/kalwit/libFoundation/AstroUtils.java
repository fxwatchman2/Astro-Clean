package com.kalwit.libFoundation;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import swisseph.SweConst;
import swisseph.SweDate;

public class AstroUtils {
	static int marsDrishti[] = {3,7};
	static int jupiterDrishti[] = {4,8};
	static int saturnDrishti[] = {2,9};

	static Map<Integer,ArrayList<Integer>> rashiPlanets = new HashMap<Integer,ArrayList<Integer>>();
	static Map<Integer,ArrayList<Integer>> drishtis = new HashMap<Integer,ArrayList<Integer>>();

	static int[][] nakshatraLordArray = {
								{1,11}, // treat this as Ketu
								{2,SweConst.SE_VENUS},
								{3,SweConst.SE_SUN},
								{4,SweConst.SE_VENUS},
								{5,SweConst.SE_MOON},
								{6,SweConst.SE_MARS},
								{7,SweConst.SE_TRUE_NODE},
								{8,SweConst.SE_JUPITER},
								{9,SweConst.SE_SATURN},
								{10,SweConst.SE_MERCURY},
								{11,11},
								{12,SweConst.SE_VENUS},
								{13,SweConst.SE_SUN},
								{14,SweConst.SE_VENUS},
								{15,SweConst.SE_MOON},
								{16,SweConst.SE_MARS},
								{17,SweConst.SE_TRUE_NODE},
								{18,SweConst.SE_JUPITER},
								{19,SweConst.SE_SATURN},
								{20,SweConst.SE_MERCURY},
								{21,11},
								{22,SweConst.SE_VENUS},
								{23,SweConst.SE_SUN},
								{24,SweConst.SE_VENUS},
								{25,SweConst.SE_MOON},
								{26,SweConst.SE_MARS},
								{27,SweConst.SE_TRUE_NODE}
							};

	static int traditionalRashiLords[] = {SweConst.SE_MARS,
			SweConst.SE_MOON,
			SweConst.SE_MERCURY,
			SweConst.SE_MOON,
			SweConst.SE_SUN, 
			SweConst.SE_MERCURY, 
			SweConst.SE_VENUS,
			SweConst.SE_MARS,
			SweConst.SE_JUPITER,
			SweConst.SE_SATURN,
			SweConst.SE_SATURN,
			SweConst.SE_JUPITER};
	
	static int traditionalNakshatraLords[] = {11,
			SweConst.SE_VENUS,
			SweConst.SE_SUN,
			SweConst.SE_VENUS,
			SweConst.SE_MOON,
			SweConst.SE_MARS,
			SweConst.SE_TRUE_NODE,
			SweConst.SE_JUPITER,
			SweConst.SE_SATURN,
			SweConst.SE_MERCURY
		};
	
	static int[][] rashiLordArray = {
			{5,SweConst.SE_SUN},
			{4,SweConst.SE_MOON},
			{3,SweConst.SE_MERCURY},
			{6,SweConst.SE_MERCURY},
			{2,SweConst.SE_VENUS},
			{7,SweConst.SE_VENUS},
			{1,SweConst.SE_MARS},
			{8,SweConst.SE_MARS},
			{9,SweConst.SE_TRUE_NODE},
			{12,SweConst.SE_TRUE_NODE},
			{10,SweConst.SE_SATURN},
			{11,SweConst.SE_SATURN},
			{6,SweConst.SE_TRUE_NODE},
			{12,12}, // Ketu
			{1,SweConst.SE_URANUS},
			{7,SweConst.SE_NEPTUNE},
		};

	static String[] descriptions = {"Mesh/Aries : Strong Bullish",
									"Vrishabh/Taurus: Bullish but gradual",
									"Mithus/Gemini: Bearish with suddenness",
									"Kark/Cancer: Strong Bullish",
									"Simha/Leo: Strong Bullish",
									"Kanya/Virgo: Moderate fall",
									"Tula/Libra: Bearishness with sudden rise",
									"Vrishchik/Scorpio: Bullish but gradual",
									"Dhanu/Sagittarius: Bullish , Upsetting rise/fall",
									"Makar/Capricorn: Moderate bullish, quick changes",
									"Kumbh/Aquarius: Bearish Tren setter",
									"Meen/Pisces: Bearish but gradual"};


	public static String getRashiPlanets(int rashi) {
		String str = "(";
		ArrayList<Integer> planetList = rashiPlanets.get(rashi);
		for (int counter=0; counter<planetList.size(); counter++) {
			int planet = planetList.get(counter);
			str = str + Conversions.getPlanetName(planet) + ",";
		}
		
		if (str.length() > 1) str = str.substring(0, str.length()-1);
		str = str + ")";
		
		return str;
	}

	public static void printKundali() {
		for (int rashi=0; rashi<12 ; rashi++) {
			System.out.print(rashi+1 + "\t");
			System.out.print(Conversions.getRashiName(rashi) + "\t");
			ArrayList<Integer> planetList = rashiPlanets.get(rashi);
			for (int counter=0; counter<planetList.size(); counter++) {
				int planet = planetList.get(counter);
				System.out.print(Conversions.getPlanetName(planet) + " ");
			}
			System.out.print("\t\t");
			planetList = drishtis.get(rashi);
			for (int counter=0; counter<planetList.size(); counter++) {
				int planet = planetList.get(counter);
				System.out.print(Conversions.getPlanetName(planet) + " ");
			}
			System.out.println();
		}
	}
	
	public static void printPlanetDetails(long millis) {
		System.out.print("Planet" + "\t\t");
		System.out.print("Longi" + "\t\t");
		System.out.print("Speed" + "\t\t");
		System.out.print("Decli" + "\t\t");
		System.out.print("Dist" + "\t\t");
		System.out.print("Navamsh");
		System.out.println();
		System.out.println("---------------------------------------------------------------------------");
		System.out.println();
		for (int counter=0; counter<11 ; counter++) {
			System.out.print(Conversions.getPlanetName(counter) + "\t\t");
			System.out.print(getDetails(millis,counter));
			System.out.println();
		}
		System.out.println();
	}
	
	public static void printKundali_old() {
		for (int rashi=0; rashi<12 ; rashi++) {
			System.out.print(rashi+1 + "\t");
		}
		System.out.println();
		for (int rashi=0; rashi<12 ; rashi++) {
			System.out.print(Conversions.getRashiName(rashi) + "\t");
		}
		System.out.println();
		for (int rashi=0; rashi<12 ; rashi++) {
			ArrayList<Integer> planetList = rashiPlanets.get(rashi);
			for (int counter=0; counter<planetList.size(); counter++) {
				int planet = planetList.get(counter);
				System.out.print(Conversions.getPlanetName(planet) + " ");
			}
			System.out.print("\t");
		}
		System.out.println();
	}
	
	public static void printDrishtis_old() {
		for (int rashi=0; rashi<12 ; rashi++) {
			ArrayList<Integer> planetList = drishtis.get(rashi);
			System.out.print(Conversions.getRashiName(rashi) + "\t" + getRashiPlanets(rashi) + "\t\t seen by ");
			for (int counter=0; counter<planetList.size(); counter++) {
				int planet = planetList.get(counter);
				System.out.print(Conversions.getPlanetName(planet));
				if (counter< (planetList.size()-1)) System.out.print(",");
			}
			System.out.println();
		}
	}
	
	public static void printDrishtis() {
		for (int rashi=0; rashi<6 ; rashi++) {
			ArrayList<Integer> planetList = drishtis.get(rashi);
			for (int counter=0; counter<planetList.size(); counter++) {
				int planet = planetList.get(counter);
				System.out.print(Conversions.getPlanetName(planet) + " ");
			}
			System.out.print("\t\t");
		}
		System.out.println();
		for (int rashi=6; rashi<12 ; rashi++) {
			ArrayList<Integer> planetList = drishtis.get(rashi);
			for (int counter=0; counter<planetList.size(); counter++) {
				int planet = planetList.get(counter);
				System.out.print(Conversions.getPlanetName(planet) + " ");
			}
			System.out.print("\t\t");
		}
		System.out.println();
	}
	
	public static void initMapForTheDay(long millis) {
		for (int rashi=0; rashi < 12; rashi++) {
			rashiPlanets.put(rashi, new ArrayList<Integer>()); 	// this is kalpurush Kundli of the day
			drishtis.put(rashi, new ArrayList<Integer>());		// This is a list of which rashi has aspects of which planets 
		}
	
		for (int planet=0; planet <SweConst.SE_TRUE_NODE; planet++) {
			double longi = getSiderealLongitude(millis, planet);
			int rashi = getRashi(millis,planet);
			ArrayList<Integer> planetList = rashiPlanets.get(rashi);
			planetList.add(planet);
			rashiPlanets.replace(rashi, planetList);
		}

		for (int rashi=0; rashi<12 ; rashi++) {
			ArrayList<Integer> planetList = rashiPlanets.get(rashi);
			for (int counter=0; counter<planetList.size(); counter++) {
				int planet = planetList.get(counter);
				int drishtiRashi = getSeventhDrishtiSign(rashi);
				ArrayList<Integer> allDrishties = drishtis.get(drishtiRashi);
				allDrishties.add(planet);
				if (planet == SweConst.SE_MARS) {
					drishtiRashi = getMarsDrishtiSign(rashi, 0);
					allDrishties = drishtis.get(drishtiRashi);
					allDrishties.add(planet);
					drishtiRashi = getMarsDrishtiSign(rashi, 1);
					allDrishties = drishtis.get(drishtiRashi);
					allDrishties.add(planet);
				}
				if (planet == SweConst.SE_JUPITER) {
					drishtiRashi = getJupiterDrishtiSign(rashi, 0); 
					allDrishties = drishtis.get(drishtiRashi);
					allDrishties.add(planet);
					drishtiRashi = getJupiterDrishtiSign(rashi, 1); 
					allDrishties = drishtis.get(drishtiRashi);
					allDrishties.add(planet);
				}
				if (planet == SweConst.SE_SATURN) {
					drishtiRashi = getSaturnDrishtiSign(rashi, 0); 
					allDrishties = drishtis.get(drishtiRashi);
					allDrishties.add(planet);
					drishtiRashi = getSaturnDrishtiSign(rashi, 1); 
					allDrishties = drishtis.get(drishtiRashi);
					allDrishties.add(planet);
				}
			}
		}
	}
	
	// zero based
	public static String getPlanetSignComment(int planet, int sign) {
		String comment = "";
		
		// SUN
		if ((planet==SweConst.SE_SUN) && (sign==5)) {
			comment = "Sun in it's swarashi(Leo)";
		}
		
		if ((planet==SweConst.SE_SUN) && (sign==1)) {
			comment = "Sun in it's Uchch rashi(Mesh)";
		}
		
		
		// MOON
		if ((planet==SweConst.SE_MOON) && (sign==4)) {
			comment = "Moon in it's swarashi(Leo)";
		}
		
		if ((planet==SweConst.SE_MOON) && (sign==2)) {
			comment = "Moon in it's Uchch rashi (Vrishabh)";
		}
		
		// MARS
		if ((planet==SweConst.SE_MARS) && (sign==1)) {
			comment = "Mars in it's swarashi(Mesh)";
		}
		
		if ((planet==SweConst.SE_MARS) && (sign==8)) {
			comment = "Mars in it's swarashi(Vrishchik)";
		}
		
		if ((planet==SweConst.SE_MARS) && (sign==10)) {
			comment = "Mars in it's Uchch rashi (Makar)";
		}
		
		// MERCURY
		if ((planet==SweConst.SE_MERCURY) && (sign==3)) {
			comment = "Mercury in it's swarashi(Mesh)";
		}
		
		if ((planet==SweConst.SE_MERCURY) && (sign==6)) {
			comment = "Mercury in it's Uchch and swarashi(Kanya)";
		}
		
		
		// JUPITER
		if ((planet==SweConst.SE_JUPITER) && (sign==9)) {
			comment = "Jupiter in it's swarashi(Dhanu)";
		}
		
		if ((planet==SweConst.SE_JUPITER) && (sign==12)) {
			comment = "Jupiter in it's swarashi(Meen)";
		}
		
		if ((planet==SweConst.SE_JUPITER) && (sign==4)) {
			comment = "Jupiter in it's Uchch rashi (Vrishchik)";
		}
		
		// VENUS
		if ((planet==SweConst.SE_VENUS) && (sign==2)) {
			comment = "Venus in it's swarashi(Vrishabh)";
		}
		
		if ((planet==SweConst.SE_VENUS) && (sign==7)) {
			comment = "Venus in it's swarashi(Tula)";
		}
		
		if ((planet==SweConst.SE_VENUS) && (sign==4)) {
			comment = "Venus in it's Uchch rashi (Kark)";
		}
		
		// SATURN
		if ((planet==SweConst.SE_SATURN) && (sign==10)) {
			comment = "Saturn in it's swarashi(Makar)";
		}
		
		if ((planet==SweConst.SE_SATURN) && (sign==11)) {
			comment = "Saturn in it's swarashi(Kumbh)";
		}
		
		if ((planet==SweConst.SE_SATURN) && (sign==7)) {
			comment = "Saturn in it's Uchch rashi (Tula)";
		}
		
		// RAHU
		if ((planet==SweConst.SE_TRUE_NODE) && (sign==6)) {
			comment = "Rahu in it's swarashi(Kanya)";
		}
		
		if ((planet==SweConst.SE_TRUE_NODE) && (sign==4)) {
			comment = "Rahu in it's Mool Trokon(Kark)";
		}
		
		if ((planet==SweConst.SE_TRUE_NODE) && (sign==3)) {
			comment = "Rahu in it's Uchch rashi (Mithun)";
		}
		
		// URANUS
		if ((planet==SweConst.SE_URANUS) && (sign==1)) {
			comment = "Uranus in it's swarashi(Mesh)";
		}
		
		if ((planet==SweConst.SE_URANUS) && (sign==9)) {
			comment = "Uranus in it's Mool Trokon(Dhanu)";
		}
		
		if ((planet==SweConst.SE_URANUS) && (sign==5)) {
			comment = "Uranus in it's Uchch rashi (Simha)";
		}
		
		// NEPTUNE
		if ((planet==SweConst.SE_NEPTUNE) && (sign==7)) {
			comment = "Neptune in it's swarashi(Tula)";
		}
		
		if ((planet==SweConst.SE_NEPTUNE) && (sign==4)) {
			comment = "Neptune in it's Mool Trokon(Kark)";
		}
		
		if ((planet==SweConst.SE_NEPTUNE) && (sign==12)) {
			comment = "Neptune in it's Uchch rashi (Meen)";
		}
		return comment;
	}

	
	public static int getSeventhDrishtiSign(int sign) {
		int drishti = (sign + 6)%12; 
		return drishti;
	}
	
	public static int getMarsDrishtiSign(int marsSign, int index) {
		index = index%2;
		int drishti= (marsSign + marsDrishti[index])%12; 
		return drishti;
	}
	
	public static int getJupiterDrishtiSign(int jupiterSign, int index) {
		index = index%2;
		int drishti = (jupiterSign + jupiterDrishti[index])%12; 
		return drishti;
	}
	
	public static int getSaturnDrishtiSign(int saturnSign, int index) {
		index = index%2;
		int drishti = (saturnSign + saturnDrishti[index])%12; 
		return drishti;
	}
	
	public static String getDetails(long date, int planet) {
		String details = "";
		SweDate sd = Conversions.getSweDate(date);
		double longi = SwissHelper.getSiderealLongitude(sd, planet);
		double decli = SwissHelper.getDeclination(sd, planet);
		double distance = SwissHelper.getDistance(sd, planet) * Conversions.AU2MillionMiles;
		double speed = SwissHelper.getSpeed(sd, planet);
		double percentDistance = distance*100/SwissHelper.maxPossibleDistances[planet];	
		double percentDecli = decli*100/SwissHelper.maxPossibleDeclinations[planet];
		
		details = details + Conversions.getDblStrWithDecimal(longi) + "\t\t";
		details = details + Conversions.getDblStrWithDecimal(speed) + "\t\t";
		details = details + Conversions.getDblStrWithDecimal(percentDecli) + "%\t\t";
		details = details + Conversions.getDblStrWithDecimal(percentDistance) + "%\t\t";
		int navam = getNavamsha(date, planet);
		details = details + Conversions.getRashiName(navam); 
		return details;
	}
	
	public static double getSiderealLongitude(long date, int planet) {
		SweDate sd = Conversions.getSweDate(date);
		double returnValue = SwissHelper.getSiderealLongitude(sd, planet);
		return returnValue;
	}

	public static double getDeclination(long date, int planet) {
		SweDate sd = Conversions.getSweDate(date);
		double returnValue = SwissHelper.getDeclination(sd, planet);
		return returnValue;
	}
	
	public static boolean isDirect(long date, int planet) {
		SweDate sd = Conversions.getSweDate(date);
		double returnValue = SwissHelper.getSpeed(sd, planet);
		return returnValue > 0.00;
	}
	
	public static boolean isStation(long date, int planet) {
		SweDate sd = Conversions.getSweDate(date);
		double returnValue = SwissHelper.getSpeed(sd, planet);
		return returnValue == 0.00;
	}
	
	public static boolean isRetro(long date, int planet) {
		SweDate sd = Conversions.getSweDate(date);
		double returnValue = SwissHelper.getSpeed(sd, planet);
		return returnValue < 0.00;
	}
	
	// D D D D D D D D D R R R R R R R R R R R D D D D
	// No planets are retro within 180 days from any non-retro day in a year
	public static int retroForHowManyDays(long date, int planet) {
		int returnValue = 0;
		long retroDate = 0;
		for (int i=0; i < 180; i++) {
			long newDate = date - ((long)(i) * Conversions.millisInADay);
			long previousDate = newDate - ((long)(i) * Conversions.millisInADay);
			if ((isRetro(newDate, planet)) && (!isRetro(previousDate, planet))) {
				retroDate = newDate;
			}
		}
		returnValue = (int) ((retroDate-date)/Conversions.millisInADay); 
		return returnValue;
	}

	public static double getDiff(double d1, double d2) {
		double diff = Math.abs(d1 - d2);
		if (diff > 180) diff = 360 - diff;
		return diff;
	}
	
	// Used to find Moon and Sun at 90, 180 and 270 degrees
	public static boolean isMoonAtDegrees(long date, double degrees) {
		double longiMoon = getSiderealLongitude(date, SweConst.SE_MOON);
		double longiSun = getSiderealLongitude(date, SweConst.SE_SUN);
		
		double target = longiSun + degrees;
		if (target > 360) target = target - 360;
		
		SweDate sd = Conversions.getSweDate(date);
		double speedMoon = SwissHelper.getSpeed(sd, SweConst.SE_MOON);
		
		if ((longiMoon > (target-speedMoon)) &&  (longiMoon <= target)) {
			return true;
		}
		return false;
	}
	
    public static boolean isPlanetFollowingSun(double dPlanet, double dSun) {
    	if (Math.abs(dPlanet-dSun) > 180) {
    		if (dPlanet < 60) return false;
    		if (dSun < 60) return true;
    	} 
    	return dPlanet < dSun;
    }

    public static boolean isP1FollowingP2(long date, int p1, int p2) {
		double longiP1 = getSiderealLongitude(date, p1);
		double longiP2 = getSiderealLongitude(date, p2);
    	
		if (Math.abs(longiP1-longiP2) > 180) {
    		if (longiP1 < 60) return false;
    		if (longiP2 < 60) return true;
    	} 
    	return longiP1 < longiP2;
    }

	public static boolean isRetroEasterly(long date, int planet) {
		if (!isRetro(date, planet)) {
			return false;
		} else {
			double longiPlanet = getSiderealLongitude(date, planet);
			double longiSun = getSiderealLongitude(date, SweConst.SE_SUN);
			return isPlanetFollowingSun(longiPlanet,longiSun);
		}
	}
	
	public static boolean isRetroWesterly(long date, int planet) {
		if (!isRetro(date, planet)) {
			return false;
		} else {
			double longiPlanet = getSiderealLongitude(date, planet);
			double longiSun = getSiderealLongitude(date, SweConst.SE_SUN);
			return !isPlanetFollowingSun(longiPlanet,longiSun);
		}
	}

	// all zero Based
	public static int getRashi(long date, int planet) {
		int returnValue;
		double divisor = 30.0;
		SweDate sd = Conversions.getSweDate(date);
		double longi = SwissHelper.getSiderealLongitude(sd, planet);
		returnValue = (int) (longi / divisor);
		return returnValue;
	}
	
	public static String getRashiName(long date, int planet) {
		int rashi = getRashi(date, planet);
		String rashiName = Conversions.getSignName(rashi);
		return rashiName;
	}
	
	public static int getRashiLord(int rashi) {
		return traditionalRashiLords[rashi];
	}
	
	public static int getNakshatraLord(int nakshatra) {
		return traditionalNakshatraLords[nakshatra%10];
	}
	
	public static int getSunRashiLord(long date) {
		int rashi = getRashi(date, SweConst.SE_SUN);
		return traditionalRashiLords[rashi];
	}
	
	public static int getMoonRashiLord(long date) {
		int rashi = getRashi(date, SweConst.SE_MOON);
		return traditionalRashiLords[rashi];
	}
	
	// Many rashis have two lords
	public static int getFirstRashiLord(long date, int planet) {
		int rashi = getRashi(date, planet);
		for (int counter=0; counter<rashiLordArray.length; counter++) {
			if (rashiLordArray[counter][0] == rashi) {
				return rashiLordArray[counter][1];
			}
		}
		return -1;
	}
	
	public static int getSecondRashiLord(long date, int planet) {
		int rashi = getRashi(date, planet);
		boolean firstFound = false;
		for (int counter=0; counter<rashiLordArray.length; counter++) {
			if (rashiLordArray[counter][0] == rashi) {
				if (firstFound) {
					return rashiLordArray[counter][1];
				} else {
					firstFound = true;
				}
			}
		}
		return -1;
	}
	

	public static String getNakshatraLord(long date, int planet) {
		int nakshatra = getNakshatra(date, planet);
		String nakshatraName = Conversions.getNakshatraName(nakshatra);
		return nakshatraName;
	}
	
	public static String getNakshatraName(long date, int planet) {
		int nakshatra = getNakshatra(date, planet);
		String nakshatraName = Conversions.getNakshatraName(nakshatra);
		return nakshatraName;
	}
	
	public static int getNakshatra(long date, int planet) {
		int returnValue;
		double divisor = 13.333;
		SweDate sd = Conversions.getSweDate(date);
		double longi = SwissHelper.getSiderealLongitude(sd, planet);
		returnValue = (int) (longi / divisor);
		return returnValue;
	}
	
	public static int getCharan(long date, int planet) {
		int returnValue;
		double divisor = 3.333;
		SweDate sd = Conversions.getSweDate(date);
		double longi = SwissHelper.getSiderealLongitude(sd, planet);
		returnValue = (int) (longi / divisor);
		return returnValue;
	}
	
	public static int getNavamsha(long date, int planet) {
		SweDate sdCurrDate = Conversions.getSweDate(date);
		double longi = SwissHelper.getSiderealLongitude(sdCurrDate, planet);
		int navRashiNum = Conversions.getZeroBasedNavamansh(longi);
		return navRashiNum;
	}

	public static boolean isMeAccelerating(long date) {
		long yesterday  = date - Conversions.millisInADay;
		SweDate sdYesterday = Conversions.getSweDate(yesterday );
		double speedMeYesterday = Math.abs(SwissHelper.getSpeed(sdYesterday,SweConst.SE_MERCURY)); 

		SweDate sd = Conversions.getSweDate(date);
		double speedMeNow = Math.abs(SwissHelper.getSpeed(sd,SweConst.SE_MERCURY)); 
		
		return ( (speedMeNow > speedMeYesterday) && (speedMeNow > SwissHelper.mercuryAvgSpeed)); 
	}

	public static boolean isMeDeccelerating(long date) {
		long yesterday  = date - Conversions.millisInADay;
		SweDate sdYesterday = Conversions.getSweDate(yesterday );
		double speedMeYesterday = Math.abs(SwissHelper.getSpeed(sdYesterday,SweConst.SE_MERCURY)); 

		SweDate sd = Conversions.getSweDate(date);
		double speedMeNow = Math.abs(SwissHelper.getSpeed(sd,SweConst.SE_MERCURY)); 
		boolean reallyDecelerating = (speedMeNow < speedMeYesterday) && (speedMeNow > SwissHelper.mercuryAvgSpeed);
		return reallyDecelerating; 
	}
	
	public static boolean isMeSlowingDown(long date) {
		long yesterday  = date - Conversions.millisInADay;
		SweDate sdYesterday = Conversions.getSweDate(yesterday );
		double speedMeYesterday = Math.abs(SwissHelper.getSpeed(sdYesterday,SweConst.SE_MERCURY)); 

		SweDate sd = Conversions.getSweDate(date);
		double speedMeNow = Math.abs(SwissHelper.getSpeed(sd,SweConst.SE_MERCURY)); 
		
		return speedMeNow < speedMeYesterday; 
	}
	
	public static boolean isVeSlowingDown(long date) {
		long yesterday  = date - Conversions.millisInADay;
		SweDate sdYesterday = Conversions.getSweDate(yesterday );
		double speedMeYesterday = Math.abs(SwissHelper.getSpeed(sdYesterday,SweConst.SE_VENUS)); 

		SweDate sd = Conversions.getSweDate(date);
		double speedMeNow = Math.abs(SwissHelper.getSpeed(sd,SweConst.SE_VENUS)); 
		
		return speedMeNow < speedMeYesterday; 
	}
	
	public static boolean isVeAccelerating(long date) {
		long yesterday  = date - Conversions.millisInADay;
		SweDate sdYesterday = Conversions.getSweDate(yesterday );
		double speedVeYesterday = Math.abs(SwissHelper.getSpeed(sdYesterday,SweConst.SE_VENUS)); 

		SweDate sd = Conversions.getSweDate(date);
		double speedVeNow = Math.abs(SwissHelper.getSpeed(sd,SweConst.SE_VENUS)); 
		 
		return ( (speedVeNow > speedVeYesterday) && (speedVeNow > SwissHelper.venusAvgSpeed)); 
	}

	public static boolean isVeDeccelerating(long date) {
		long yesterday  = date - Conversions.millisInADay;
		SweDate sdYesterday = Conversions.getSweDate(yesterday );
		double speedVeYesterday = Math.abs(SwissHelper.getSpeed(sdYesterday,SweConst.SE_VENUS)); 

		SweDate sd = Conversions.getSweDate(date);
		double speedVeNow = Math.abs(SwissHelper.getSpeed(sd,SweConst.SE_VENUS)); 
		
		return ( (speedVeNow < speedVeYesterday)); 
	}
	
	public static boolean isMeAcceleratingAndDirect(long date) {
		boolean isMeDirect = isDirect(date,SweConst.SE_MERCURY);
		boolean isMeAccelerating = isMeAccelerating(date);
		return (isMeAccelerating && isMeDirect);
	}
	
	public static boolean isMeAcceleratingAndRetro(long date) {
		boolean isMeAccelerating = isMeAccelerating(date);
		boolean isMeRetro = isRetro(date,SweConst.SE_MERCURY); 
		return (isMeAccelerating && isMeRetro); 
	}
	
	public static boolean isVeAcceleratingAndDirect(long date) {
		boolean isVeAccelerating = isVeAccelerating(date);
		boolean isVeDirect = isDirect(date,SweConst.SE_VENUS); 
		return (isVeAccelerating && isVeDirect); 
	}
	
	public static boolean isVeAcceleratingAndRetro(long date) {
		boolean isVeAccelerating = isVeAccelerating(date);
		boolean isVeRetro = isRetro(date,SweConst.SE_VENUS); 
		return (isVeAccelerating && isVeRetro); 
	}
	
	public static boolean isMeDecceleratingAndRetro(long date, int planet) {
		boolean isMeDecellerating = isMeDeccelerating(date); 
		boolean isMeRetro = isRetro(date,SweConst.SE_MERCURY); 
		return (isMeDecellerating&& isMeRetro); 
	}
	
	public static boolean isVeDecceleratingAndRetro(long date, int planet) {
		boolean isVeDeccelerating = isVeDeccelerating(date);
		boolean isVeRetro = isRetro(date,SweConst.SE_VENUS); 
		return (isVeDeccelerating && isVeRetro); 
	}
	
	public static boolean isMeFasterThanVe(long date) {
		SweDate sd = Conversions.getSweDate(date);
		double speedMe = SwissHelper.getSpeed(sd,SweConst.SE_MERCURY);
		double speedVe = SwissHelper.getSpeed(sd,SweConst.SE_VENUS);
		return speedMe > speedVe;
	}
	
	public static boolean isVeFasterThanMe(long date) {
		SweDate sd = Conversions.getSweDate(date);
		double speedMe = SwissHelper.getSpeed(sd,SweConst.SE_MERCURY);
		double speedVe = SwissHelper.getSpeed(sd,SweConst.SE_VENUS);
		return speedVe > speedMe;
	}
	
	public static boolean areInRashi(long date, int planet1, int planet2, int rashi) {
		int r1 = getRashi(date, planet1);	
		int r2 = getRashi(date, planet2);
		return ((r1==r2) && (r1==rashi));
	}
	
	public static boolean areInNakshatra(long date, int planet1, int planet2, int nakshatra) {
		int n1 = getNakshatra(date, planet1);	
		int n2 = getNakshatra(date, planet2);
		return ((n1==n2) && (n1==nakshatra));
	}
	
	public static boolean areInCharan(long date, int planet1, int planet2, int nakshatra, int charan) {
		if (areInNakshatra(date, planet1, planet2,nakshatra)) {
			int ch1 = getCharan(date,planet1);
			int ch2 = getCharan(date,planet2);
			return (ch1==ch2);
		}
		return false;
	}

	public static boolean areInNavamsha(long date, int planet1, int planet2, int rashi) {
		int n1 = getNavamsha(date, planet1);	
		int n2 = getNavamsha(date, planet2);
		return ((n1==n2) && (n1==rashi));
	}
	
	
	public static boolean areInSameRashi(long date, int planet1, int planet2) {
		int r1 = getRashi(date, planet1);	
		int r2 = getRashi(date, planet2);
		return r1==r2;
	}
	
	public static boolean areInSameNakshatra(long date, int planet1, int planet2) {
		int n1 = getNakshatra(date, planet1);	
		int n2 = getNakshatra(date, planet2);
		return n1==n2;
	}
	
	public static boolean areInSameNavamsha(long date, int planet1, int planet2) {
		int n1 = getNavamsha(date, planet1);
		int n2 = getNavamsha(date, planet2);
		return n1==n2;
	}

	public static double getMercVenusSeparation(long date) {
		double sunLongi = getSiderealLongitude(date, SweConst.SE_SUN);
		double mercLongi = getSiderealLongitude(date, SweConst.SE_MERCURY);
		double venusLongi = getSiderealLongitude(date, SweConst.SE_VENUS);
		double diff = getDiff(mercLongi, venusLongi);
		return diff;
	}
	
	public static boolean isPlanetSettingInWest(long date, int planet) {
		boolean isSetting = false;
		boolean isInWest = false;
		boolean isRetro = false;

		double sunLongi = getSiderealLongitude(date, SweConst.SE_SUN);
		double planetLongi = getSiderealLongitude(date, planet);
		double diff = getDiff(planetLongi, sunLongi); 		
    	
    	isSetting = diff < SwissHelper.astangatAngularDistance[planet];
    	isInWest = !isPlanetFollowingSun(planetLongi,sunLongi); 
    	isRetro = isRetro(date, planet);
    	
    	// Must be leading the  Sun, going into Astangat degree and retro
		return isSetting && isInWest && isRetro;
	}

	public static boolean isPlanetSettingInEast(long date, int planet) {
		boolean isSetting = false;
		boolean isInEast = false;
		boolean isRetro = false;

		double sunLongi = getSiderealLongitude(date, SweConst.SE_SUN);
		double planetLongi = getSiderealLongitude(date, planet);
		double diff = getDiff(planetLongi,sunLongi); 		

    	isSetting = diff < SwissHelper.astangatAngularDistance[planet];
    	isInEast = isPlanetFollowingSun(planetLongi,sunLongi); 
    	isRetro = isRetro(date, planet);
    	
    	// Must be following the  Sun, going into Astangat degree and NOT retro
		return isSetting && isInEast && !isRetro; 
	}

	public static void getRetroUpdate(long currMillis) {
		if (isRetro(currMillis,SweConst.SE_MERCURY) ) {
			System.out.println("Merc is Retro");
		} else if (isStation(currMillis,SweConst.SE_MERCURY)) {
			System.out.println("Merc is station");
		}
		
		if (isRetro(currMillis,SweConst.SE_VENUS) ) {
			System.out.println("Venus is Retro");
		} else if (isStation(currMillis,SweConst.SE_VENUS)) {
			System.out.println("Venus is station");
		}
		
		if (isRetro(currMillis,SweConst.SE_MARS) ) {
			System.out.println("Mars is Retro");
		} else if (isStation(currMillis,SweConst.SE_MARS)) {
			System.out.println("Mars is station");
		}
		
		if (isRetro(currMillis,SweConst.SE_JUPITER) ) {
			System.out.println("Jupiter is Retro");
		} else if (isStation(currMillis,SweConst.SE_JUPITER)) {
			System.out.println("Jupiter is station");
		}
		
		if (isRetro(currMillis,SweConst.SE_SATURN) ) {
			System.out.println("Saturn is Retro");
		} else if (isStation(currMillis,SweConst.SE_SATURN)) {
			System.out.println("Saturn is station");
		}
		
		if (isRetro(currMillis,SweConst.SE_URANUS) ) {
			System.out.println("Uranus is Retro");
		} else if (isStation(currMillis,SweConst.SE_URANUS)) {
			System.out.println("Uranus is station");
		}
		
		if (isRetro(currMillis,SweConst.SE_NEPTUNE) ) {
			System.out.println("Neptune is Retro");
		} else if (isStation(currMillis,SweConst.SE_NEPTUNE)) {
			System.out.println("Neptune is station");
		}
		
		if (isRetro(currMillis,SweConst.SE_PLUTO) ) {
			System.out.println("Pluto is Retro");
		} else if (isStation(currMillis,SweConst.SE_PLUTO)) {
			System.out.println("Pluto is station");
		}
	}

	public static void getFMNMUpdate(long currMillis) {
		String moonRashi = getRashiName(currMillis, SweConst.SE_MOON);
		String sunRashi = getRashiName(currMillis, SweConst.SE_SUN);
		
		String moonNakshatra = getNakshatraName(currMillis, SweConst.SE_MOON);
		String sunNakshatra = getNakshatraName(currMillis, SweConst.SE_SUN);
		
		if (isMoonAtDegrees(currMillis,90.0)) {
			System.out.println("\t\t\tSun-Moon at 90. Changing quarter. Moon (" + moonRashi + "). Sun (" + sunRashi + ")");
		} else if (isMoonAtDegrees(currMillis,270.0)) {
			System.out.println("\t\t\tSun-Moon at 270. Changing quarter. Moon (" + moonRashi + "). Sun (" + sunRashi + ")");
		} else if (isMoonAtDegrees(currMillis,180.0)) {
			System.out.println("\t\t\tFull Moon Day. Moon (" + moonRashi + "). Sun (" + sunRashi + ")");
		} else if (isMoonAtDegrees(currMillis,0.0)) {
			System.out.println("\t\t\tNew Moon Day. Moon and Sun in (" + moonRashi + ")");
		} else {
			double sunLongi = getSiderealLongitude(currMillis,SweConst.SE_SUN);
			double moonLongi = getSiderealLongitude(currMillis,SweConst.SE_MOON);
			SweDate sd = Conversions.getSweDate(currMillis);
			double speed = SwissHelper.getSpeed(sd,SweConst.SE_MOON);

			System.out.print("Sun:\t" + Conversions.getDblStr(sunLongi) + "\t(" + sunRashi + "-"+ sunNakshatra + ")");
			System.out.print("\tMoon:\t" + Conversions.getDblStr(moonLongi) + "\t(" + moonRashi + "-"+ moonNakshatra + ")");
			System.out.print("\tDiff from " + Conversions.getDblStr(Conversions.getAbsoluteDiff(sunLongi,moonLongi)));
			System.out.println("\tto " + Conversions.getDblStr(Conversions.getAbsoluteDiff(sunLongi,moonLongi+speed)));
			System.out.print("Sun Lord is\t" + Conversions.getPlanetName(getSunRashiLord(currMillis)));
			System.out.println("\t\tMoon Lord is\t" + Conversions.getPlanetName(getMoonRashiLord(currMillis)));
			
		}
	}

	public static void getDetailsUpdate(long currMillis) {
		System.out.println();
	}

	public static int getSmallestLongi(double p1, double p2, double p3) {
		if ((p1 < p2) && (p1 < p3)) {
			return 1;
		}

		if ((p2 < p1) && (p2 < p3)) {
			return 2;
		}
		return 3;
	}
	
	public static int getLargestLongi(double p1, double p2, double p3) {
		if ((p1 > p2) && (p1 > p3)) {
			return 1;
		}

		if ((p2 > p1) && (p2 > p3)) {
			return 2;
		}
		return 3;
	}

	public static int getMiddleLongi(double p1, double p2, double p3) {
		int a = getSmallestLongi(p1,p2,p3);
		int b = getLargestLongi(p1,p2,p3);
		if (a==1 && b==2) return 3;
		if (a==1 && b==3) return 2;

		if (a==2 && b==3) return 1;
		if (a==2 && b==1) return 3;

		if (a==3 && b==1) return 2;
		if (a==3 && b==2) return 1;

		return 0;
	}
	
	
	public static void getMercVenusUpdate(long currMillis) {
		String meStatus = "";
		String veStatus = "";
		String suStatus = "Su";
		
		if (isRetro(currMillis,SweConst.SE_MERCURY)) {
			meStatus = "Me(R)";
		} else {
			meStatus = "Me(D)";
		}
		
		if (isRetro(currMillis,SweConst.SE_VENUS)) {
			veStatus = "Ve(R)";
		} else {
			veStatus = "Ve(D)";
		}
		
		// TODO: Setting planet status is NOT included yet
		String allThree[] = {meStatus, veStatus, suStatus};

		double meLongi = getSiderealLongitude(currMillis,SweConst.SE_MERCURY);
		double veLongi = getSiderealLongitude(currMillis,SweConst.SE_VENUS);
		double suLongi = getSiderealLongitude(currMillis,SweConst.SE_SUN);

		int firstPlanet = getSmallestLongi(meLongi, veLongi, suLongi);
		int secondPlanet = getMiddleLongi(meLongi, veLongi, suLongi);
		int thirdPlanet = getLargestLongi(meLongi, veLongi, suLongi);
		String sequence = allThree[firstPlanet-1] + "-" + allThree[secondPlanet-1] + "-" + allThree[thirdPlanet-1];

		System.out.println(sequence);
		
		if (isPlanetSettingInEast(currMillis, SweConst.SE_MERCURY)) {
			System.out.println("Merc setting in the East");
		} else if (isPlanetSettingInWest(currMillis, SweConst.SE_MERCURY)) {
			System.out.println("Merc setting in the West");
		} else {
			// NOT setting. Let us find out if it is in east or west
			if (isRetroEasterly(currMillis, SweConst.SE_MERCURY)) {
				System.out.println("Merc Retro in the East");
			} else if (isRetroWesterly(currMillis, SweConst.SE_MERCURY)) {
				System.out.println("Merc Retro in the West");
			}
		}
		
		if (isPlanetSettingInEast(currMillis, SweConst.SE_VENUS)) {
			System.out.println("Venus setting in the East");
		} else if (isPlanetSettingInWest(currMillis, SweConst.SE_VENUS)) {
			System.out.println("Venus setting in the West");
		} else {
			// NOT setting. Let us find out if it is in east or west
			if (isRetroEasterly(currMillis, SweConst.SE_VENUS)) {
				System.out.println("Venus Retro in the East");
			} else if (isRetroWesterly(currMillis, SweConst.SE_VENUS)) {
				System.out.println("Venus Retro in the West");
			}
		}

		double declMerc = getDeclination(currMillis, SweConst.SE_MARS);
		double declVen = getDeclination(currMillis, SweConst.SE_VENUS);
		double maxDeclMerc = SwissHelper.maxPossibleDeclinations[SweConst.SE_MARS];
		double maxDeclVen = SwissHelper.maxPossibleDeclinations[SweConst.SE_VENUS];
				
		double mercPercentDecli = declMerc*100/SwissHelper.maxPossibleDeclinations[SweConst.SE_MERCURY];
		
		System.out.println();
		
		System.out.println("Merc Decl/Max Decl\t" + Conversions.getDblStrWithDecimal(mercPercentDecli) + "%\t\t" +
				Conversions.getDblStrWithDecimal(declMerc) + "/" + Conversions.getDblStrWithDecimal(maxDeclMerc));

		double venusPercentDecli = declVen*100/SwissHelper.maxPossibleDeclinations[SweConst.SE_VENUS];
		
		System.out.println("Ven Decl/Max Decl\t" + Conversions.getDblStrWithDecimal(venusPercentDecli) + "%\t\t" +
				Conversions.getDblStrWithDecimal(declVen) + "/" + Conversions.getDblStrWithDecimal(maxDeclVen));
		
		System.out.println();
		
		if (isMeFasterThanVe(currMillis) && isMeAcceleratingAndDirect(currMillis) ) {
			System.out.println("Merc is faster, accelerating and Direct");
		} else if (isMeFasterThanVe(currMillis) && isMeAcceleratingAndRetro(currMillis) ) {
			System.out.println("Merc is faster, accelerating and Retro");
		} 
		
		if (isMeDeccelerating(currMillis)) {
			if (isRetro(currMillis,SweConst.SE_MERCURY)) {
				System.out.println("Merc is decelerating and Retro");
			} else {
				System.out.println("Merc is decelerating");	
			}
		} else if (isMeSlowingDown(currMillis)) {
			System.out.println("Merc is just slowing down");
		}
		
		if (isVeDeccelerating(currMillis)) {
			if (isRetro(currMillis,SweConst.SE_VENUS)) {
				System.out.println("Venus is decelerating and Retro");
			} else {
				System.out.println("Venus is decelerating");	
			}
		} else if (isVeSlowingDown(currMillis)) {
			System.out.println("Venus is just slowing down");
		}
		
		if (areInRashi(currMillis, SweConst.SE_SUN, SweConst.SE_MERCURY, 0)) {
			if (areInRashi(currMillis, SweConst.SE_SUN, SweConst.SE_VENUS, 0)) {
				System.out.println("Merc, Venus and Sun are in Mesh. Markets will be confused");	
			} else {
				System.out.println("Merc and Sun are in Mesh. Markets Bullish");
			}
		} else {
			if (areInRashi(currMillis, SweConst.SE_SUN, SweConst.SE_VENUS, 0)) {
				System.out.println("Venus and Sun are in Mesh. Markets Bearish");
			}
		}
		
		String str = getMercVenusDiffUpdate(currMillis);
		System.out.println(str);
	}
	
	public static void getDeclinationUpdate(long currMillis) {
		for (int pCounter=1;pCounter<=9; pCounter++) {
			double decl = getDeclination(currMillis,pCounter);
			
			double refDegree = 0;
			double maxDecl = SwissHelper.maxPossibleDeclinations[pCounter]; 
			if (decl> (refDegree-1) && decl < (refDegree+1)) {
				System.out.println(Conversions.getPlanetName(pCounter) + " is close to Zero declination");	
			}
			
			if (decl> maxDecl-1 && decl < maxDecl) {
				System.out.println(Conversions.getPlanetName(pCounter) + " is close to Max North declination");
			}
			if (decl < (-maxDecl+1) && decl > (-maxDecl)) {
				System.out.println(Conversions.getPlanetName(pCounter) + " is close to Max South declination");
			}
		}
	}
	
	public static void getConjInRashiUpdate(long currMillis) {
		for (int p1 = 0; p1 < 10; p1++) {
			for (int p2 = p1 + 1; p2 < 10; p2++) {
				if (areInSameRashi(currMillis, p1, p2)) {
					String pName1 = Conversions.getPlanetName(p1);
					String pName2 = Conversions.getPlanetName(p2);
					int r1 = getRashi(currMillis, p1);
					// String rashi = Conversions.getZeroBasedNavamansh(r1)
					String rashi = Conversions.getSignName(r1);
					System.out.println(pName1 + " and " + pName2 + " are in " + rashi);
				}
			}
		}
	}

	public static String getMercVenusDiffUpdate(long currMillis) {
		String retStr = "";
		double diff = getMercVenusSeparation(currMillis);
		boolean mercFollows = isP1FollowingP2(currMillis,SweConst.SE_MERCURY, SweConst.SE_VENUS);
		if (mercFollows) {
			retStr = "Mercury follows Venus and is " + Conversions.getDblStrWithDecimal(diff) + " degrees away";
		} else {
			retStr = "Venus follows Mercury and is " + Conversions.getDblStrWithDecimal(diff) + " degrees away";
		}
		return retStr;
	}
	
	public static void getConjInNavamshaUpdate(long currMillis) {
		for (int p1 = 0; p1 < 10; p1++) {
			for (int p2 = p1 + 1; p2 < 10; p2++) {
				if (areInSameNavamsha(currMillis, p1, p2)) {
					String pName1 = Conversions.getPlanetName(p1);
					String pName2 = Conversions.getPlanetName(p2);
					int r1 = getNavamsha(currMillis, p1);
					String rashi = Conversions.getSignName(r1);
					System.out.println(pName1 + " and " + pName2 + " are in " + rashi + " Navamsha");
				}
			}
		}
	}	
	
	public static void getSummaryForDay(long currMillis) {
		String date = Conversions.getDisplayDate(currMillis);
		String dow = Conversions.getDayOfTheWeek(currMillis);

		System.out.println("Date:\t" + date);
		System.out.println("Day:\t" + dow);
		getFMNMUpdate(currMillis);
		
		// Mercury Venus and Sun update
		getMercVenusUpdate(currMillis);
		System.out.println();
		
		getConjInRashiUpdate(currMillis);
		System.out.println();
		
		getConjInNavamshaUpdate(currMillis);
		System.out.println();
		
		getDeclinationUpdate(currMillis);
		System.out.println();
		
		// Retro
		// getRetroUpdate(currMillis);
		System.out.println();
		initMapForTheDay(currMillis);
		
		printKundali();
		System.out.println();
		printPlanetDetails(currMillis);			
		System.out.println("===========================================================================");
		System.out.println();
	}

/*	
	public static void main(String[] args) {
		PrintStream console = null;
		console = GenericWriter.printToFile("/home/spider/Data/vedicCal-1925-2051.txt");
		
		long startMillis = Utilities.getTimeInMillis("2024-01-01");
		long endMillis = Utilities.getTimeInMillis("2051-01-01");

		//long startMillis = Utilities.getTimeInMillis("1999-01-01");
		//long endMillis = Utilities.getTimeInMillis("2025-01-01");

		for (long currMillis = startMillis; currMillis < endMillis; currMillis += (Conversions.millisInADay)) {
			String date = Conversions.getDisplayDate(currMillis);
			getSummaryForDay(currMillis);
		}
		GenericWriter.resetOut(console);
	}
*/	
}

	