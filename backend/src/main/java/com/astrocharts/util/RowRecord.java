package com.astrocharts.util;

public class RowRecord {
    private String date;
    private double open;
    private double high;
    private double low;
    private double close;
    private long volume;
    
    private double closeProjection; // projection Price
    private double lowProjection; // projection Price
    private double highProjection; // projection Price
    
    public RowRecord () {
    	
    }
    
    public RowRecord (String date,double open,double high,double low,double close,long volume) {
    	this.date = date;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;    	

        this.closeProjection = 0.0;
        this.lowProjection = 0.0;
        this.highProjection = 0.0;
    }

	public RowRecord(String data) {
		String[] parts = data.split(";");
		
		if (parts.length < 6) return;
		
		// Check if time is present in the first part
		String datePart = parts[0];
		String timePart = "T00:00:00";
		if (datePart.contains(" ")) {
			String[] dateAndTime = datePart.split(" ");
			datePart = dateAndTime[0];
			timePart = "T" + dateAndTime[1];
		}
		
		open = Double.parseDouble(parts[1]);
		high = Double.parseDouble(parts[2]);
        low = Double.parseDouble(parts[3]);
        close = Double.parseDouble(parts[4]);
        volume = Long.parseLong(parts[5]);

    	this.date = datePart + timePart;

        this.closeProjection = 0.0;
        this.lowProjection = 0.0;
        this.highProjection = 0.0;
    }
    
	public String getDate() {
		return date;
	}
	
	public void setDate(String date) {
		this.date = date;
	}
	
	public double getOpen() {
		return open;
	}
	
	public void setOpen(double open) {
		this.open = open;
	}

	public double getHigh() {
		return high;
	}

	public void setHigh(double high) {
		this.high = high;
	}

	public double getLow() {
		return low;
	}

	public void setLow(double low) {
		this.low = low;
	}

	public double getClose() {
		return close;
	}

	public void setClose(double close) {
		this.close = close;
	}

	public long getVolume() {
		return volume;
	}

	public void setVolume(long volume) {
		this.volume = volume;
	}

	public double getCloseProjection() {
		return closeProjection;
	}

	public void setCloseProjection(double closeProjection) {
		this.closeProjection = closeProjection;
	}

	public double getLowProjection() {
		return lowProjection;
	}

	public void setLowProjection(double lowProjection) {
		this.lowProjection = lowProjection;
	}

	public double getHighProjection() {
		return highProjection;
	}

	public void setHighProjection(double highProjection) {
		this.highProjection = highProjection;
	}

}


