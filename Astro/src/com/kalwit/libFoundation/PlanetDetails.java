package com.kalwit.libFoundation;

public class PlanetDetails {
	private double longitude;
	private double latitude;
	private double declination;
	private double percentClosest;
	
	public PlanetDetails(double longitude, double latitude, double declination,double percentClosest) {
		this.longitude = longitude;
		this.latitude = latitude;
		this.declination = declination;
		this.percentClosest = percentClosest;
	}
	
	public double getLongitude() {
		return longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getDeclination() {
		return declination;
	}

	public double getPercentClosest() {
		return percentClosest;
	}

}
