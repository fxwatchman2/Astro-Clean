package com.kalwit.services;

import java.util.ArrayList;
import java.util.List;

import com.kalwit.libFoundation.PlanetTuple;
import com.kalwit.libFoundation.SwissHelper;
import com.kalwit.libFoundation.TimeKeeper;
import com.kalwit.libFoundation.Utilities;

import swisseph.SweConst;

/* 
	++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	AstroServices contains all the entry points Java services called by the REST services group 
	++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
*/

public class AstroService {

	private List<String> getDates(List<PlanetTuple> pTuples) {
		List<String> dateList = new ArrayList<String>(); 
		
		for (PlanetTuple planetTuple : pTuples) {
			String d = Utilities.getDisplayDateTime(planetTuple.date);
			dateList.add(d.substring(0,10));
		}
		
		return dateList;
	}
	
	public List<String> getDegreeSeparation(int p1, int p2, String startDateTime, String endDateTime, double degrees) {
		System.out.println("[getDegreeSeparation] Called with: p1=" + p1 + ", p2=" + p2 + ", startDateTime=" + startDateTime + ", endDateTime=" + endDateTime + ", degrees=" + degrees);
		List<String> resultList = null; 
		long startTime = Utilities.getTimeInMillis(startDateTime.substring(0, 10));
		long endTime = Utilities.getTimeInMillis(endDateTime.substring(0,10));
		
		// swap if dates are not in order
		if (startTime > endTime) {
			String tempStr = startDateTime;
			startDateTime = endDateTime;
			endDateTime = tempStr;
			System.out.println("[getDegreeSeparation] Swapped dates: startDateTime=" + startDateTime + ", endDateTime=" + endDateTime);
		}
		
		int flags = SweConst.SEFLG_SWIEPH | SweConst.SEFLG_TRANSIT_LONGITUDE;
		
		SwissHelper sh = new SwissHelper();
		List<PlanetTuple> workingList = null;
	
		workingList = sh.processAnyAspectUsingTC(p1, p2, degrees, flags, startDateTime.substring(0, 10), endDateTime.substring(0,10));
		System.out.println("[getDegreeSeparation] workingList size: " + (workingList == null ? 0 : workingList.size()));
		if (workingList != null && workingList.size() > 0) {
			System.out.println("[getDegreeSeparation] Sample PlanetTuple: " + workingList.get(0));
		}
		resultList = getDates(workingList);
		System.out.println("[getDegreeSeparation] resultList size: " + (resultList == null ? 0 : resultList.size()));
		if (resultList != null && resultList.size() > 0) {
			System.out.println("[getDegreeSeparation] Sample result date: " + resultList.get(0));
		}
		return resultList;
	}
	
	List<String> getConjunctions(int p1, int p2, String startDateTime, String endDateTime) {
		double conjunctionDegrees = 0.0;
		return getDegreeSeparation(p1, p2, startDateTime, endDateTime, conjunctionDegrees);
	}

	List<String> getOpposition(int p1, int p2, String startDateTime, String endDateTime) {
		double oppositionDegrees = 180.0;
		return getDegreeSeparation(p1, p2, startDateTime, endDateTime, oppositionDegrees);
	}

	List<String> getSquare(int p1, int p2, String startDateTime, String endDateTime) {
		double squareDegrees = 90.0;
		return getDegreeSeparation(p1, p2, startDateTime, endDateTime, squareDegrees);
	}

	List<String> getTrine(int p1, int p2, String startDateTime, String endDateTime) {
		double trineDegrees = 120.0;
		return getDegreeSeparation(p1, p2, startDateTime, endDateTime, trineDegrees);
	}
	
	public static void test() {
		TimeKeeper tk = new TimeKeeper();
		double degrees = 15.0;
		AstroService astroServices = new AstroService();
		//List<String> dates = astroServices.getConjunctions(SweConst.SE_MERCURY,SweConst.SE_VENUS,"1930-01-01","2031-01-01");
		//List<String> dates = astroServices.getDegreeSeparation(SweConst.SE_SUN,SweConst.SE_MERCURY,"1930-01-01","2031-01-01",degrees);
		List<String> dates = astroServices.getDegreeSeparation(SweConst.SE_SUN,SweConst.SE_MOON,"2031-01-01","1930-01-01",degrees);

		tk.println();
		System.out.println("dates.size() = " + dates.size());
	}
	
	public static void main(String[] args) {
		test();
	}
}
