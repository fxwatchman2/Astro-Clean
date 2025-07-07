package com.kalwit.libFoundation;

import java.util.ArrayList;
import java.util.List;

import swisseph.SweConst;
import swisseph.SweDate;
import swisseph.SwissEph;
import swisseph.TCPlanet;
import swisseph.TCPlanetPlanet;
import swisseph.TransitCalculator;

/*
xx[0]:   longitude
xx[1]:   latitude
xx[2]:   distance in AU
xx[3]:   speed in longitude (degree / day)
xx[4]:   speed in latitude (degree / day)
xx[5]:   speed in distance (AU / day)
 */


public class SwissHelper {
	
	public static double au2Miles = 92955807.0;
	
	private static double moonPerigee = 225700.0;
	private static double moonApogee =  252500.0;
	
	private static double sunPerihelion = 91402500.0;
	private static double sunAphelion =   94509100.0;

	static int MAX_PLANETS = 21;
	static int MAX_VALUES = 6;
	
	public static double mercuryMaxSpeed =   2.20;
	public static double mercuryAvgSpeed =   1.09;

	public static double venusMaxSpeed =   1.24;
	public static double venusAvgSpeed =   1.22;
	
	public static double mercGeoMinDist = 53558.21;
	public static double mercGeoMaxDist = 131860.26;
	
	public static double venGeoMinDist = 69943.05;
	public static double venGeoMaxDist = 161304.26;
	
	public static double mercHelioMinDist =  28583.56;
	public static double mercHelioMaxDist =  43382.60;
	
	public static double venHelioMinDist = 66784.94;
	public static double venHelioMaxDist = 67691.34;
	
	public static final int idxLongitude = 0;
	public static final int idxLatitude = 1;
	public static final int idxDistanceAU = 2;
	public static final int idxSpeedLongi = 3;
	public static final int idxSpeedLati = 4;
	public static final int idxSpeedDist = 5;

	public static final double gcLongitude = 266.51; // 265.25
	public static final double gcDecl  = -29;
	
	int[] planets = { SweConst.SE_SUN, SweConst.SE_MOON, SweConst.SE_MERCURY,SweConst.SE_VENUS, SweConst.SE_MARS,
			SweConst.SE_JUPITER,SweConst.SE_SATURN, SweConst.SE_URANUS, SweConst.SE_NEPTUNE, 
			SweConst.SE_PLUTO, SweConst.SE_MEAN_NODE};
	
	//private static SwissEph swissEph = new SwissEph("C:/Jars");
	private static SwissEph swissEph = new SwissEph("/Jars"); 

	public static double[] synodicPeriods = {365,29.5,116,584,780,399,378,369,367,265};
	public static double[] maxPossibleDeclinations = {23.45,28.64, 30.45, 24.84, 26.6, 24.75,  25.94, 28.8,   29.3,   49.3,    30.5};
	// in million miles
	public static double[] maxPossibleDistances =    {94.55,0.258, 135.0,161.43,248.88,600.32,1027.38,1963.64,2914.28,4679.94, 0.26};
	public static double[] minPossibleDistances =    {91.45,0.22 , 51.05, 24.59, 34.62,366.32, 746.52,1605.62,2679.00,2666.93, 0.22};
	public static double[] astangatAngularDistance = {0.0,  11.0,  12.0, 7.0,   15.0,  10.0,  14.0,   14.0,   14.0,   14.0,    14.0, 0.0 };
	
	public static SwissEph getSwissEph() {
		// NY
		double longitude = -74.0063889;
		double latitude = 40.7141667;
	
		// Set the geographic coordinates using swe_set_topo
		swissEph.swe_set_topo(longitude, latitude, 0);
		return swissEph; 
	}

	public static SwissEph getSwissEph(double longitude, double latitude) {
		swissEph = new SwissEph("/Jars");
		swissEph.swe_set_topo(longitude, latitude, 0);
		return swissEph; 
	}

	public SwissHelper() {
		// NY
		double longitude = -74.0063889;
		double latitude = 40.7141667;
		
		// Set the geographic coordinates using swe_set_topo
		swissEph.swe_set_topo(longitude, latitude, 0);
	}
	
	public static SwissEph resetSwissEph() {
		swissEph = new SwissEph("/Jars");

		// NY
		double longitude = -74.0063889;
		double latitude = 40.7141667;
		
		// Set the geographic coordinates using swe_set_topo
		swissEph.swe_set_topo(longitude, latitude, 0);

		return swissEph; 
	}
	
	public static double getTropicalLongitude(SweDate sd, int p1) {
		SwissEph swissEph =  getSwissEph();
		StringBuffer serr1 = new StringBuffer();
		double[] xpGeo = new double[MAX_VALUES];
		int flags = SweConst.SEFLG_SWIEPH | 
						SweConst.SEFLG_SPEED;
		
        // Set the local time zone offset
        double timeZoneOffset = 5.0;
        double julianDate = sd.getJulDay() - timeZoneOffset / 24.0;
        
		int ret1 = swissEph.swe_calc_ut(julianDate, p1, flags, xpGeo, serr1);
		return xpGeo[0];
	}

	public static double[] getTropicalRawData(SweDate sd, int p1) {
		SwissEph swissEph =  getSwissEph();
		StringBuffer serr1 = new StringBuffer();
		double[] xpGeo = new double[MAX_VALUES];
		int flags = SweConst.SEFLG_SWIEPH | 
						SweConst.SEFLG_SPEED;
		
        // Set the local time zone offset
        double timeZoneOffset = 5.0;
        double julianDate = sd.getJulDay() - timeZoneOffset / 24.0;
		int ret1 = swissEph.swe_calc_ut(julianDate, p1, flags, xpGeo, serr1);
		return xpGeo;
	}

	public static double[] getHelioRawData(SweDate sd, int p1) {
		SwissEph swissEph =  getSwissEph();
		StringBuffer serr1 = new StringBuffer();
		double[] xpHelio = new double[MAX_VALUES];
		int flags = SweConst.SEFLG_SWIEPH | 
				SweConst.SEFLG_HELCTR;
	
        // Set the local time zone offset
        double timeZoneOffset = 5.0;
        double julianDate = sd.getJulDay() - timeZoneOffset / 24.0;
		int ret1 = swissEph.swe_calc_ut(julianDate, p1, flags, xpHelio, serr1);
		return xpHelio;
	}

	public static double[] getSiderealRawDataWorking(SweDate sd, int p1) {
		SwissEph swissEph =  getSwissEph();
		StringBuffer serr1 = new StringBuffer();
		double[] xpGeo = new double[MAX_VALUES];
		int flags = SweConst.SEFLG_SWIEPH | 
						SweConst.SEFLG_SIDEREAL |
						SweConst.SEFLG_SPEED;
		
        // Set the local time zone offset
        double timeZoneOffset = 5.0;
        double julianDate = sd.getJulDay() - timeZoneOffset / 24.0;
		int ret1 = swissEph.swe_calc_ut(julianDate, p1, flags, xpGeo, serr1);
		return xpGeo;
	}

	public static double[] getSiderealRawData(SweDate sd, int p1) {
		SwissEph swissEph =  getSwissEph();
		StringBuffer serr1 = new StringBuffer();
		double[] xpGeo = new double[MAX_VALUES];
		int flags = SweConst.SEFLG_SWIEPH | 
						SweConst.SEFLG_SPEED;
		
        // Set the local time zone offset
        double timeZoneOffset = 5.0;
        double julianDate = sd.getJulDay() - timeZoneOffset / 24.0;
		int ret1 = swissEph.swe_calc_ut(julianDate, p1, flags, xpGeo, serr1);
		xpGeo[0] = xpGeo[0] - 24.199;
		if (xpGeo[0]<0) xpGeo[0] = xpGeo[0] + 360.0;
		return xpGeo;
	}

	
	public static double[][] getSiderealRawDataForAllPlanets(SweDate sd) {
		double[][] allData = new double[SweConst.SE_MEAN_NODE+1][6];
		
		allData[SweConst.SE_SUN] = getSiderealRawData(sd,SweConst.SE_SUN);
		allData[SweConst.SE_MOON] = getSiderealRawData(sd,SweConst.SE_MOON);
		allData[SweConst.SE_MERCURY] = getSiderealRawData(sd,SweConst.SE_MERCURY);
		allData[SweConst.SE_VENUS] = getSiderealRawData(sd,SweConst.SE_VENUS);
		allData[SweConst.SE_MARS] = getSiderealRawData(sd,SweConst.SE_MARS);
		allData[SweConst.SE_JUPITER] = getSiderealRawData(sd,SweConst.SE_JUPITER);
		allData[SweConst.SE_SATURN] = getSiderealRawData(sd,SweConst.SE_SATURN);
		allData[SweConst.SE_URANUS] = getSiderealRawData(sd,SweConst.SE_URANUS);
		allData[SweConst.SE_NEPTUNE] = getSiderealRawData(sd,SweConst.SE_NEPTUNE);
		allData[SweConst.SE_PLUTO] = getSiderealRawData(sd,SweConst.SE_PLUTO);
		allData[SweConst.SE_TRUE_NODE] = getSiderealRawData(sd,SweConst.SE_TRUE_NODE);
		
		return allData;
	}

	public static double[][] getSiderealRawDataForAllPlanets(long millis) {
		SweDate sd = Conversions.getSweDate(millis);
		return getSiderealRawDataForAllPlanets(sd);
	}
	
	public static double getTropicalLatitude(SweDate sd, int p1) {
		SwissEph swissEph =  getSwissEph();
		StringBuffer serr1 = new StringBuffer();
		double[] xpGeo = new double[MAX_VALUES];
		int flags = SweConst.SEFLG_SWIEPH | 
						SweConst.SEFLG_SPEED;
		
        // Set the local time zone offset
        double timeZoneOffset = 5.0;
        double julianDate = sd.getJulDay() - timeZoneOffset / 24.0;
        
		int ret1 = swissEph.swe_calc_ut(julianDate, p1, flags, xpGeo, serr1);
		return xpGeo[1];
	}

	public static boolean isMoonBenefic(SweDate sd) {
		boolean moonIsBenefic = false;
		double sunLongi = getSiderealLongitude(sd, 0);
		double moonLongi = getSiderealLongitude(sd, 1);
		double diff = AstroUtils.getDiff(sunLongi, moonLongi);
		if ((diff>54) && (diff<230)) moonIsBenefic = true;
		return moonIsBenefic;
	}

	public static double getSiderealLongitude(SweDate sd, int p1) {
		SwissEph swissEph =  getSwissEph();
		swissEph.swe_set_sid_mode(SweConst.SE_SIDM_LAHIRI, 0, 0);
		StringBuffer serr1 = new StringBuffer();
		double[] xpGeo = new double[MAX_VALUES];
		int flags = SweConst.SEFLG_SWIEPH | 
						SweConst.SEFLG_SIDEREAL |
						SweConst.SEFLG_SPEED;
		
        // Set the local time zone offset
		double timeZoneOffset = 5.5;
        double julianDate = sd.getJulDay() - timeZoneOffset / 24.0;
	
		int ret1 = swissEph.swe_calc_ut(julianDate, p1, flags, xpGeo, serr1);
		return xpGeo[0];
	}

	public static double[] doPlanetocentricMath(double[] xpReference , double[] xpOriginal) {
		for (int i=0; i<MAX_VALUES; i++) {
			xpOriginal[i] = xpOriginal[i] - xpReference[i];
			if (xpOriginal[i] <0) {
				xpOriginal[i] = 360 + xpOriginal[i];
			}
		}
		return xpOriginal;
	}
	
	public static double[][] computePlanetocentricLongitude(int forPlanet, double[][] xpAllHelio) {
		for (int i=0; i<MAX_PLANETS; i++) {
			xpAllHelio[i][0] = xpAllHelio[i][0] - xpAllHelio[forPlanet][0];
			if (xpAllHelio[i][0]<0) xpAllHelio[i][0] = 360.0 + xpAllHelio[i][0];
		}
		return xpAllHelio;
	}

	public static double[][] computePlanetocentricLongiLati(int forPlanet, double[][] xpAllHelio) {
		for (int i=0; i<MAX_PLANETS; i++) {
			xpAllHelio[i][0] = xpAllHelio[i][0] - xpAllHelio[forPlanet][0];
			if (xpAllHelio[i][0]<0) xpAllHelio[i][0] = 360.0 + xpAllHelio[i][0];
			xpAllHelio[i][1] = xpAllHelio[i][1] - xpAllHelio[forPlanet][1];
			if (xpAllHelio[i][1]<0) xpAllHelio[i][1] = 360.0 + xpAllHelio[i][1];
		}
		return xpAllHelio;
	}

	public static double[] getHelioReading(SweDate sd, int p1) {
		SwissEph swissEph =  getSwissEph();
		StringBuffer serr1 = new StringBuffer();
		double[] xpHelio = new double[6];
		int flags = SweConst.SEFLG_SWIEPH | 
						SweConst.SEFLG_HELCTR;
		
        // Set the local time zone offset
        double timeZoneOffset = 5.0;
        double julianDate = sd.getJulDay() - timeZoneOffset / 24.0;
		
		int ret1 = swissEph.swe_calc_ut(julianDate, p1, flags, xpHelio, serr1);
		return xpHelio;
	}
	
	// This will select dates/events when usual 2 planet aspects are associated with 
	// another aspect of one of the two planets already in an aspect. NOT implemented and NOT used
	public List<PlanetTuple> processComplexAspect(int p1, int p2, double degrees,int flags,
			String startDateString,String endDateString) {
		
		SwissEph sw = SwissHelper.getSwissEph();
		
		//----------------------------------------------------------------
		
		TransitCalculator tc = new TCPlanetPlanet(
				sw,
				0,
				1,
				flags,
				degrees);

		//----------------------------------------------------------------
		boolean backwards = false;
		List<PlanetTuple> returnArray = new ArrayList<PlanetTuple>();
		
		SweDate sdStart = Conversions.getSweDate(Conversions.getTimeInMillis(startDateString));
		SweDate sdEnd = Conversions.getSweDate(Conversions.getTimeInMillis(endDateString));

		double jdUTStart = sdStart.getJulDay();
		double jdUTEnd = sdEnd.getJulDay();
		double nextTransitUT = jdUTStart;
		
		try {
			while (nextTransitUT > 0) {
				nextTransitUT = sw.getTransitUT(tc, nextTransitUT, backwards,jdUTEnd);
				
				SweDate sweDate = new SweDate(nextTransitUT);
				PlanetTuple planetTuple = new PlanetTuple(sweDate,p1,p2,degrees);
				returnArray.add(planetTuple);
				
				// strip off the time and use only day as a the key so that all aspects in a day will be clubbed together
				String strDateTime = Conversions.getDisplayDate(nextTransitUT);
				long millisDate = Conversions.getTimeInMillis(strDateTime);
				millisDate = Conversions.getTimeInMillis(Conversions.getDisplayDateTime(nextTransitUT)) + 24*60*60*1000;
				nextTransitUT = Conversions.getSweDate(millisDate).getJulDay();
			}
		} catch (swisseph.SwissephException e) {
			//System.out.print("*");
			return null;
		}
		return returnArray;
	}

	public List<PlanetTuple> processAnyAspect(int p1, int p2, int desiredAngle,int flags,
			String startDateString,String endDateString, boolean forming, long interval) {
		
		List<PlanetTuple> aspectArray = new ArrayList<PlanetTuple>();
		
		long startMillis = Conversions.getTimeInMillis(startDateString);
		long endMillis = Conversions.getTimeInMillis(endDateString);
		
		SweDate sdStart = Conversions.getSweDate(Conversions.getTimeInMillis(startDateString));
		SweDate sdEnd = Conversions.getSweDate(Conversions.getTimeInMillis(endDateString));

		for (long currDayMillis = startMillis; currDayMillis <= endMillis; currDayMillis += interval) {
			SweDate sdCurrDate= Conversions.getSweDate(currDayMillis);
			double d1 = SwissHelper.getSiderealLongitude(sdCurrDate, p1);
			double d2 = SwissHelper.getSiderealLongitude(sdCurrDate, p2);
			double orb = Utilities.getOrb(p1, p2);
			if (Utilities.areAtAngleForming(desiredAngle,d1,d2,orb )) {
				PlanetTuple planetTuple = new PlanetTuple(sdCurrDate,p1,p2,desiredAngle);
				if (forming) {
					if (d1<d2) {
						aspectArray.add(planetTuple);
					}
				} else {
					if (d1>d2) {
						aspectArray.add(planetTuple);
					}
				}
			}
		}
		return aspectArray;
	}
	
	public List<PlanetTuple> processAnyAspectWorking(int p1, int p2, double desiredAngle,int flags,
			String startDateString,String endDateString) {
		
		List<PlanetTuple> aspectArray = new ArrayList<PlanetTuple>();
		
		long startMillis = Conversions.getTimeInMillis(startDateString);
		long endMillis = Conversions.getTimeInMillis(endDateString);
		
		SweDate sdStart = Conversions.getSweDate(Conversions.getTimeInMillis(startDateString));
		SweDate sdEnd = Conversions.getSweDate(Conversions.getTimeInMillis(endDateString));

		for (long currDayMillis = startMillis; currDayMillis < endMillis; currDayMillis += Conversions.millisInADay) {
			SweDate sdCurrDate= Conversions.getSweDate(currDayMillis);
			double d1 = SwissHelper.getSiderealLongitude(sdCurrDate, p1);
			double d2 = SwissHelper.getSiderealLongitude(sdCurrDate, p2);
			double orb = Utilities.getOrb(p1, p2);
			if (Utilities.areAtAngle(desiredAngle,d1,d2,orb )) {
				PlanetTuple planetTuple = new PlanetTuple(sdCurrDate,p1,p2,desiredAngle);
				aspectArray.add(planetTuple);
			}
		}
		return aspectArray;
	}

	public List<PlanetTuple> processStarAspectTC(int p1, int s1, double desiredAngle,int flags,String startDateString,String endDateString) {
		
		int usedFor = 5;
		List<PlanetTuple> returnArray = new ArrayList<PlanetTuple>();
		boolean sidereal = true;
		long startMillis = Conversions.getTimeInMillis(startDateString);
		long endMillis = Conversions.getTimeInMillis(endDateString);
		
		long currentTime = startMillis;
		double divideBy = 1;

		while (currentTime < endMillis) {
			SweDate ingressDateTime = AspectManager.getConjunctDegree(p1, currentTime, desiredAngle, sidereal);
			PlanetTuple pTuple = null;
			pTuple = new PlanetTuple(ingressDateTime,p1,s1,desiredAngle,usedFor);
			returnArray.add(pTuple);
			// This can cause problem unless moved up by significantly large time. Moving up by 2 days
			currentTime = 48*60*60*1000+Conversions.getTimeInMillis(ingressDateTime);
			System.out.println(Conversions.getDisplayDate(currentTime) + "  :  " + desiredAngle + "  :  " + p1);
			
		}		
		return returnArray;
	}

	// p2 is Rashi or Nakshatra or charan or an ignorable
	public List<PlanetTuple> processDegreeConjunctionUsingTC(int p1, int p2, double desiredAngle,int flags,String startDateString,String endDateString, int usedFor) {
		
		List<PlanetTuple> returnArray = new ArrayList<PlanetTuple>();
		boolean sidereal = true;
		long startMillis = Conversions.getTimeInMillis(startDateString);
		long endMillis = Conversions.getTimeInMillis(endDateString);
		
		long currentTime = startMillis;
		while (currentTime < endMillis) {
			SweDate ingressDateTime = AspectManager.getConjunctDegree(p1, currentTime, desiredAngle, sidereal);
			PlanetTuple pTuple = null;
			pTuple = new PlanetTuple(ingressDateTime,p1,p2,desiredAngle,usedFor);
			returnArray.add(pTuple);
			currentTime = 5*60*60*1000+Conversions.getTimeInMillis(ingressDateTime); // move the current time by 5 hours. See if this still causes problems for the moon
			System.out.println(Conversions.getDisplayDate(currentTime) + "  :  " + desiredAngle + "  :  " + p1);
		}
		return returnArray;
	}
	
	
	public List<PlanetTuple> processIngressUsingTC(int p1, double desiredAngle,int flags,String startDateString,String endDateString,int usedFor) {
		
		List<PlanetTuple> returnArray = new ArrayList<PlanetTuple>();
		boolean sidereal = true;
		long startMillis = Conversions.getTimeInMillis(startDateString);
		long endMillis = Conversions.getTimeInMillis(endDateString);
		
		long currentTime = startMillis;
		double divideBy = 1;
		if (usedFor==1) {
			divideBy = 30.;
		}
		
		if (usedFor==2) {
			divideBy = 13.3333;
		}
		
		if (usedFor==3) {
			divideBy = 3.3333;
		}
		
		if (usedFor==4) {
			divideBy = 3.3333;
		}
		
		if (usedFor==5) {
			divideBy = 1; // exact match with a star
		}
			
		while (currentTime < endMillis) {
			SweDate ingressDateTime = AspectManager.getConjunctDegree(p1, currentTime, desiredAngle, sidereal);
			PlanetTuple pTuple = null;
			if (usedFor!=5) {
				pTuple = new PlanetTuple(ingressDateTime,p1,(int)(desiredAngle/divideBy),desiredAngle,usedFor);
			} else {
				pTuple = new PlanetTuple(ingressDateTime,p1,0,desiredAngle,usedFor);
			}
			returnArray.add(pTuple);
			currentTime = 5*60*60*1000+Conversions.getTimeInMillis(ingressDateTime);
			System.out.println(Conversions.getDisplayDate(currentTime) + "  :  " + desiredAngle + "  :  " + p1);
			
		}
		return returnArray;
	}
	
	// call with a start date and use the return date as the next start date 
	// since degrees will change for each call (0-90-180-270) 
	public long processSunMoonAspectUsingTC(double degrees,int flags,String strAfterDate) {
	
		int p1 = 0;
		int p2 = 1; 
		SwissEph sw = SwissHelper.getSwissEph();
		String endDateString = "2090-01-01";
		//----------------------------------------------------------------
		
		TransitCalculator tc = new TCPlanetPlanet(
				sw,
				p1,
				p2,
				flags,
				degrees);

		//----------------------------------------------------------------
		boolean backwards = false;
		SweDate sdStart = Conversions.getSweDate(Conversions.getTimeInMillis(strAfterDate));
		SweDate sdEnd = Conversions.getSweDate(Conversions.getTimeInMillis(endDateString));

		double jdUTStart = sdStart.getJulDay();
		double jdUTEnd = sdEnd.getJulDay();
		double nextTransitUT = jdUTStart;
		long returnDateMillis = 0;
		
		try {
			nextTransitUT = sw.getTransitUT(tc, nextTransitUT, backwards,jdUTEnd);
			String strDateTime = Conversions.getDisplayDateTime(nextTransitUT);
			//System.out.println("Date = " + Conversions.getTimeInMillis(strDateTime));
			//returnDateMillis = Conversions.getTimeInMillis(strDateTime);
			returnDateMillis = Conversions.getTimeInMillisDateAndTime(strDateTime);
			//System.out.println("After Conversions.getTimeInMillis strDateTime = " + Conversions.getDisplayDateTime(returnDateMillis));
		} catch (swisseph.SwissephException e) {
			System.out.print("error");
			return returnDateMillis;
		}
		return returnDateMillis;
	}

	public long getNextDateOfPlanetAtDegreeUsingTC(int p1, double degrees,int flags, long fromTime, long lastTime) {
		SwissEph sw = SwissHelper.getSwissEph();
		TransitCalculator tc = new TCPlanet(sw, p1, flags, degrees);
		boolean backwards = false;
		long millisDate = 0;
		
		SweDate sdStart = Conversions.getSweDate(fromTime);
		SweDate sdEnd = Conversions.getSweDate(lastTime); // Not to exceed this time in general

		double jdUTStart = sdStart.getJulDay();
		double jdUTEnd = sdEnd.getJulDay();
		
		try {
			double nextTransitUT = sw.getTransitUT(tc, jdUTStart, backwards,jdUTEnd);

			SweDate sweDate = new SweDate(nextTransitUT);
			String strDateTime = Conversions.getDisplayDate(nextTransitUT);
			millisDate = Conversions.getTimeInMillis(strDateTime);
		} catch (swisseph.SwissephException e) {
			//System.out.print(e);
			return millisDate;
		}
		return millisDate;
	}
	
	
	// Used for finding conjunction of planets with various fixed stars (51 or 52 of them, captured in FixedStars class
	public List<PlanetTuple> processConjunctionWithFixedStarsUsingTC(int p1, int starNum, double degrees,int flags,String startDateString,String endDateString) {
		SwissEph sw = SwissHelper.getSwissEph();
		
		TransitCalculator tc = new TCPlanet(sw, p1, flags, degrees);

		boolean backwards = false;
		List<PlanetTuple> returnArray = new ArrayList<PlanetTuple>();
		
		SweDate sdStart = Conversions.getSweDate(Conversions.getTimeInMillis(startDateString));
		SweDate sdEnd = Conversions.getSweDate(Conversions.getTimeInMillis(endDateString));

		double jdUTStart = sdStart.getJulDay();
		double jdUTEnd = sdEnd.getJulDay();
		double nextTransitUT = jdUTStart;
		final int usedForFixedStars = 5;
		
		try {
			// nextTransitUT is stuck on the same date 3/30/2000 ; Debug this: Probably for Moon
			while (nextTransitUT > 0) {
				nextTransitUT = sw.getTransitUT(tc, nextTransitUT, backwards,jdUTEnd);
				
				SweDate sweDate = new SweDate(nextTransitUT);
				PlanetTuple planetTuple = new PlanetTuple(sweDate,p1,starNum,degrees,usedForFixedStars); // fix this by creating one more constructor
				returnArray.add(planetTuple);
				
				// strip off the time and use only day as a the key so that all aspects in a day will be clubbed together
				String strDateTime = Conversions.getDisplayDate(nextTransitUT);
				
				//System.out.println(strDateTime);
				long millisDate = Conversions.getTimeInMillis(strDateTime);
				
				// 
				// adding one week to advance the start date
				millisDate = Conversions.getTimeInMillis(Conversions.getDisplayDateTime(nextTransitUT)) + 7*24*60*60*1000; 
				
				nextTransitUT = Conversions.getSweDate(millisDate).getJulDay();
				strDateTime = Conversions.getDisplayDate(nextTransitUT);
				//System.out.println(strDateTime);
			}
		} catch (swisseph.SwissephException e) {
			//System.out.print(e);
			return returnArray;
		}
		return returnArray;
	}
	
	public List<PlanetTuple> processAnyAspectUsingTC(int p1, int p2, double degrees,int flags,
			String startDateString,String endDateString) {
		
		SwissEph sw = SwissHelper.getSwissEph();
		
		//----------------------------------------------------------------
		
		TransitCalculator tc = new TCPlanetPlanet(
				sw,
				p1,
				p2,
				flags,
				degrees);

		//----------------------------------------------------------------
		boolean backwards = false;
		List<PlanetTuple> returnArray = new ArrayList<PlanetTuple>();
		
		SweDate sdStart = Conversions.getSweDate(Conversions.getTimeInMillis(startDateString));
		SweDate sdEnd = Conversions.getSweDate(Conversions.getTimeInMillis(endDateString));

		double jdUTStart = sdStart.getJulDay();
		double jdUTEnd = sdEnd.getJulDay();
		double nextTransitUT = jdUTStart;
		
		try {
			while (nextTransitUT > 0) {
				nextTransitUT = sw.getTransitUT(tc, nextTransitUT, backwards,jdUTEnd);
				SweDate sweDate = new SweDate(nextTransitUT);
				//String strDateTime = Conversions.getDisplayDate(nextTransitUT);
				//System.out.println(strDateTime);
				PlanetTuple planetTuple = new PlanetTuple(sweDate,p1,p2,degrees);
				returnArray.add(planetTuple);
				
				// strip off the time and use only day as a the key so that all aspects in a day will be clubbed together
				// strDateTime = Conversions.getDisplayDate(nextTransitUT);
				//long millisDate = Conversions.getTimeInMillis(strDateTime);
				long millisDate = Conversions.getTimeInMillis(Conversions.getDisplayDateTime(nextTransitUT)) + 2*Conversions.millisInADay; // adding two days instead of one. This will make Moon readings incorrect with Nakshatra, charan and charan_midpoints
				double previousValueOfNextTransitUT = nextTransitUT;
				nextTransitUT = Conversions.getSweDate(millisDate).getJulDay();
				if (previousValueOfNextTransitUT == nextTransitUT) {
					System.out.println("previousValueOfNextTransitUT == nextTransitUT");
					sw = null;
					return returnArray; // to break from a deadlock which seems to be happening randomly
				}
			}
		} catch (swisseph.SwissephException e) {
			sw = null;
			return returnArray;
		}
		return returnArray;
	}
	
	// keep a pristine copy of xpHelioReturn and make a new copy for getting each planets planetocentric readinds  
	public static double[][] getAllPlanetsHelioReading(SweDate sd) {
		double[][] xpHelioReturn = new double[MAX_PLANETS][MAX_VALUES];

		xpHelioReturn[SweConst.SE_MOON] = getHelioReading(sd, SweConst.SE_MOON);
		xpHelioReturn[SweConst.SE_MERCURY] = getHelioReading(sd, SweConst.SE_MERCURY);
		xpHelioReturn[SweConst.SE_VENUS] = getHelioReading(sd, SweConst.SE_VENUS);
		xpHelioReturn[SweConst.SE_MARS] = getHelioReading(sd, SweConst.SE_MARS);
		xpHelioReturn[SweConst.SE_JUPITER] = getHelioReading(sd, SweConst.SE_JUPITER);
		xpHelioReturn[SweConst.SE_SATURN] = getHelioReading(sd, SweConst.SE_SATURN);
		xpHelioReturn[SweConst.SE_URANUS] = getHelioReading(sd, SweConst.SE_URANUS);
		xpHelioReturn[SweConst.SE_NEPTUNE] = getHelioReading(sd, SweConst.SE_NEPTUNE);
		xpHelioReturn[SweConst.SE_PLUTO] = getHelioReading(sd, SweConst.SE_PLUTO);
		xpHelioReturn[SweConst.SE_MEAN_NODE] = getHelioReading(sd, SweConst.SE_MEAN_NODE);
		xpHelioReturn[SweConst.SE_TRUE_NODE] = getHelioReading(sd, SweConst.SE_TRUE_NODE);
		xpHelioReturn[SweConst.SE_MEAN_APOG] = getHelioReading(sd, SweConst.SE_MEAN_APOG);
		xpHelioReturn[SweConst.SE_OSCU_APOG] = getHelioReading(sd, SweConst.SE_OSCU_APOG);
		xpHelioReturn[SweConst.SE_EARTH] = getHelioReading(sd, SweConst.SE_EARTH);
		xpHelioReturn[SweConst.SE_CHIRON] = getHelioReading(sd, SweConst.SE_CHIRON);
		xpHelioReturn[SweConst.SE_PHOLUS] = getHelioReading(sd, SweConst.SE_PHOLUS);
		xpHelioReturn[SweConst.SE_CERES] = getHelioReading(sd, SweConst.SE_CERES);
		xpHelioReturn[SweConst.SE_PALLAS] = getHelioReading(sd, SweConst.SE_PALLAS);
		xpHelioReturn[SweConst.SE_JUNO] = getHelioReading(sd, SweConst.SE_JUNO);
		xpHelioReturn[SweConst.SE_VESTA] = getHelioReading(sd, SweConst.SE_VESTA);
		
		return xpHelioReturn; 
	}
	
	public static double getHelioLongitude(SweDate sd, int p1) {
		SwissEph swissEph =  getSwissEph();
		StringBuffer serr1 = new StringBuffer();
		double[] xpHelio = new double[6];
		int flags = SweConst.SEFLG_SWIEPH | 
						SweConst.SEFLG_HELCTR | 
						SweConst.SEFLG_SPEED;

        // Set the local time zone offset
        double timeZoneOffset = 5.0;
        double julianDate = sd.getJulDay() - timeZoneOffset / 24.0;
		
		int ret1 = swissEph.swe_calc_ut(julianDate, p1, flags, xpHelio, serr1);
		return xpHelio[0];
	}

	public static double getHelioDistance(SweDate sd, int p1) {
		SwissEph swissEph =  getSwissEph();
		StringBuffer serr1 = new StringBuffer();
		double[] xpHelio = new double[6];
		int flags = SweConst.SEFLG_SWIEPH | 
						SweConst.SEFLG_HELCTR | 
						SweConst.SEFLG_SPEED;

        // Set the local time zone offset
        double timeZoneOffset = 5.0;
        double julianDate = sd.getJulDay() - timeZoneOffset / 24.0;
		
		int ret1 = swissEph.swe_calc_ut(julianDate, p1, flags, xpHelio, serr1);
		return xpHelio[2];
	}

	/*
	 Generate a string that captures positions and speeds of Sun, Mercury and Venus as follows
	 Ve->Me->Su or Ve-R--Me->Su or Me->Su--Ve-R
	 
	  Possible combinations are
	  * -> accelerating
	  ~ -> decelerating
	  MeVeSu -> Ve is closer to Sun than Me and both are before the Sun in the Zodiac
	  Ve-Su--Me -> Ve is closer to Sun than Me and Ve is before and Me is after the Sun in the Zodiac
	  
	  MeVeSu (bearish) Me*VeSu (bearish+) Me*Ve~Su (bearish++) 
	  VeMeSu (bullish) VeMe*Su (bullish+) Ve~Me*Su (bullish++)
	  MeSuVe (bullish) Me*SuVe (bullish)  Me*SuVe~ (bullish++)
	  VeSuMe (bearish)
	  SuMeVe (bullish)
	  SuVeMe (bearish)
	*/

	public static boolean isAccelerating(String dateStr, int planet) {
		String prevDay = Conversions.getPreviousDate(dateStr);
		double speed1 = getSpeed(prevDay,planet);
		double speed2 = getSpeed(dateStr,planet);
		return (speed2 > speed1);
	}

	public static boolean isSlowing(String dateStr, int planet) {
		String prevDay = Conversions.getPreviousDate(dateStr);
		double speed1 = getSpeed(prevDay,planet);
		double speed2 = getSpeed(dateStr,planet);
		return (speed1 > speed2);
	}

	private static boolean isMercFasterThanV(String dateStr) {
		String prevDay = Conversions.getPreviousDate(dateStr);
		double speedM = getSpeed(prevDay,SweConst.SE_MERCURY);
		double speedV = getSpeed(dateStr,SweConst.SE_VENUS);
		return (speedM > speedV);
	}

	private static boolean isMCloserToSunThanV(double s, double m, double v) {
		return (Math.abs(m-s) < Math.abs(v-s));
	}

	public static boolean isRetro(String dateStr, int planet) {
		double speed = getSpeed(dateStr,planet);
		return (speed <= 0);
	}

	private static String getDecoratedMerc(String dateStr) {
		if (isRetro(dateStr, SweConst.SE_MERCURY)) {
			return "Me-R";
		} else {
			if (isAccelerating(dateStr, SweConst.SE_MERCURY)) {
				return "Me->";
			}
		}
		return "Me";
	}
	
	private static String getDecoratedVen(String dateStr) {
		if (isRetro(dateStr, SweConst.SE_VENUS)) {
			return "Ve-R";
		} else {
			if (isAccelerating(dateStr, SweConst.SE_VENUS)) {
				return "Ve->";
			}
		}
		return "Ve";
	}
	
	private static String getSequence(String dateStr , double s,double m,double v) {
		String sequence = "";
		String mStr = getDecoratedMerc(dateStr);
		String vStr = getDecoratedVen(dateStr);
		if (s<60) s = s +360;
		if (m<60) m = m +360;
		if (v<60) v = v +360;
	
		boolean isMercSlow = !isMercFasterThanV(dateStr);
		
		if (s<m && s<v) {
			
			sequence = "S";
			if (m<v) {
				sequence = "S" + mStr + vStr;
			} else {
				sequence = "S" + vStr + mStr ;
			}
		} 

		if (m<s && m<v) {
			if (s<v) {
				sequence = mStr + "S" + vStr;
			} else {
				sequence = mStr + vStr + "S";
			}
		}
		
		if (v<s && v<m) {
			sequence = "V";
			if (s<m) {
				sequence = vStr + "S" + mStr;
			} else {
				sequence = vStr + mStr + "S";
			}
		}
		
		return sequence;
	}
	public static String getSMVString(String dateStr) {
		double degSun = getDegrees(dateStr, 0);
		double degMerc = getDegrees(dateStr, 2);
		double degVen = getDegrees(dateStr, 3);
	
		// to linearize the degrees in case sun isbetween 0 and 30 and merc and/or venus less than 360 
		if ((degMerc > 300) || (degVen > 300) || (degSun > 300)) {
			degSun += 360;
			degMerc += 360;
			degVen += 360;
		}
		
		String retStr = getSequence(dateStr, degSun,degMerc,degVen);
		return retStr;	 
	}
	
	public static int getSMVScore(String dateStr) {
		if (true) return 0;  // not used right now. Needs work
		double degSun = getDegrees(dateStr, 0);
		double degMerc = getDegrees(dateStr, 2);
		double degVen = getDegrees(dateStr, 3);
		int score = 0;
		
		boolean mercRetro = isRetro(dateStr, SweConst.SE_MERCURY);
		boolean mercAccelerating = isAccelerating(dateStr, SweConst.SE_MERCURY);
		
		boolean venusRetro = isRetro(dateStr, SweConst.SE_VENUS);
		boolean venusAccelerating = isAccelerating(dateStr, SweConst.SE_VENUS);
		
		// to linearize the degrees in case sun isbetween 0 and 30 and merc and/or venus less than 360 
		if ((degMerc > 300) || (degVen > 300) || (degSun > 300)) {
			degSun += 360;
			degMerc += 360;
			degVen += 360;
		}
		
		// bearish when Merc is ahead of the Sun
		if ((degMerc > degSun ) && (degVen > degSun)) {
			if (degMerc > degVen) {
				score = 10;
			} else {
				if (mercRetro && mercAccelerating) {
					score = 30; // less bearish when Merc is closer to the Sun
					if (venusRetro && venusAccelerating) {
						score = 20; // less bearish when Merc is closer to the Sun
					} else if (venusRetro) {
						score = 25;
					}
				} else {
					score = 20; // less bearish when Merc is closer to the Sun
				}
			}
		}
		
		if ((degMerc < degSun ) && (degVen < degSun)) {
			if (degMerc < degVen) {
				score = 25;
				if (mercRetro && mercAccelerating) {
					score = 15;
				}
			} else {
				score = 40; 
				if (mercAccelerating) {
					score = 50;
				}
				if (mercRetro) {
					score = 40;
				}
			}
		}

		// When Merc is behind and Venus is ahead
		if ((degMerc < degSun ) && (degVen > degSun)) {
			if (isMCloserToSunThanV(degSun,degMerc,degVen)) {
				// TODO	
			} else {
				// TODO	
			}
		}
	
		if ((degMerc > degSun ) && (degVen < degSun)) {
			if (isMCloserToSunThanV(degSun,degMerc,degVen)) {
				if (mercAccelerating) {
					score = 50;
				} else {
					score = 40;
				}
			} else {
				if (mercAccelerating) {
					score = 30;
				} else {
					score = 20;
				}
			}
		}
		return score;	 
	}
	
	public static double getDistance(SweDate sd, int p1) {
		SwissEph swissEph =  getSwissEph();
		StringBuffer serr1 = new StringBuffer();
		double[] xpGeo = new double[6];
		int flags = SweConst.SEFLG_SWIEPH | 
				SweConst.SEFLG_SIDEREAL |
						SweConst.SEFLG_SPEED;

        // Set the local time zone offset
        double timeZoneOffset = 5.0;
        double julianDate = sd.getJulDay() - timeZoneOffset / 24.0;
		
		int ret1 = swissEph.swe_calc_ut(julianDate, p1, flags, xpGeo, serr1);
		return xpGeo[2];
	}

	public static double getSpeed(SweDate sd, int p1) {
		SwissEph swissEph =  getSwissEph();
		StringBuffer serr1 = new StringBuffer();
		double[] xpGeo = new double[6];
		int flags = SweConst.SEFLG_SWIEPH | 
				SweConst.SEFLG_SIDEREAL |
						SweConst.SEFLG_SPEED;

        // Set the local time zone offset
        double timeZoneOffset = 5.0;
        double julianDate = sd.getJulDay() - timeZoneOffset / 24.0;
		
		int ret1 = swissEph.swe_calc_ut(julianDate, p1, flags, xpGeo, serr1);
		return xpGeo[3];
	}

	public static double getSpeed(String dateStr, int p1) {
		SweDate sd = Conversions.getSweDate(dateStr);
		double speed = getSpeed(sd,p1);
		return speed;
	}

	public static double getDegrees(String dateStr, int p1) {
		SweDate sd = Conversions.getSweDate(dateStr);
		double degrees = getSiderealLongitude(sd, p1);
		return degrees;
	}

	public static double getDecl(long date, int p1) {
		SweDate sd = Conversions.getSweDate(date);
		double decl = getDeclination(sd, p1);
		return decl;
	}

	public static double getDecl(String dateStr, int p1) {
		SweDate sd = Conversions.getSweDate(dateStr);
		double decl = getDeclination(sd, p1);
		return decl;
	}

	public static double getDistance(long date, int p1) {
		String dateStr = Utilities.getDisplayDate(date);
		return getDistance(dateStr, p1);
	}
	
	public static double getDistance(String dateStr, int p1) {
		SweDate sd = Conversions.getSweDate(dateStr);
		double distance = getDistance(sd, p1) * SwissHelper.au2Miles;
		return distance;
	}

	public static double getHelioDistance(String dateStr, int p1) {
		SweDate sd = Conversions.getSweDate(dateStr);
		double distance = getHelioDistance(sd, p1) * SwissHelper.au2Miles;
		return distance;
	}

	public static boolean isRetro(SweDate sd, int p1) {
		double speed = getSpeed(sd, p1);
		return (speed < 0.0);
	}

	public static double getDeclination(SweDate sd, int p1) {
		SwissEph swissEph =  getSwissEph();
		StringBuffer serr1 = new StringBuffer();
		double[] xpGeo = new double[6];
		int flags = SweConst.SEFLG_SWIEPH | 
						SweConst.SEFLG_EQUATORIAL | 
						SweConst.SEFLG_SPEED;
		
        // Set the local time zone offset
        double timeZoneOffset = 5.0;
        double julianDate = sd.getJulDay() - timeZoneOffset / 24.0;
		
		int ret1 = swissEph.swe_calc_ut(julianDate, p1, flags, xpGeo, serr1);
		return xpGeo[1];
	}

	public boolean isCombust(SweDate sd, int p1, double combustDegrees) {
		int sun = 0;
		boolean condition1 = false;
		boolean condition2 = false;
		
		double mercDegrees = getSiderealLongitude(sd, p1);
		double sunDegrees = getSiderealLongitude(sd, sun);
		// 1. the separation should be less than 13 degrees
		condition1 = (Math.abs(sunDegrees-mercDegrees) <= combustDegrees);
		double d2 = getSiderealLongitude(Conversions.getNextDaySweDate(sd), p1);
		// 2. Sun should be closer than Merc
		condition2 = (getDistance(sd, sun) < getDistance(sd, p1));

		return (condition1 && condition1);
	}
	
	public boolean isCombustMerc(SweDate sd) {
		double combustDegrees = 13.0;		
		int merc = 2;
		return isCombust(sd, merc, combustDegrees); 
	}
	
	public boolean isCombustVenus(SweDate sd) {
		double combustDegrees = 8.0;		
		int venus = 3;
		return isCombust(sd, venus, combustDegrees); 
	}
	
	public static PlanetDetails getPlanetDetails(SweDate sd, int p1) {
		SwissEph swissEph =  getSwissEph();
		StringBuffer serr1 = new StringBuffer();
		double[] xpGeo = new double[6];
		int flags = SweConst.SEFLG_SWIEPH | 
						SweConst.SEFLG_SPEED;

        // Set the local time zone offset
        double timeZoneOffset = 5.0;
        double julianDate = sd.getJulDay() - timeZoneOffset / 24.0;
		
		int ret1 = swissEph.swe_calc_ut(julianDate, p1, flags, xpGeo, serr1);

		//PlanetDetails(double longitude, double latitude, double declination,double percentClosest)
		PlanetDetails planetDetails =  new PlanetDetails(getSiderealLongitude(sd, p1),0,getDeclination(sd, p1),0);
		
		/*
		planetDetails.planet = p1;
		planetDetails.siderealLongitude = getSiderealLongitude(sd, p1); 
		planetDetails.tropicalLongitude = xpGeo[0];
		planetDetails.helioLongitude = getHelioLongitude(sd, p1); 
		planetDetails.declination = getDeclination(sd, p1);
		planetDetails.distance = xpGeo[2] * au2Miles;
		planetDetails.speed = xpGeo[3];

		planetDetails.rashiSidereal = 1 + (int)(planetDetails.siderealLongitude/30.0);
		planetDetails.rashiTropical = 1 + (int)(planetDetails.tropicalLongitude/30.0);
		planetDetails.nakshatra = 1 +  (int)(planetDetails.siderealLongitude/13.3333);
		
		planetDetails.nameRashiTropical = AstroHelper.get1BasedSignName(planetDetails.rashiTropical); 
		planetDetails.nameRashiSidereal = AstroHelper.get1BasedSignName(planetDetails.rashiSidereal);
		planetDetails.nameNakshtra = AstroHelper.get1BasedNakshatraName(planetDetails.nakshatra); 
		*/
		return planetDetails;
	}
	
	public PlanetDetails[] getAllPlanetDetails(SweDate sd) {
		PlanetDetails[] allDetails = new PlanetDetails[MAX_PLANETS];

		allDetails[SweConst.SE_SUN] = getPlanetDetails(sd, SweConst.SE_SUN);
		allDetails[SweConst.SE_MOON] = getPlanetDetails(sd, SweConst.SE_MOON);
		allDetails[SweConst.SE_MERCURY] = getPlanetDetails(sd, SweConst.SE_MERCURY);
		allDetails[SweConst.SE_VENUS] = getPlanetDetails(sd, SweConst.SE_VENUS);
		allDetails[SweConst.SE_MARS] = getPlanetDetails(sd, SweConst.SE_MARS);
		allDetails[SweConst.SE_JUPITER] = getPlanetDetails(sd, SweConst.SE_JUPITER);
		allDetails[SweConst.SE_SATURN] = getPlanetDetails(sd, SweConst.SE_SATURN);
		allDetails[SweConst.SE_URANUS] = getPlanetDetails(sd, SweConst.SE_URANUS);
		allDetails[SweConst.SE_NEPTUNE] = getPlanetDetails(sd, SweConst.SE_NEPTUNE);
		allDetails[SweConst.SE_PLUTO] = getPlanetDetails(sd, SweConst.SE_PLUTO);
		allDetails[SweConst.SE_MEAN_NODE] = getPlanetDetails(sd, SweConst.SE_MEAN_NODE);
		allDetails[SweConst.SE_TRUE_NODE] = getPlanetDetails(sd, SweConst.SE_TRUE_NODE);
		allDetails[SweConst.SE_MEAN_APOG] = getPlanetDetails(sd, SweConst.SE_MEAN_APOG);
		allDetails[SweConst.SE_OSCU_APOG] = getPlanetDetails(sd, SweConst.SE_OSCU_APOG);
		allDetails[SweConst.SE_EARTH] = getPlanetDetails(sd, SweConst.SE_EARTH);
		allDetails[SweConst.SE_CHIRON] = getPlanetDetails(sd, SweConst.SE_CHIRON);
		allDetails[SweConst.SE_PHOLUS] = getPlanetDetails(sd, SweConst.SE_PHOLUS);
		allDetails[SweConst.SE_CERES] = getPlanetDetails(sd, SweConst.SE_CERES);
		allDetails[SweConst.SE_PALLAS] = getPlanetDetails(sd, SweConst.SE_PALLAS);
		allDetails[SweConst.SE_JUNO] = getPlanetDetails(sd, SweConst.SE_JUNO);
		allDetails[SweConst.SE_VESTA] = getPlanetDetails(sd, SweConst.SE_VESTA);
		
		return allDetails; 
	}

	public void printAspects(int viewpointPlanet, double [][] xp) {
		double[] allPlanets = xp[viewpointPlanet];
		for (int i=0; i<allPlanets.length; i++) {
			System.out.print(allPlanets[i] + " ");
		}
		System.out.println(" len =" + allPlanets.length);
	}
	
	public String colSeparator() {
		return ",";
	}

	public String rowSeparator() {
		return System.lineSeparator();
	}

	public String getResult(ArrayList<AspectTuple> aspect) {
		String output = "";
		
		for (int counter=0; counter<aspect.size(); counter++) {
			AspectTuple tuple = aspect.get(counter);
			String prefix = " and ";
			if (counter==0) {
				prefix = "";
			} 
			output = output + prefix + tuple.getP1() + "-" + tuple.getP2(); 
		}
		return output;
	}
	
	public static void printLongitudes(int p, double[][] hReturn) {
		SwissEph sw = getSwissEph();
		System.out.println(sw.swe_get_planet_name(p));
		
		for (int c=0; c<hReturn.length; c++) {
			System.out.print(Utilities.getDblStr(hReturn[c][0]) + " ");
		}
		System.out.println();
	}

	public static void findMaxDistance(String fromDate, int numOfDays, int p1) {
		double maxDistance = Double.MIN_VALUE;
		
		long startMillis = Conversions.getTimeInMillis(fromDate);
		for (int counter=0; counter<numOfDays; counter++) {
			long currMillis = startMillis + counter * Conversions.millisInADay;
			SweDate sD = Conversions.getSweDate(currMillis);
			double d = getDistance(sD, p1);
			if (d > maxDistance) maxDistance = d;
			//if ((counter%365)==0) System.out.println(counter);
		}
		
		maxDistance = maxDistance * Conversions.AU2MillionMiles;
		System.out.println("Max Distance for :" + Conversions.getPlanetName(p1) + " is " + Conversions.getDblStrWithDecimal(maxDistance));
	}
	
	public static void findMinDistance(String fromDate, int numOfDays, int p1) {
		double minDistance = Double.MAX_VALUE;
		
		long startMillis = Conversions.getTimeInMillis(fromDate);
		for (int counter=0; counter<numOfDays; counter++) {
			long currMillis = startMillis + counter * Conversions.millisInADay;
			SweDate sD = Conversions.getSweDate(currMillis);
			double d = getDistance(sD, p1);
			if (d < minDistance) minDistance = d;
			//if ((counter%365)==0) System.out.println(counter);
		}
		
		minDistance = minDistance * Conversions.AU2MillionMiles;
		System.out.println("Min Distance for :" + Conversions.getPlanetName(p1) + " is " + Conversions.getDblStrWithDecimal(minDistance));
	}

	public static void fmtDbl(String s,double d) {
		System.out.println(s + " " + Utilities.getDblStr(d));
	}
	
	public static void main(String[] args) {

		long startDate = Utilities.getTimeInMillis("2024-01-01");
		long endDate = Utilities.getTimeInMillis("2025-01-01");

		double speed22 = getSpeed("2024-02-01", 2);
		double speed23 = getSpeed("2024-02-02", 2);

		System.out.println("Speed of Mercury: " +  Utilities.getDblStr(speed22));
		System.out.println("Speed of Mercury: " +  Utilities.getDblStr(speed23));
		
		//if (true) return;
		long currentDay = startDate; 
		double maxSpeed2 = 0.0;
		double maxSpeed3 = 0.0;
		double avgSpeed2 = 0.0;
		double avgSpeed3 = 0.0;
		int dayCounter = 0;
		
		while (currentDay<=endDate) {
			String dateStr = Utilities.getDisplayDate(currentDay);
			
			System.out.println(getSMVString(dateStr));
			
			double speed2 = getSpeed(dateStr, 2);
			double speed3 = getSpeed(dateStr, 3);

			if (speed2 > maxSpeed2) maxSpeed2 = speed2; 
			if (speed3 > maxSpeed3) maxSpeed3 = speed3;

			if (speed2 > 0) avgSpeed2 += speed2;
			if (speed3 > 0) avgSpeed3 += speed3;
			
			currentDay = currentDay + Conversions.millisInADay;
			dayCounter++;
		}
		
		fmtDbl("Max Merc  speed =" , maxSpeed2);
		fmtDbl("Max Venus speed =" , maxSpeed3);

		System.out.println();
		
		fmtDbl("Average Merc  speed =" ,avgSpeed2/dayCounter);
		fmtDbl("Average Venus speed =" , avgSpeed3/dayCounter);
		

		SwissHelper swH = new SwissHelper();		
		
		if (true) return;

		findMinDistance("2200-01-01", 240*365, SweConst.SE_TRUE_NODE);
		findMaxDistance("2200-01-01", 240*365, SweConst.SE_TRUE_NODE);
		
		
		String startDateString = "2023-05-01:10:30:02";
		String endDateString = "2023-06-15";
		
		SweDate sweDate = Conversions.getSweDate(Utilities.getTimeInMillis("2020-01-01"), 0);
		
		for (int i=0; i<=14; i++ ) {
			double[] eLongi = swH.getHelioRawData(sweDate , i);
			System.out.println(i + "  " + eLongi[0]);
		}
		
		if (true) return;
		
		long startMillis = Utilities.getTimeInMillis(startDateString);
		long endMillis = Utilities.getTimeInMillis(endDateString);
		
		String date = Utilities.getDisplayDate(startMillis);
		System.out.println(date);
		startMillis = Utilities.getTimeInMillis(date);
		date = Utilities.getDisplayDate(startMillis);
		System.out.println(date);

		if (true) return;
		
		SwissEph sw = getSwissEph();
		
		//int counter=0;
		for (long currMillis = startMillis; currMillis < endMillis; currMillis += Conversions.millisInADay) {
			double[][] pristineHelioReturn = swH.getAllPlanetsHelioReading(Utilities.getSweDate(currMillis, 0.0));
			
			for (int planetCounter=2; planetCounter<SweConst.SE_PLUTO; planetCounter++) {
				double[][] xpHelioReturn = pristineHelioReturn;

				//printLongitudes(planetCounter, xpHelioReturn);
				
				xpHelioReturn = computePlanetocentricLongitude(planetCounter, xpHelioReturn);
				printLongitudes(planetCounter, xpHelioReturn);
				
				boolean foundAnAspect = false;				
				double orb = 2.0;
				for (int pCounter1=0; pCounter1<=SweConst.SE_PLUTO; pCounter1++) {
					if (pCounter1==planetCounter) continue;
					for (int pCounter2=pCounter1+1; pCounter2<=SweConst.SE_PLUTO; pCounter2++) {
						if (pCounter2==planetCounter) continue;
						if (Utilities.areConjunct(xpHelioReturn[pCounter1][0], xpHelioReturn[pCounter2][0], orb)) {
							if (!foundAnAspect) {
								foundAnAspect = true;
								System.out.println("----------------");
								System.out.println("Viewpoint: " + sw.swe_get_planet_name(planetCounter));
								System.out.println(Utilities.getDisplayDate(currMillis));
								System.out.println("****************");
							}
							System.out.print("Conjunct " + sw.swe_get_planet_name(pCounter1));
							System.out.println(" " + sw.swe_get_planet_name(pCounter2));
						}
					}
				}

			}
		}
	}
}
