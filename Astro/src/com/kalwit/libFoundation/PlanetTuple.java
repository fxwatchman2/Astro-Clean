package com.kalwit.libFoundation;

import swisseph.SweDate;

public class PlanetTuple implements Comparable<PlanetTuple> {
	
	
	public static final int usedForAspects = 0;
	public static final int usedForRashiIngress = 1;
	public static final int usedForNakIngress = 2;
	public static final int usedForCharanIngress = 3;
	
	public static final int usedForCharanNavamsh = 4;
	public static final int usedForFixedStar1 = 5; // old definition of Fixed Star
	public static final int usedForFixedStar2 = 6; // new fixed star list of 119 stars 

	public int usedFor;
	public SweDate date;
	public int p1;
	public int p2;
	public double p1Degrees;
	public double p2Degrees;

	public double degrees;
	public double degreesStar; // used only for fixed stars
	
	public PlanetTuple(SweDate date, int p1,int p2,double degrees) {
		usedFor = usedForAspects;
		this.date = date;
		this.p1 = p1;
		this.p2 = p2;
		this.degrees = degrees;
	}
	
	public PlanetTuple(SweDate date, int p1,int p2,double degrees, int usedFor) {
		this.usedFor = usedFor;
		this.date = date;
		this.p1 = p1;
		this.p2 = p2;
		this.degrees = degrees;
	}

	// this constructor is for use case 6 (planets conjunctions and oppositions with fixed stars )
	// note: degreesP1 and degreesS1 are the same for conjunctions
	public PlanetTuple(SweDate date, int p1,int s1,double degreesP1,double degreesS1, int usedFor) {
		this.usedFor = usedFor;
		this.date = date;
		this.p1 = p1;
		this.p2 = s1;
		this.degrees = degreesP1;
		this.degreesStar = degreesS1;
	}

	public void setP1Degrees(double d) {
		this.p1Degrees = d;
	}
	
	public void setP2Degrees(double d) {
		this.p2Degrees = d;
	}
	
	public double getP1Degrees() {
		return p1Degrees;
	}
	
	public double getP2Degrees() {
		return p2Degrees;
	}
	
	// used for aspects
	public void printUsedFor0() {

		double l1 = SwissHelper.getSiderealLongitude(date, p1);
		double l2 = SwissHelper.getSiderealLongitude(date, p2);
		
		int rashi1 = (int) (l1/30.0);
		int rashi2 = (int) (l2/30.0);
		
		int nak1 = (int) (l1/13.33334);
		int nak2 = (int) (l2/13.33334);
		
		double d1 = SwissHelper.getDeclination(date, p1);
		double d2 = SwissHelper.getDeclination(date, p2);
		
		double sp1 = SwissHelper.getSpeed(date, p1);
		double sp2 = SwissHelper.getSpeed(date, p2);
		
		boolean s1 = (sp1==0.0);
		boolean s2 = (sp2==0.0);
				
		boolean r1 = (sp1<0.0);
		boolean r2 = (sp2<0.0);
				
		System.out.print(Conversions.getDisplayDateTime(date) +"\t");
		
		// offset readings for aspects other than 0 and 180
		if ((this.degrees !=0) || (this.degrees !=180)) {
			System.out.print("\t");
		}
		System.out.print(Conversions.getDblStr(degrees) + "\t");
		if (s1) System.out.print("s-");
		if (r1) System.out.print("r-");
		System.out.print(Conversions.getPlanetName(p1) +"\t");
		if (s2) System.out.print("s-");
		if (r2) System.out.print("r-");
		System.out.print(Conversions.getPlanetName(p2) +"\t");
		System.out.print("(" + Utilities.getDblStr(d1)+ ":");
		System.out.print(Conversions.getDblStr(d2) + ")\t");
		System.out.print("(" + Utilities.getShortRashiName(rashi1) + ":");
		System.out.print(Utilities.getShortRashiName(rashi2) + ")\t");
		System.out.print("(" + Utilities.getShortNakName(nak1) + ":");
		System.out.print(Utilities.getShortNakName(nak2) + ")\t");
		System.out.println();
	}

	// used for sidereal rashi ingress
	public void printUsedFor1() {
		System.out.print("\t");
		System.out.print(Conversions.getDisplayDateTime(date) +"\t");
		System.out.print(Conversions.getPlanetName(p1) +"\t");
		System.out.print(Utilities.getShortRashiName(p2) +"\t");
		System.out.println((int)degrees);
	}

	// used for sidereal Nakshatra ingress
	public void printUsedFor2() {
		System.out.print("\t");
		System.out.print(Utilities.getDisplayDateTime(date) +"\t");
		System.out.print(Utilities.getPlanetName(p1) +"\t");
		System.out.print(Utilities.getShortNakName(p2) +"\t");
		System.out.println((int)degrees);
	}

/*	
	// used for sidereal Nakshatra Charan ingress
	public void printUsedFor3() {
		int nak = p2/4;
		int charan = p2 % 4;
		
		System.out.print("\t");
		System.out.print(Utilities.getDisplayDateTime(date) +"\t");
		System.out.print(Utilities.getPlanetName(p1) +"\t");
		System.out.print(Utilities.getShortNakName(nak) +"\t");
		System.out.print(charan +"\t");
		System.out.println((int)degrees);
	}

	// used for Navamsh
	public void printUsedFor4() {
		int navamsh = p2 % 12;

		System.out.print("\t");
		System.out.print(Utilities.getDisplayDateTime(date) +"\t");
		System.out.print(Utilities.getPlanetName(p1) +"\t");
		System.out.print(Utilities.getShortRashiName(navamsh) +"\t");
		System.out.print(navamsh +"\t");
		System.out.println((int)degrees);
	}

	
	// used for Fixed stars (shorter list with declination defined in FixedStar.java)
	public void printUsedFor5() {
		String star = FixedStar.getAllStars()[p2].getName();
		
		long dateTime = Conversions.getTimeInMillis(date);
		boolean isP1Retro = AstroUtils.isRetro(dateTime, p1);
		
		System.out.print(Utilities.getDisplayDateTime(date) +"\t");
		System.out.print(Utilities.getPlanetName(p1));
		if (isP1Retro) {
			System.out.print("(R)");
		}
		System.out.print("\t");
		System.out.print(star +"\t");
		System.out.println(Conversions.getDblStrWithDecimal(degrees));
	}

	// used for Fixed stars (shorter list with declination defined in FixedStar.java)
	public void printUsedFor51() {
		//String star = FixedStar.getAllStars()[p2].getName();
		String star = FixedStar.getNumberAndNameStr(p2);
		
		
		long dateTime = Conversions.getTimeInMillis(date);
		boolean isP1Retro = AstroUtils.isRetro(dateTime, p1);
		System.out.println();
		System.out.print(Utilities.getDisplayDateTime(date) +"\t");
		System.out.print("\t");
		System.out.print(star +"-");
		//System.out.println(Conversions.getDblStrWithDecimal(degrees));
		System.out.print(Utilities.getPlanetName(p1));
		if (isP1Retro) {
			//System.out.print("(R)");
		}
	}

	// used for Fixed stars (shorter list with declination defined in FixedStar.java)
	public void printUsedFor6() {
		String star = FixedStar.getAllStars()[p2].getName();
		
		long dateTime = Conversions.getTimeInMillis(date);
		boolean isP1Retro = AstroUtils.isRetro(dateTime, p1);
		
		System.out.print(Utilities.getDisplayDateTime(date) +"\t");
		System.out.print(Utilities.getPlanetName(p1));
		if (isP1Retro) {
			System.out.print("(R)");
		}
		System.out.print("\t");
		System.out.println(Conversions.getDblStrWithDecimal(degrees));
		System.out.print(star);
		System.out.print("\t");
		System.out.println(Conversions.getDblStrWithDecimal(degreesStar));
	}

	
	public void print() {
		if (usedFor==0) printUsedFor0();
		if (usedFor==1) printUsedFor1();
		if (usedFor==2) printUsedFor2();
		if (usedFor==3) printUsedFor3();
		if (usedFor==4) printUsedFor4();
		if (usedFor==5) printUsedFor51(); // temporary: printing only date
		if (usedFor==6) printUsedFor6();
	}
*/
	   // Implement compareTo method
    @Override
    public int compareTo(PlanetTuple other) {
        return Long.compare(Conversions.getTimeInMillis(date), Conversions.getTimeInMillis(other.date));
    }	
}

