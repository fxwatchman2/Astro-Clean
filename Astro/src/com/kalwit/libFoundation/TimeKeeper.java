package com.kalwit.libFoundation;

public class TimeKeeper {
	long startTime = 0;
	
	public TimeKeeper() {
		startTime = System.currentTimeMillis();
	}

	public void resetBeginning() {
		startTime = System.currentTimeMillis();
	}
	
	public long millis() {
		long currentTime = System.currentTimeMillis();
		return (currentTime-startTime);
	}

	public void print() {
		long currentTime = System.currentTimeMillis();
		System.out.print("Time Taken:\t" + (currentTime-startTime) + " Millis\t Or " + (currentTime-startTime)/1000 + " Seconds");
	}

	public void println() {
		print();
		System.out.println();
	}
	
	public void printReset() {
		print();
		resetBeginning();
	}

	public void printlnReset() {
		println();
		resetBeginning();
	}
}
