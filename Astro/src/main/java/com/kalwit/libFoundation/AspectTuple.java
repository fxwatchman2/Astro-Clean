package com.kalwit.libFoundation;

import swisseph.SweDate;

public class AspectTuple {
	private int aspectType; // 0-conj, 1- square, 2-trine, 3-opp
		
	private SweDate date;
	private int p1;
	private int p2;
	private double orb;
	
	public AspectTuple(int aspectType) {
		this.aspectType = aspectType;
	}
	
	public void createAspect(SweDate date, int p1,int p2,double orb) {
		this.date = date;
		this.p1 = p1;
		this.p2 = p2;
		this.orb= orb;
	}
	
	public int getP1() {
		return p1;
	}

	public int getP2() {
		return p2;
	}

	public double getOrb() {
		return orb;
	}

	public String getOrbStr() {
		return Conversions.getDblStrWithDecimal(orb);
	}

	public SweDate getDate() {
		return date;
	}

	public String getDateStr() {
		return Conversions.getDisplayDate(date);
	}

	public int getAspect() {
		return aspectType;
	}

	public String getAspectStr() {
		if (aspectType==0) return "conj";
		if (aspectType==1) return "square";
		if (aspectType==2) return "trine";
		if (aspectType==3) return "opp";
		
		return "undefined";
	}
	
}

