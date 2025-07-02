package com.kalwit.libFoundation;

import java.util.ArrayList;

public class DayMapTuple {

	private static int MAX_PLANETS = 11;

	public DayMapTuple(long dateTime) {
		this.dateTime = dateTime;
		String dateStr = Utilities.getDisplayDate(dateTime);
		this.smv = SwissHelper.getSMVString(dateStr);
	}
	
	public long dateTime; // mills of the day
	
	public boolean fullMoon =  false;
	public boolean newMoon =  false;
	public boolean lunarEclipse =  false;
	public boolean solarEclipse =  false;
	public int tithiMarketStart;
	public int tithiMarketEnd;
	
	public String yogMarketStart;
	public String yogMarketEnd;
	
	public int dayOfTheWeek;
	
	public double earthLongi; // heliocentric data
	public double[][] rawData = new double[MAX_PLANETS][6]; // geocentric data
	public double[][] rawHelioData = new double[MAX_PLANETS][6]; 
	public double[] declination = new double[MAX_PLANETS];
	public double[] speed = new double[MAX_PLANETS];
	
	public boolean longestDistance[] = new boolean [MAX_PLANETS];  
	public boolean shortestDistance[] = new boolean [MAX_PLANETS]; 
	public boolean highestDeclination[] = new boolean [MAX_PLANETS]; 
	public boolean lowestDeclination[] = new boolean [MAX_PLANETS]; 
	public boolean zeroXDeclination[] = new boolean [MAX_PLANETS]; 
	public boolean retrograde[] = new boolean [MAX_PLANETS]; 

	public int tropicalRashi[] = new int[MAX_PLANETS]; // the tropical rashi number
	public int siderealRashi[] = new int[MAX_PLANETS];  // the geocentric rashi number
	public int siderealNakshatra[] = new int[MAX_PLANETS];  // the geocentric nakshatra number

	ArrayList<PlanetTuple> rashiIngresses = new ArrayList<PlanetTuple>();
	ArrayList<PlanetTuple> nakshatraIngresses = new ArrayList<PlanetTuple>();
	ArrayList<PlanetTuple> nakshatraCharanIngresses = new ArrayList<PlanetTuple>();
	
	String smv; // Sun Merc Ven 
	
	public void addRashiIngress(PlanetTuple pt) {
		rashiIngresses.add(pt);
	}
	
	public void addNakshatraIngress(PlanetTuple pt) {
		nakshatraIngresses.add(pt);
	}

	public void addNakshatraCharanIngress(PlanetTuple pt) {
		nakshatraCharanIngresses.add(pt);
	}
	
	public int getDayValue() {
		int total = 0;
		return total;
	}
	
	private void printRashiIngresses() {
		for (int counter=0; counter < rashiIngresses.size(); counter++) {
			PlanetTuple pt = rashiIngresses.get(counter);
			System.out.print(Conversions.getPlanetName(pt.p1) + "-" + Utilities.getShortRashiName(pt.p2));
			if (counter < (rashiIngresses.size()-1)) System.out.print(","); 
		}
	}
	
	private double[] getDistanceInMiles() {
		int maxPlanets = rawData.length;
		double[] allDistances = new double[maxPlanets];
		for (int counter=0; counter<maxPlanets; counter++) {
			allDistances[counter] = (100-(rawData[counter][2]*SwissHelper.au2Miles/(SwissHelper.maxPossibleDistances[counter]*1000000))*100); 
		}
	 return allDistances;
	}
	
	private boolean[] getAstangat() {
		int maxPlanets = rawData.length;
		boolean[] allPlanetStatus = new boolean[maxPlanets];
		for (int counter=0; counter<maxPlanets; counter++) {
			allPlanetStatus[counter] = false;
		}
		// Sun is never astnagat
		for (int counter=1; counter<maxPlanets; counter++) {
			allPlanetStatus[counter] = (Math.abs(rawData[0][0]-rawData[counter][0]) < SwissHelper.astangatAngularDistance[counter]);
		}
	 return allPlanetStatus;
	}
	
	private boolean[] getPositiveOrNegative() {
		int maxPlanets = rawData.length;
		boolean[] allPlanetStatus = new boolean[maxPlanets];
		for (int counter=0; counter<maxPlanets; counter++) {
			allPlanetStatus[counter] = true;
		}
		
		double a1 = 0;
		double a2 = 0;
		double b1 = 0;
		double b2 = 0;
		// Sun is the reference
		for (int counter=1; counter<maxPlanets; counter++) {
			a1 = rawData[0][0];
			a2 = rawData[0][0]+180;
			if (a2>360) {
				b2 = a2-360;
				a2 = 359.999999;
			}
			double r = rawData[counter][0];
			if ((r>a1) && (r<=a2)) {
				allPlanetStatus[counter] = false;	
			}
			if ((r>b1) && (r<b2)) {
				allPlanetStatus[counter] = false;	
			}
		}
	 return allPlanetStatus;
	}
	
	public static int getMaxPlanets() {
		return MAX_PLANETS;
	}
	
	private void displayBooleanArray(String msg, boolean displayArray[]) {
		System.out.print(msg +"\t");
		for (int counter=0; counter<displayArray.length; counter++) {
			if (displayArray[counter]) {
				System.out.print(AstroHelper.getPlanetName(counter) +"\t");
			} else {
				System.out.print("----" +"\t");
			}
		}
		System.out.println();
	}
	
	private void displayDoubleArray(String msg, double displayArray[]) {
		System.out.print(msg +"\t");
		for (int counter=0; counter<displayArray.length; counter++) {
			System.out.print(Utilities.getDblStr(displayArray[counter]) +"\t");
		}
		System.out.println();
	}

	private void displayNavamshaArray(String msg, double doubleArray[][]) {
		System.out.print(msg +"\t");
		for (int counter=0; counter<doubleArray.length; counter++) {
			int navRashiNum = Conversions.getZeroBasedNavamansh(doubleArray[counter][0]);
			String navRashi = Utilities.getShortRashiName(navRashiNum);
			System.out.print(navRashi +"\t");
		}
		System.out.println();
	}
	
	private void displayLongitudeArray(String msg, double doubleArray[][]) {
		System.out.print(msg +"\t");
		for (int counter=0; counter<doubleArray.length; counter++) {
			System.out.print(Utilities.getDblStr(doubleArray[counter][0]) +"\t");
		}
		System.out.println();
	}
	
	private void displayLongitudeArrayHC(String msg, double doubleArray[][]) {
		System.out.print(msg +"\t");
		System.out.print( Utilities.getDblStr(earthLongi) +"\t");
		for (int counter=1; counter<doubleArray.length; counter++) {
			System.out.print(Utilities.getDblStr(doubleArray[counter][0]) +"\t");
		}
		System.out.println();
	}
	
	private void displayPlanetArrayHC(String msg) {
		System.out.print(msg);
		System.out.print(AstroHelper.getPlanetName(14) +"\t");
		for (int counter=1; counter<MAX_PLANETS; counter++) {
			System.out.print(AstroHelper.getPlanetName(counter) +"\t");
		}
		System.out.println();
	}

	private void displayPlanetArray(String msg) {
		System.out.print(msg);
		for (int counter=0; counter<MAX_PLANETS; counter++) {
			System.out.print(AstroHelper.getPlanetName(counter) +"\t");
		}
		System.out.println();
	}

	private void displayRashiArray(String msg, int displayArray[]) {
		System.out.print(msg +"\t");
		for (int counter=0; counter<displayArray.length; counter++) {
			System.out.print(Utilities.getShortRashiName(displayArray[counter]) +"\t");
		}
		System.out.println();
	}
	
	
	private void displaySignArray(String msg, int displayArray[]) {
		System.out.print(msg +"\t");
		for (int counter=0; counter<displayArray.length; counter++) {
			System.out.print(Utilities.getShortSignName(displayArray[counter]) +"\t");
		}
		System.out.println();
	}
	
	private void displayEmptyLine() {
		System.out.println();
	}

	private void displayNakshatraArray(String msg, int displayArray[]) {
		System.out.print(msg +"\t");
		for (int counter=0; counter<displayArray.length; counter++) {
			System.out.print(Utilities.getShortNakName(displayArray[counter]) +"\t");
		}
		System.out.println();
	}

	private void computeSpeed() {
		for (int i=0; i<MAX_PLANETS; i++) {
			speed[i] = rawData[i][3];	
		}
	}
	
	private double moonMaxTill4pm() {
		return rawData[1][0] + rawData[1][3] * 6.5/24.0;
	}
	
	private boolean noMoonAspects() {
		boolean aspectFound = true;
		double d1 = rawData[1][0]; 
		double d2 = moonMaxTill4pm();
		// TODO. Find if there are any aspects with any of the planets other than the Sun

		for (int counter=2; counter<9; counter++) {
			aspectFound = aspectFound && (!Utilities.areAtAngle(0,d1,rawData[counter][0],1.0));
			aspectFound = aspectFound && (!Utilities.areAtAngle(30,d1,rawData[counter][0],1.0));
			aspectFound = aspectFound && (!Utilities.areAtAngle(60,d1,rawData[counter][0],1.0));
			aspectFound = aspectFound && (!Utilities.areAtAngle(90,d1,rawData[counter][0],1.0));
			aspectFound = aspectFound && (!Utilities.areAtAngle(120,d1,rawData[counter][0],1.0));
			aspectFound = aspectFound && (!Utilities.areAtAngle(150,d1,rawData[counter][0],1.0));
			aspectFound = aspectFound && (!Utilities.areAtAngle(180,d1,rawData[counter][0],1.0));
		}

		for (int counter=2; counter<9; counter++) {
			aspectFound = aspectFound && (!Utilities.areAtAngle(0,d2,rawData[counter][0],1.0));
			aspectFound = aspectFound && (!Utilities.areAtAngle(30,d2,rawData[counter][0],1.0));
			aspectFound = aspectFound && (!Utilities.areAtAngle(60,d2,rawData[counter][0],1.0));
			aspectFound = aspectFound && (!Utilities.areAtAngle(90,d2,rawData[counter][0],1.0));
			aspectFound = aspectFound && (!Utilities.areAtAngle(120,d2,rawData[counter][0],1.0));
			aspectFound = aspectFound && (!Utilities.areAtAngle(150,d2,rawData[counter][0],1.0));
			aspectFound = aspectFound && (!Utilities.areAtAngle(180,d2,rawData[counter][0],1.0));
		}

		return !aspectFound;
	}
	
	private boolean noSunAspects() {
		boolean aspectFound = true;
		double d1 = rawData[0][0]; 
		// TODO. Find if there are any aspects with any of the planets other than the Sun

		for (int counter=2; counter<9; counter++) {
			aspectFound = aspectFound && (!Utilities.areAtAngle(0,d1,rawData[counter][0],1.0));
			aspectFound = aspectFound && (!Utilities.areAtAngle(30,d1,rawData[counter][0],1.0));
			aspectFound = aspectFound && (!Utilities.areAtAngle(60,d1,rawData[counter][0],1.0));
			aspectFound = aspectFound && (!Utilities.areAtAngle(90,d1,rawData[counter][0],1.0));
			aspectFound = aspectFound && (!Utilities.areAtAngle(120,d1,rawData[counter][0],1.0));
			aspectFound = aspectFound && (!Utilities.areAtAngle(150,d1,rawData[counter][0],1.0));
			aspectFound = aspectFound && (!Utilities.areAtAngle(180,d1,rawData[counter][0],1.0));
		}

		return !aspectFound;
	}
	
	public void display() {
		
		tithiMarketStart = TithiCalculator.getTithi(dateTime);
		tithiMarketEnd = TithiCalculator.getTithi(dateTime+Conversions.millisInADay/2);
		yogMarketStart = TithiCalculator.getYogName(dateTime);
		yogMarketEnd = TithiCalculator.getYogName(dateTime+Conversions.millisInADay/2);
		
		System.out.println(Utilities.getDisplayDateTime(dateTime));
		//if (true) return;
		System.out.println(Utilities.getDisplayDate(dateTime) + "\t\t");
		System.out.println(smv);
		System.out.println();
		computeSpeed();
		System.out.print("     fm\t\t"); if (fullMoon) System.out.println("yes");  else System.out.println(); 
		System.out.print("     nm\t\t"); if (newMoon) System.out.println("yes"); else System.out.println();  
		System.out.print("lun ecl\t\t"); if (lunarEclipse) System.out.println("yes"); else System.out.println();
		System.out.print("sol ecl\t\t"); if (solarEclipse) System.out.println("yes"); else System.out.println(); 
		System.out.print(" Tithis\t\t"); System.out.println(""+tithiMarketStart +"\t" + tithiMarketEnd);
		System.out.print("    Yog\t\t"); System.out.println(yogMarketStart + "\t" + yogMarketEnd);
		System.out.print("    DOW\t\t"); System.out.println(""+dayOfTheWeek); 

		for (int i=0; i<declination.length; i++) {
			declination[i] = declination[i] * 100.0 / SwissHelper.maxPossibleDeclinations[i];
		}
		
		displayPlanetArray   ("Planets\t\t");
		displayLongitudeArray("S Longi\t" , rawData);
		
		displayNakshatraArray("  Naksh\t" , siderealNakshatra);
		
		displayRashiArray    ("  Rashi\t" , siderealRashi);
		displayNavamshaArray("  Navam\t", rawData);
		
		displayEmptyLine();
		displayDoubleArray   ("  Decl%\t" , declination);
		displayDoubleArray   ("MaxDecl\t" , SwissHelper.maxPossibleDeclinations);
		displayBooleanArray  ("  Retro\t" , retrograde);
		displayDoubleArray   ("  Speed\t" , speed);

		boolean[] pOrNPlanets = getPositiveOrNegative();
		displayBooleanArray  ("    +ve\t" , pOrNPlanets);
		boolean[] astPlanets = getAstangat();
		displayBooleanArray  ("  Astan\t" , astPlanets);
		displayEmptyLine();

		if (noMoonAspects()) {
		   System.out.println("Void Mo\t\t" + "Moon has no aspects during the trading day");	
		} else {
			System.out.println("Void Mo\t\t");
		}
			
		if (noSunAspects()) {
		    System.out.println("Void Su\t\t" + "Sun has no aspects during the trading day");	
		} else {
			System.out.println("Void Su\t\t");
		}
		System.out.println();
		
		double[] percentDistances = getDistanceInMiles();
		 displayDoubleArray("  Dist%\t" , percentDistances);
		displayBooleanArray("   Aph\t" , longestDistance);
		displayBooleanArray("   Per\t" , shortestDistance);


		for (int i=0; i<declination.length; i++) {
			declination[i] = declination[i] * 100.0 / SwissHelper.maxPossibleDeclinations[i];
		}
		
		System.out.print("   Ingr\t");printRashiIngresses();
		System.out.println();
		System.out.println("-----------------------------------------------------------------------------");
		System.out.println();
		
		/*
		displayDayMap3PAspect("in line\t", inALine);		
		displayDayMap3PAspect("squares\t", squares);		
		displayDayMap3PAspect(" trines\t", trines);		
		displayDayMap3PAspect("ang 135\t", angle135);		
		System.out.println();
		*/
	}
}
