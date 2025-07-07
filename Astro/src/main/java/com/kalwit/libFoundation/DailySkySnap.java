package com.kalwit.libFoundation;

import java.util.ArrayList;

import swisseph.SweDate;
import swisseph.SwissEph;

public class DailySkySnap {
	
	public final static int longitudeIndex = 0;
	public final static int latitudeIndex = 1;
	public final static int distance_in_auIndex = 2;
	public final static int speed_in_longitudeIndex = 3;
	public final static int speed_in_latitudeIndex = 4;
	public final static int speed_in_distanceIndex = 5;
		
	public long date= 0;
	String newline = System.getProperty("line.separator");
	public ArrayList<SkySnapPerPlanet> allPlanets = new ArrayList<SkySnapPerPlanet>();
	SwissEph sw = new SwissEph("C:/Jars");


	public DailySkySnap(long date, ArrayList<SkySnapPerPlanet> allPlanets) {
		this.date= date; 
		for (int counter=0; counter<allPlanets.size(); counter++) {
			this.allPlanets.add(allPlanets.get(counter));
		}
	}
	public DailySkySnap(SweDate sd, ArrayList<SkySnapPerPlanet> allPlanets) {
		this.date= Utilities.getTimeInMillisDateAndTime(sd);
		this.allPlanets.addAll(allPlanets);
	}

	public long getDate() {
		return date;
	}

	public double getDegrees(int planetNum) {
		return allPlanets.get(planetNum).xpSidereal[longitudeIndex];
	}
	
	public boolean isStationary(int planetNum) {
		boolean isItStationary = 
				((allPlanets.get(planetNum).xpSidereal[speed_in_longitudeIndex] < 0.5)
				&&
				(allPlanets.get(planetNum).xpSidereal[speed_in_longitudeIndex] > -0.5));
		return isItStationary;

	}

	public boolean isRetro(int planetNum) {
		boolean isItRetro = (allPlanets.get(planetNum).xpSidereal[speed_in_longitudeIndex] < 0.0);
		return isItRetro;

	}

	public int planetNakIngress(int planetNum) {
		int nakshatra = (int) (allPlanets.get(planetNum).xpSidereal[longitudeIndex]/13.34);
		if ((allPlanets.get(planetNum).xpSidereal[longitudeIndex] - (nakshatra*13.3)) < 1.0) return nakshatra+1;
		return -1;
	}

	public int planetIngress(int planetNum) {
		int sign = (int) (allPlanets.get(planetNum).xpSidereal[longitudeIndex]/30.0);
		if ((allPlanets.get(planetNum).xpSidereal[longitudeIndex] - (sign*30)) < 1.0) return sign+1;
		return -1;
	}

	public int planetIngressTrop(int planetNum) {
		int sign = (int) (allPlanets.get(planetNum).xpTrop[longitudeIndex]/30.0);
		if ((allPlanets.get(planetNum).xpTrop[longitudeIndex] - (sign*30)) < 1.0) return sign+1;
		return -1;
	}	

	public int planetIngressH(int planetNum) {
		int sign = (int) (allPlanets.get(planetNum).xpHelio[longitudeIndex]/30.0);
		if ((allPlanets.get(planetNum).xpHelio[longitudeIndex] - (sign*30)) < 1.0) return sign+1;
		return -1;
	}

	public boolean planetInSign(int planetNum, int signNum) {
		return (
				(allPlanets.get(planetNum).xpSidereal[longitudeIndex] >= signNum*30)
				&&
				(allPlanets.get(planetNum).xpSidereal[longitudeIndex] <= (signNum+1)*30)
				);
	}

	public double planetSpeed(int planetNum) {
		return allPlanets.get(planetNum).xpSidereal[distance_in_auIndex];
	}
	
	public double planetDistance(int planetNum) {
		return allPlanets.get(planetNum).xpSidereal[speed_in_distanceIndex]; 
	}
	
	public int planetInSign(int planetNum) {
		return (int) (1+(allPlanets.get(planetNum).xpSidereal[longitudeIndex]/30.0));
	}
	
	public int planetInSiderealSign(int planetNum) {
		double siderealDegrees = allPlanets.get(planetNum).xpSidereal[longitudeIndex]-Utilities.ayanamsha;
		if (siderealDegrees < 0) siderealDegrees = 360.0 + siderealDegrees;
		int signNum = (int) (1+(siderealDegrees/30.0));
		return signNum; 
	}
	
	public int planetInNakshatra(int planetNum) {
		double siderealDegrees = allPlanets.get(planetNum).xpSidereal[longitudeIndex]-Utilities.ayanamsha;
		if (siderealDegrees < 0) siderealDegrees = 360.0 + siderealDegrees;
		
		int nakshatraNum = (int) (1+(siderealDegrees/13.34));
		return nakshatraNum;
	}
	
	public boolean planetsAreConjunct(int thePlanet, int secondPlanet, double orb) {
		return Utilities.areConjunct(allPlanets.get(thePlanet).xpSidereal[longitudeIndex], allPlanets.get(secondPlanet).xpSidereal[longitudeIndex], orb);
	}

	public boolean planetsAreOpposed(int thePlanet, int secondPlanet, double orb) {
		return Utilities.areOpposed(allPlanets.get(thePlanet).xpSidereal[longitudeIndex], allPlanets.get(secondPlanet).xpSidereal[longitudeIndex], orb);
	}

	public boolean planetsAreTrine(int thePlanet, int secondPlanet, double orb) {
		return Utilities.areTrine(allPlanets.get(thePlanet).xpSidereal[longitudeIndex], allPlanets.get(secondPlanet).xpSidereal[longitudeIndex], orb);
	}

	public boolean planetsAreSquared(int thePlanet, int secondPlanet, double orb) {
		return Utilities.areSquared(allPlanets.get(thePlanet).xpSidereal[longitudeIndex], allPlanets.get(secondPlanet).xpSidereal[longitudeIndex], orb);
	}

	public boolean planetsAreConjunctH(int thePlanet, int secondPlanet, double orb) {
		return Utilities.areConjunct(allPlanets.get(thePlanet).xpHelio[longitudeIndex], allPlanets.get(secondPlanet).xpHelio[longitudeIndex], orb);
	}

	public boolean planetsAreOpposedH(int thePlanet, int secondPlanet, double orb) {
		return Utilities.areOpposed(allPlanets.get(thePlanet).xpHelio[longitudeIndex], allPlanets.get(secondPlanet).xpHelio[longitudeIndex], orb);
	}

	public boolean planetsAreTrineH(int thePlanet, int secondPlanet, double orb) {
		return Utilities.areTrine(allPlanets.get(thePlanet).xpHelio[longitudeIndex], allPlanets.get(secondPlanet).xpHelio[longitudeIndex], orb);
	}

	public boolean planetsAreSquaredH(int thePlanet, int secondPlanet, double orb) {
		return Utilities.areSquared(allPlanets.get(thePlanet).xpHelio[longitudeIndex], allPlanets.get(secondPlanet).xpHelio[longitudeIndex], orb);
	}

	public boolean areAtAngleOf30(int thePlanet, int secondPlanet, double orb) {
		return Utilities.areAtAngleOf30(allPlanets.get(thePlanet).xpSidereal[longitudeIndex], allPlanets.get(secondPlanet).xpSidereal[longitudeIndex], orb);
	}

	public boolean areAtAngleOf60(int thePlanet, int secondPlanet, double orb) {
		return Utilities.areAtAngleOf60(allPlanets.get(thePlanet).xpSidereal[longitudeIndex], allPlanets.get(secondPlanet).xpSidereal[longitudeIndex], orb);
	}

	// we don't need other varieties
	public boolean areAtAngle(double desiredAngle,int thePlanet, int secondPlanet, double orb) {
		return Utilities.areAtAngle(desiredAngle,allPlanets.get(thePlanet).xpSidereal[longitudeIndex], allPlanets.get(secondPlanet).xpSidereal[longitudeIndex], orb);
	}

	public void generateAllAspectys() {
		String retStr = "";
		int maxPlanets = allPlanets.size();
		double orb = 0.5;

		for (int counter=0; counter<maxPlanets; counter++) {
			for (int p2Counter=counter+1; p2Counter<maxPlanets; p2Counter++) {
				if ((counter==1) || (p2Counter==1) ) { // for Moon
					orb = 5.0;
				} else {
					orb = 0.5;
				}

				if (planetsAreConjunct(counter, p2Counter, orb)) {
					// sw.swe_get_planet_name(counter) 
					// sw.swe_get_planet_name(p2Counter)
					// Utilities.getDblStr(allPlanets.get(p2Counter).xpGeo[longitude]); 
				} else if (planetsAreOpposed(counter, p2Counter, orb)) {
				} else if (planetsAreTrine(counter, p2Counter, orb)) {
				} else if (planetsAreSquared(counter, p2Counter, orb)) {
				} else if (areAtAngleOf30(counter, p2Counter, orb)) {
				} else if (areAtAngleOf60(counter, p2Counter, orb)) {
				}
			}
		}
	}
	
	public String getAllAspects() {
		String retStr = "";
		int maxPlanets = allPlanets.size();
		double orb = 0.5;

		for (int counter=0; counter<maxPlanets; counter++) {
			for (int p2Counter=counter+1; p2Counter<maxPlanets; p2Counter++) {
				if ((counter==1) || (p2Counter==1) ) { // for Moon
					orb = 5.0;
				} else {
					orb = 0.5;
				}

				if (planetsAreConjunct(counter, p2Counter, orb)) {
					retStr = retStr + newline + "Conjunction: " + sw.swe_get_planet_name(counter) + " and\t" + sw.swe_get_planet_name(p2Counter)
					+ " at\t" + Utilities.getDblStr(allPlanets.get(p2Counter).xpSidereal[0]); 
				} else if (planetsAreOpposed(counter, p2Counter, orb)) {
					retStr = retStr +newline + "Opposition: " + sw.swe_get_planet_name(counter) + " and\t" + sw.swe_get_planet_name(p2Counter)
					+ " at\t" + Utilities.getDblStr(allPlanets.get(counter).xpSidereal[0]) + " at\t" + Utilities.getDblStr(allPlanets.get(p2Counter).xpSidereal[0]); 
				} else if (planetsAreTrine(counter, p2Counter, orb)) {
					retStr = retStr +newline + "Trine: " + sw.swe_get_planet_name(counter) + " and\t" + sw.swe_get_planet_name(p2Counter)
					+ " at\t" + Utilities.getDblStr(allPlanets.get(counter).xpSidereal[0]) + " at\t" + Utilities.getDblStr(allPlanets.get(p2Counter).xpSidereal[0]); 
				} else if (planetsAreSquared(counter, p2Counter, orb)) {
					retStr = retStr +newline + "Square: " + sw.swe_get_planet_name(counter) + " and\t" + sw.swe_get_planet_name(p2Counter)
					+ " at\t" + Utilities.getDblStr(allPlanets.get(counter).xpSidereal[0]) + " at\t" + Utilities.getDblStr(allPlanets.get(p2Counter).xpSidereal[0]); 
				} else if (areAtAngleOf30(counter, p2Counter, orb)) {
					retStr = retStr +newline + "30 Degrees: " + sw.swe_get_planet_name(counter) + " and\t" + sw.swe_get_planet_name(p2Counter)
					+ " at\t" + Utilities.getDblStr(allPlanets.get(counter).xpSidereal[0]) + " at\t" + Utilities.getDblStr(allPlanets.get(p2Counter).xpSidereal[0]); 
				} else if (areAtAngleOf60(counter, p2Counter, orb)) {
					retStr = retStr +newline + "60 Degrees: " + sw.swe_get_planet_name(counter) + " and\t" + sw.swe_get_planet_name(p2Counter)
					+ " at\t" + Utilities.getDblStr(allPlanets.get(counter).xpSidereal[0]) + " at\t" + Utilities.getDblStr(allPlanets.get(p2Counter).xpSidereal[0]); 
				}
			}
		}
		return retStr;
	}

	public String getAllIngressesAndRetro() {
		String retStr = "";
		int maxPlanets = allPlanets.size();
		for (int counter=0; counter<maxPlanets; counter++) {
			if (isRetro(counter)) {
				retStr = retStr +newline + "Retrograde: " + sw.swe_get_planet_name(counter) 
				+" at\t" + Utilities.getDblStr(allPlanets.get(counter).xpSidereal[0]); 
			}
		}

		for (int counter=0; counter<maxPlanets; counter++) {
			if (planetIngress(counter)> -1) {
				retStr = retStr +newline + "Ingress: " + sw.swe_get_planet_name(counter) +" in\t" + planetIngress(counter)
				+" at\t" + Utilities.getDblStr(allPlanets.get(counter).xpSidereal[0]); 
			}
		}

		retStr = retStr + newline + "**********************************************************" + newline; 
		return retStr;
	}
}
