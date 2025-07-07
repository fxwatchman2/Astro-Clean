package com.kalwit.libFoundation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import swisseph.SweConst;
import swisseph.SweDate;
import swisseph.SwissEph;

public class Utilities {
	// diff is 15 cents or 1 percent of the value, whichever is higher
	public static double THEDIFF = 0.4;
	private static final double EPSILON = 0.0000001d;

	public static final double gcPoint = 266.51;
	public static final double ayanamsha = 24.144;

	public static final long twoWeekDifference = 14 * 24 * 60 * 60 * 1000;

	public static enum RESULT {
		bearishXOver, noChange, bullishXOver
	};

	public static enum TriggerType {
		priceCrossingUp, priceCrossingDown, priceCloseTo, timeIsUp, priceCrossingUpAndDown
	};

	public static double[] planetRotationDays = { 365.24, 29.5, 88.0, 224.0, 687.0, 4332.0, 10795.0, 30688.0, 60182.0,
			90600.0, 6750.0, 6750.0 };

	public static enum PatternType {
		NoResult, approachingDT, DT, DTMonthly, DTWeekly, DTDaily, DB, HighestHigh, LowestLow, HnS, ReverseHnS,
		HighPivots, LowPivots, UptrendDoji, DowntrendHammer, PipeTop, PipeBottom, ATHAndDoji, QuickMoveAndDoji,
		UpTrendAndDoji, ATLAndHammer, QuickMoveAndHammer, DownTrendAndHammer, oversold, overbought, goodForMetonic
	};

	public static enum planets {
		SE_SUN, SE_MOON, SE_MERCURY, SE_VENUS, SE_MARS, SE_JUPITER, SE_SATURN, SE_URANUS, SE_NEPTUNE, SE_PLUTO,
		SE_TRUE_NODE
	};

	public static int planetsList[] = { SweConst.SE_SUN, SweConst.SE_MOON, SweConst.SE_MERCURY, SweConst.SE_VENUS,
			SweConst.SE_MARS, SweConst.SE_JUPITER, SweConst.SE_SATURN, SweConst.SE_URANUS, SweConst.SE_NEPTUNE,
			SweConst.SE_PLUTO, SweConst.SE_TRUE_NODE };

	public static String[] gcPlanetNames = { "Ea", "Mo", "Me", "Ve", "Ma", "Ju", "Sa", "Ur", "Ne", "Pl", "nN", "sN" };

	public static enum gcPlanetNumbers {
		SE_SUN, SE_MOON, SE_MERCURY, SE_VENUS, SE_MARS, SE_JUPITER, SE_SATURN, SE_URANUS, SE_NEPTUNE, SE_PLUTO,
		SE_TRUE_NODE
	};

	public static String[] hcPlanetNames = { "Su", "Mo", "Me", "Ve", "Ma", "Ju", "Sa", "Ur", "Ne", "Pl", "nN", "sN" };

	public static String[] signNames = { "Aries", "Taurus", "Gemini", "Cancer", "Leo", "Virgo", "Libra", "Scorpio",
			"Sagittarius", "Capricorn", "Aquarius", "Pisces" };

	public static String[] shortSignNames = { "Ari", "Tau", "Gem", "Can", "Leo", "Vir", "Lib", "Sco", "Sag", "Cap",
			"Aqua", "Pisc" };

	public static String[] rashiNames = { "Mesh", "Vrishabh", "Mithun", "Kark", "Simha", "Kanya", "Tula", "Vrischik",
			"Dhanu", "Makar", "Kumbh", "Meen" };

	public static String[] shortRashiNames = { "Mes", "Vrs", "Mit", "Kar", "Sim", "Kan", "Tul", "Vrch", "Dha", "Mak",
			"Kum", "Mee" };

	public static String[] nakshatraNames = { "Ashvini", "Bharani", "Krittika", "Rohini", "Mrigashirsha", "Ardra",
			"Punarvasu", "Pushya", "Ashlesha", "Magha", "Purva Phalguni", "Uttara Phalguni", "Hasta", "Chitra", "Swati",
			"Vishakha", "Anuradha", "Jyeshtha", "Mula", "Purva Ashadha", "Uttara Ashadha", "Shravana", "Dhanishtha",
			"Shatabhisha", "Purva Bhadrapada", "Uttara Bhadrapada", "Revati" };

	public static String[] shortNakshatraNames = 
		{ "Ashv", "Bhar", "Krit", "Roh", "Mrig", "Ardr", "Punar", "Push","Ashl", 
				"Magh", "pPha", "uPha", "Hast", "Chit", "Swat", "Vish", "Anur", "Jyes", 
				"Mula", "pAsh", "uAsh", "Shra", "Dhan", "Shat", "pBhad", "uBhad", "Reva", "Abhi"};

	public static String getPlanetName(int planetNum) {
		return hcPlanetNames[planetNum];
	}

	public static String getSignName(int num) {
		return signNames[num];
	}

	public static String getShortSignName(int num) {
		return shortSignNames[num];
	}

	public static String getShortRashiName(int num) {
		return shortRashiNames[num];
	}

	public static String getNakshatraName(int num) {
		return nakshatraNames[num];
	}

	public static String getShortNakName(int num) {
		return shortNakshatraNames[num];
	}

	public static String getTriggerTypeDescription(TriggerType tt) {
		switch (tt) {
		case priceCrossingUp:
			return "Trigger when price is crossing up the target";
		case priceCrossingDown:
			return "Trigger when price is crossing down the target";
		case priceCloseTo:
			return "Trigger when price is close to the target";
		case timeIsUp:
			return "Trigger when specified time is up";
		case priceCrossingUpAndDown:
			return "Trigger when price is crossing up first and then crossing down the target";
		}
		return "";
	}

	public static void logMsg(String msg) {
		System.out.print(msg);
	}

	public static void logMsgLn(String msg) {
		System.out.println(msg);
	}

	public static void logMsg(int msg) {
		System.out.print(msg);
	}

	public static void logMsgLn(int msg) {
		System.out.println(msg);
	}

	// to find out if two weeks have elapsed since an aspect happened.
	// Slow moving planets can cause the same aspect to exist for a long time
	public static boolean diffIsMoreThan2Weeks(long d1, long d2) {
		return ((Math.abs(d2 - d1) >= twoWeekDifference) || (d1 == 0) || (d2 == 0));
	}

	public static String removeToken(String text, String token) {
		String retStr = "";
		StringTokenizer tokenizer = new StringTokenizer(text, token);
		while (tokenizer.hasMoreTokens()) {
			retStr = retStr + tokenizer.nextToken();
		}
		return retStr;
	}

	public static String replaceToken(String text, String token, String replacementToken) {
		String retStr = "";
		int numOfTokens = numOfTokens(text, token);
		int counter = 0;

		StringTokenizer tokenizer = new StringTokenizer(text, token);
		while (tokenizer.hasMoreTokens()) {
			counter++;
			retStr = retStr + tokenizer.nextToken();
			if (counter < numOfTokens)
				retStr = retStr + replacementToken; // dont append the replacementToken after the last token
		}
		return retStr;
	}

	public static int numOfTokens(String text, String token) {
		int numOfTokens = 0;
		StringTokenizer tokenizer = new StringTokenizer(text, token);
		while (tokenizer.hasMoreTokens()) {
			tokenizer.nextToken();
			numOfTokens++;
		}
		return numOfTokens;
	}

	public static void sleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch (Exception e) {

		}
	}

	public static double getDiff(double price) {
		double diff = THEDIFF;
		double percentage = 1.0;
		if ((percentage * price / 100.0) > diff)
			diff = percentage * price / 100.0;
		if ((diff < 0.5) && (price > 30))
			diff = 0.5; // if price is less than 30, do not change the diff more than 1%
		return diff;
	}

	// specified % or 40 cents
	public static double getDiff(double price, double percentage) {
		double diff = THEDIFF;
		if ((percentage * price / 100.0) > diff)
			diff = percentage * price / 100.0;
		return diff;
	}

	public static double getStrictDiff(double price) {
		// only 1% allowed
		// return 1.0*price/100.0;

		// only 15 cents allowed
		return 0.15;
	}

	public static double getAngularDifference(double angle1, double angle2) {
		double difference = (angle1 - angle2) % 360.0;
		if (difference < 0.0)
			difference += 360.0; // If difference is negative, make it positive
		if (difference > 180.0)
			difference -= 360.0; // If difference is greater than 180, take the shorter route
		return Math.abs(difference);
	}

	public static double getOrb(int planet1) {
		return getOrb(planet1, 1000); // second planet is ignored
	}

	public static double getOrb(int planet1, int planet2, int planet3) {
		int referencePlanet = planet1;
		if (planet2 < planet1)
			planet1 = planet2;
		if (planet3 < planet1)
			planet1 = planet3;
		return getOrb(planet1);
	}

	// orb is the angular distance that can be covered in 2 days
	// most of the time market is not closed more than 4 days in a row and
	// orb operates on both sides of the event, therefore 4/2 = 2 days
	public static double getOrb(int planet1, int planet2) {

		if (true) return 1;

		int thePlanet = (planet1 < planet2) ? planet1 : planet2;
		if (0 == thePlanet) {
			return 2; // 2.1; // sun
		} else if (1 == thePlanet) {
			return 5; // 10.0; // moon
		} else if (2 == thePlanet) {
			return 5; // 7.0; // Mercury
		} else if (3 == thePlanet) {
			return 5; // 2.4; // venus
		} else if (4 == thePlanet) {
			return 5; // 1.1; // mars
		} else if (5 == thePlanet) {
			return 5; // 0.18; // jupiter
		} else if (6 == thePlanet) {
			return 5; // 0.07; // saturn
		} else if (7 == thePlanet) {
			return 5; // 0.0235; // uranus
		} else if (8 == thePlanet) {
			return 5; // 0.012; // neptune
		} else if (10 == thePlanet) {
			return 5; // 0.11; // node (N)
		} else if (11 == thePlanet) {
			return 5; // 0.11; // node (S)
		}
		return 5; // 0.012; // in case other planets are used in future
	}

	public static double getOrbForAnHour(int planet1, int planet2) {
		int thePlanet = (planet1 < planet2) ? planet1 : planet2;
		// find the movement in an hour. Use 20% margin for the orb
		return ((360.00 / planetRotationDays[thePlanet]) / 24.0) * 1.2;
	}

	// is p1 close to p2 regardless of higher or lower
	public static boolean almostSame(double p1, double p2, double diff) {
		return Math.abs(p1 - p2) < diff + EPSILON;
	}

	public static boolean almostSame(double p1, double p2) {
		return Math.abs(p1 - p2) < getDiff(p1) + EPSILON;
	}

	// is p1 close to p2 but not higher than p2?
	public static boolean almostSameUpTo(double p1, double p2, double diff) {
		if (p1 > p2)
			return false;
		return Math.abs(p1 - p2) < diff + EPSILON;
	}

	// is p1 close to p2 but not lower than p2?
	public static boolean almostSameDwonTo(double p1, double p2, double diff) {
		if (p1 < p2)
			return false;
		return Math.abs(p1 - p2) < diff + EPSILON;
	}

	public static ArrayList<String> readPoolNameFile(String sourceFileName) {
		// logMsgLn ("Reading names... ");
		File file = new File(sourceFileName);
		BufferedReader reader = null;
		ArrayList<String> tempNamesArray = new ArrayList<String>();

		try {
			reader = new BufferedReader(new FileReader(file));
			String text = null;

			// repeat until all lines is read
			while ((text = reader.readLine()) != null) {
				tempNamesArray.add(text);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Collections.reverse(tempNamesArray);
		return tempNamesArray;
	}

	private static boolean allDigits(String token) {
		boolean allGood = true;
		for (int counter = 0; counter < token.length(); counter++) {
			allGood = allGood && (token.charAt(counter) >= '0' && token.charAt(counter) <= '9');
		}
		return allGood;
	}

	public static String getTodayDateAndTimeForTwelveData() {
		String date = getTodayDate();
		date = date + "T18:00:00";
		return date;
	}
	
	public static String getTodayDate() {
		long dateNum = System.currentTimeMillis() + 24*60*60*1000; // for the next day. Testing to include today in the download limit
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		String date1 = format1.format(dateNum);
		return date1;
	}

	public static int getMonth(long dateNum) {
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		String date1 = format1.format(dateNum);
		return Integer.parseInt(date1.substring(7, 9));
	}

	public static String getDisplayDate(long dateNum) {
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		String date1 = format1.format(dateNum);
		return date1;
	}

	public static String getDisplayTime(long dateNum) {
		String date = getDisplayDate(dateNum);
		long dateMillis = getTimeInMillis(date);
		long timeMillis = dateNum - dateMillis;
		long hours = timeMillis/(60*60*1000); 
		long mins =  (timeMillis - hours*60*60*1000)/(60*1000);
		String hoursStr = "";
		String minsStr = "";
		if (hours < 10) hoursStr = "0" + hours; 
		if (mins < 10) minsStr = "0" + mins;

		hoursStr = hoursStr + hours; 
		minsStr = minsStr + mins; 

		return hoursStr + ":" + minsStr;
	}

	public static String getDateTimeStr(String dateTimeString) {
		String date = dateTimeString.substring(0, 10);
		String time = dateTimeString.substring(11, 16);

		return date + ":" + time;
	}

	public static String getOnlyTimeStr(String dateTimeString) {
		String time = dateTimeString.substring(11, 16);
		return time;
	}

	public static String getDisplayDateTime(long dateNum) {
		return getDisplayDate(dateNum) + ":" + getDisplayTime(dateNum); 
	}

	// NOT working properly
	public static String getDisplayDateTimeOld(long dateNum) {
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd:HH:MM");
		String date1 = format1.format(dateNum);
		return date1;
	}

	public static String getNextDayDisplayDateTime(long dateNum) {
		dateNum = dateNum + 24 * 60 * 60 * 1000;
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd:HH:MM");
		String date1 = format1.format(dateNum);
		return date1;
	}

	// This gives you the date as a long number of days before today
	public static String getAnotherDisplayDateOnly(long daysBefore) {
		long dateNum = System.currentTimeMillis();
		daysBefore = daysBefore * 24 * 60 * 60 * 1000;
		dateNum = dateNum - daysBefore; 
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		String date1 = format1.format(dateNum);
		return date1;
	}

	public static String getDisplayDateTime(SweDate sweDateNum) {
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd:HH:MM");
		String date1 = format1.format(getTimeInMillisDateAndTime(sweDateNum));
		return date1;
	}

	public static String getDisplayDate(SweDate sweDateNum) {
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		String date1 = format1.format(getTimeInMillisDateAndTime(sweDateNum));
		return date1;
	}

	public static String getDisplayDateTime(double dblDateNum) {
		return getDisplayDateTime(getNewSWEDateAndTime(dblDateNum, 0));
	}

	public static String getDisplayDate(double dblDateNum) {
		return getDisplayDate(getNewSWEDateAndTime(dblDateNum, 0));
	}

	public static String getDisplayDateTimeyyyyMMdd(long dateNum) {
		SimpleDateFormat format1 = new SimpleDateFormat("yyyyMMdd:HH:MM");
		String date1 = format1.format(dateNum);
		return date1;
	}

	// "2016-07-22:10:30"
	public static double getJulianDay(String dateTime) {
		long millis = getTimeInMillis(dateTime);
		return getSweDate(millis, 0.0).getJulDay();
	}

	/*
	 * Fix # 2017/1 ============ When long date is converted to SweDate using
	 * calendar, it goes back one month because GregorianCalendar uses 0 based
	 * month. Therefore we are incrementing month by 1 when creating SweDate And
	 * reducing month by 1 when converting it back into a GregorianCalendar date to
	 * get date a milliSeconds
	 */
	public static long getTimeInMillisDateAndTime(SweDate sd) {
		int hour = (int) sd.getHour();
		int min = (int) ((sd.getHour() - hour) * 60);
		long millisToAdd = hour * 60 * 60 * 1000 + min * 60 * 1000;
		// Calendar cal = new
		// GregorianCalendar(1900+sd.getYear(),sd.getMonth(),sd.getDay());
		// System.out.println("sd.getMonth() = " + sd.getMonth());
		// test
		int month = sd.getMonth() - 1;
		Calendar cal = new GregorianCalendar(sd.getYear(), month, sd.getDay());
		return millisToAdd + cal.getTimeInMillis();
	}

	// Fix # 2017/1
	public static SweDate getNewSWEDateAndTime(SweDate sd, int minutesToAdd) {
		int hour = (int) sd.getHour();
		int min = (int) ((sd.getHour() - hour) * 60);
		long millisToAdd = hour * 60 * 60 * 1000 + (min + minutesToAdd) * 60 * 1000;
		int month = sd.getMonth() - 1;
		Calendar cal = new GregorianCalendar(sd.getYear(), month, sd.getDay());
		return getSweDate(millisToAdd + cal.getTimeInMillis());
	}

	public static SweDate getNewSWEDateAndTime(long millis, int minutesToAdd) {
		double totalHoursToAdd = (minutesToAdd * 1.0) / 60.0;
		return getSweDate(millis, totalHoursToAdd);
	}

	public static SweDate getNewSWEDateAndTime(double dblDate, int minutesToAdd) {
		long longDate = (long) dblDate;
		double totalHoursToAdd = ((dblDate - longDate) * 24.0) + ((minutesToAdd * 1.0) / 60.0);
		return getSweDate(longDate * 1000, totalHoursToAdd);
	}

	// yyyy-MM-dd:HH:MM
	public static long getTimeInMillisDateAndTime(String strDate) {
		String strYear = strDate.substring(0, 4);
		String strMonth = strDate.substring(5, 7);
		String strDay = strDate.substring(8, 10);
		String strHr = strDate.substring(11, 13);
		String strMin = strDate.substring(14,16);

		//strMin = String.format("%02d", Integer.parseInt(strMin)); 

		long millisToAdd = (long)Integer.parseInt(strHr) * 60 * 60 * 1000 + (long)Integer.parseInt(strMin) * 60 * 1000;
		Calendar cal = new GregorianCalendar(Integer.parseInt(strYear), Integer.parseInt(strMonth) - 1,
				Integer.parseInt(strDay));
		return millisToAdd + cal.getTimeInMillis();
	}


	// yyyy-MM-dd:HH:MM
	public static long getTimeInMillisForJustTime(String strDate) {
		String strHr = strDate.substring(11, 13);
		String strMin = strDate.substring(14,16);

		long millis = (long)Integer.parseInt(strHr) * 60 * 60 * 1000 + (long)Integer.parseInt(strMin) * 60 * 1000;
		return millis;
	}


	// yyyy-MM-dd
	public static long getTimeInMillis(String strDate) {
		String strYear = strDate.substring(0, 4);
		String strMonth = strDate.substring(5, 7);
		String strDay = strDate.substring(8, 10);

		Calendar cal = new GregorianCalendar(Integer.parseInt(strYear), Integer.parseInt(strMonth) - 1,
				Integer.parseInt(strDay));
		return cal.getTimeInMillis();
	}

	// To be used for getting 38,30,27,19,11 and 8 year dates for metonic cycles
	public static String getDisplayDateForAnotherYear(String strDate, int yearsAgoOrAfter) { // negative for past,
		// positive for future
		long millis = getTimeInMillisForAnotherYear(strDate, yearsAgoOrAfter);
		return getDisplayDate(millis);
	}

	// NOT working yet
	public static String getDisplayDateForDaysBefore(String strDate, int numOfDays) {
		long millis = getTimeInMillisForDays(strDate, numOfDays);
		return getDisplayDate(millis);
	}

	// yyyy-MM-dd
	public static long getTimeInMillisForAnotherYear(String strDate, int yearsAgoOrAfter) { // negative for past,
		// positive for future
		String strYear = strDate.substring(0, 4);
		String strMonth = strDate.substring(5, 7);
		String strDay = strDate.substring(8, 10);

		int newYear = Integer.parseInt(strYear) + yearsAgoOrAfter;

		Calendar cal = new GregorianCalendar(newYear, Integer.parseInt(strMonth) - 1, Integer.parseInt(strDay));
		return cal.getTimeInMillis();
	}

	public static long getTimeInMillisForDays(String strDate, int numOfDays) { // negative for past, positive for future
		String strYear = strDate.substring(0, 4);
		String strMonth = strDate.substring(5, 7);
		String strDay = strDate.substring(8, 10);

		long millisToSubtract = (long) (numOfDays * 24 * 60 * 60 * 1000);

		Calendar cal = new GregorianCalendar(Integer.parseInt(strYear), Integer.parseInt(strMonth) - 1,
				Integer.parseInt(strDay));
		return (cal.getTimeInMillis() - millisToSubtract);
	}

	public static long getTimeInMillisForDays(long millis, long numOfDays) {
		long millisToAdd = numOfDays * 24 * 60 * 60 * 1000;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date(millis));
		return millisToAdd + calendar.getTimeInMillis();
	}

	public static long getTimeInMillis(long millis, double hourAndMin) {
		long millisToAdd = (long) (hourAndMin * 60 * 60 * 1000);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date(millis));
		return millisToAdd + calendar.getTimeInMillis();
	}

	// ignore the time part of the date string and add the hour separately. Useful
	// in getting an exact date time without knowing the time part of original date
	// yyyy-MM-dd:HH:MM
	public static long getTimeInMillis(String strDate, double hourAndMin) {
		String strYear = strDate.substring(0, 4);
		String strMonth = strDate.substring(5, 7);
		String strDay = strDate.substring(8);

		Calendar cal = new GregorianCalendar(Integer.parseInt(strYear), Integer.parseInt(strMonth) - 1,
				Integer.parseInt(strDay));
		int hourPartOfHourAndMin = (int) hourAndMin;
		int minPartOfHourAndMin = (int) ((hourAndMin - hourPartOfHourAndMin) * 60);

		long millisToAdd = hourPartOfHourAndMin * 60 * 60 * 1000 + minPartOfHourAndMin * 60 * 1000;
		return millisToAdd + cal.getTimeInMillis();
	}

	// Fix # 2017/1
	public static SweDate getSweDate(long dateNum, double hour) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date(dateNum));

		int year = calendar.get(Calendar.YEAR);
		int month = 1 + calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		double newHour = hour + calendar.get(Calendar.HOUR) + (1.0 * calendar.get(Calendar.MINUTE) / 60.0);

		SweDate sd = new SweDate(year, month, day, newHour);
		return sd;
	}

	// Fix # 2017/1
	public static SweDate getSweDate(long dateNum) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date(dateNum));

		int year = calendar.get(Calendar.YEAR);
		int month = 1 + calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);

		double hour = calendar.get(Calendar.HOUR) + (1.0 * calendar.get(Calendar.MINUTE) / 60.0);
		SweDate sd = new SweDate(year, month, day, hour);
		return sd;
	}

	public static String getDblStr(double num) {
		NumberFormat formatter = new DecimalFormat("#0.00");
		String numStr = formatter.format(num);
		return numStr;
	}

	public static String getDblIntStr(double num) {
		NumberFormat formatter = new DecimalFormat("#0");
		String numStr = formatter.format(num);
		return numStr;
	}



	/*
	 * public static FileReader openFile(String tickerPath, boolean forReading) {
	 * FileReader fr = null; try { fr = new FileReader (tickerPath); // ticker_ds
	 * (for data set) } catch (IOException e) { // log and terminate } return fr; }
	 */

	public static String adjustFutureDate(String dateStr,int daysForward) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate date = LocalDate.parse(dateStr, formatter);

		date = date.plusDays(daysForward);

		// Adjust if the date falls on a weekend
		if (date.getDayOfWeek() == DayOfWeek.SATURDAY) {
			date = date.plusDays(2); // Move to Monday
		} else if (date.getDayOfWeek() == DayOfWeek.SUNDAY) {
			date = date.plusDays(1); // Move to Monday
		}

		return date.format(formatter);
	}

	public static String adjustPastDate(String dateStr,int daysBack) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate date = LocalDate.parse(dateStr, formatter);

		date = date.minusDays(daysBack); 
		
		// Adjust if the date falls on a weekend
		if (date.getDayOfWeek() == DayOfWeek.SUNDAY) {
			date = date.minusDays(2); // Go back 2 days to Friday
		} else if (date.getDayOfWeek() == DayOfWeek.SATURDAY) {
			date = date.minusDays(1); // Go back 1 day to Friday
		}

		return date.format(formatter);
	}

	public static String getDateStringAfterDays(String currentDate, int numOfDays) {
		long millisInaDay = 24 * 60 * 60 * 1000;
		long time1 = Utilities.getTimeInMillis(currentDate);
		time1 = time1 + numOfDays * millisInaDay;
		return getDisplayDate(time1);
	}

	// format yyyy-MM-dd
	public static String getNextDateString(String currentDate) {
		final int february = 1; // second month, in a 0 based array
		int daysOfMonths[] = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		String date1 = format1.format(Utilities.getTimeInMillis(currentDate));

		int runningYear = Integer.parseInt(date1.substring(0, 4));
		int runningMonth = Integer.parseInt(date1.substring(5, 7));
		int runningDay = Integer.parseInt(date1.substring(8));
		;

		if ((runningYear % 4) == 0)
			daysOfMonths[february] = 29;
		else
			daysOfMonths[february] = 28;

		runningDay++;
		if (runningDay > daysOfMonths[runningMonth - 1]) {
			runningDay = 1;
			runningMonth++;
			if (runningMonth == 13) {
				runningMonth = 1;
				runningYear++;
				if ((runningYear % 4) == 0)
					daysOfMonths[february] = 29;
				else
					daysOfMonths[february] = 28;
			}
		}

		String prefix = "";
		if (runningYear < 100) {
			prefix = "20";
		}
		String dateString = prefix + runningYear + "-";
		prefix = "";
		if (runningMonth < 10) {
			prefix = "0";
		}
		dateString = dateString + prefix + runningMonth + "-";
		prefix = "";
		if (runningDay < 10) {
			prefix = "0";
		}
		dateString = dateString + prefix + runningDay;

		return dateString;
	}

	public static void writeFile(String name, char[] contents) {
		FileWriter fw = openFile(name);
		writeBLOB(fw, contents);
		closeFile(fw);
	}

	/*
	 * How to use
	 * 
	 * FileWriter fw = Utilties.openFile(""); Utilties.writeLine(fw,"");
	 * Utilties.closeFile(fw);
	 */
	public static FileWriter openFile(String tickerPath) {
		FileWriter fw = null;
		try {
			fw = new FileWriter(tickerPath); // ticker_ds (for data set)
		} catch (IOException e) {
			// log and terminate
		}
		return fw;
	}

	public static void closeFile(FileWriter fw) {
		try {
			fw.flush();
			fw.close();
		} catch (IOException e) {
			// log and terminate
		}
	}

	public static void writeLine(FileWriter fw, String line) {
		try {
			fw.write(line);
			fw.write(13);
			fw.write(10);
		} catch (IOException e) {
			// log and terminate
		}
	}

	public static void writeBLOB(FileWriter fw, char[] contents) {
		try {
			fw.write(contents);
		} catch (IOException e) {
			// log and terminate
		}
	}

	public static boolean haveCrossedDifference(double degree1, double degree2, double aspectDifference) {
		double angularDistance = Math.abs(degree1 - degree2);
		return (angularDistance > aspectDifference);
	}

	public static boolean areInAspect(double degree1, double degree2, double aspectDegrees, double orb) {
		double angularDistance = Math.abs(degree1 - degree2);
		return almostSame(angularDistance, aspectDegrees, orb);
	}

	public static boolean isWithinHalfDegree(double degree1, double degree2) {
		return ((degree1 > degree2 - 0.25) && (degree1 < degree2 + 0.25));
	}

	public static boolean areConjunct(double degree1, double degree2, double orb) {
		return almostSame(degree1, degree2, orb);
	}

	public static boolean areTrine(double degree1, double degree2, double orb) {
		double angularDistance = Math.abs(degree1 - degree2);
		return almostSame(angularDistance, 120.0, orb);
	}

	public static boolean areOpposed(double degree1, double degree2, double orb) {
		double angularDistance = Math.abs(degree1 - degree2);
		return almostSame(angularDistance, 180.0, orb);
	}

	public static boolean areSquared(double degree1, double degree2, double orb) {
		double angularDistance = Math.abs(degree1 - degree2);
		return almostSame(angularDistance, 90.0, orb);
	}

	public static boolean areAtAngle(double desiredAngle, double d1, double d2, double orb) {
		if ((d2 - d1) <= (desiredAngle + orb)) {
			if ((d2 - d1) >= desiredAngle) {
				return true;
			}
		}
		return false;
	}

	public static boolean areAtAngleOf30(double degree1, double degree2, double orb) {
		double angularDistance = Math.abs(degree1 - degree2);
		return almostSame(angularDistance, 30.0, orb);
	}

	public static boolean areAtAngleOf45(double degree1, double degree2, double orb) {
		double angularDistance = Math.abs(degree1 - degree2);
		return almostSame(angularDistance, 45.0, orb);
	}

	public static boolean areAtAngleOf60(double degree1, double degree2, double orb) {
		double angularDistance = Math.abs(degree1 - degree2);
		return almostSame(angularDistance, 60.0, orb);
	}

	public static boolean areAtAngleOf150(double degree1, double degree2, double orb) {
		double angularDistance = Math.abs(degree1 - degree2);
		return almostSame(angularDistance, 150.0, orb);
	}

	public static boolean areAtAngleOf210(double degree1, double degree2, double orb) {
		double angularDistance = Math.abs(degree1 - degree2);
		return almostSame(angularDistance, 210.0, orb);
	}

	public boolean findDT(ArrayList<Double> closingPrices, int minDistanceRequired) {
		double cp = closingPrices.get(closingPrices.size() - 1);
		double margin = 0.5;
		System.out.println("cp " + cp);
		for (int counter = 0; counter < closingPrices.size() - 1; counter++) {
			if (closingPrices.get(counter) > (cp + margin))
				return false;
		}

		// System.out.println("First hurdle cleared...:");

		boolean matchFound = false;
		boolean matchingPrice = false;
		for (int counter = 0; counter < closingPrices.size() - 1; counter++) {
			matchingPrice = almostSame(closingPrices.get(counter), cp);
			if ((matchingPrice) && ((closingPrices.size() - counter) > minDistanceRequired)) {
				matchFound = true;
				break;
			}
		}
		// System.out.println("matchingPrice " + matchingPrice);
		return matchFound;
	}


	public static double getDouble(String dblString) {
		double returnVal = 0;
		try {
			returnVal = Double.parseDouble(dblString);
		} catch (Exception e) {

		}
		return returnVal;
	}

	public static int getInt(String intString) {
		int returnVal = 0;
		try {
			returnVal = Integer.parseInt(intString);
		} catch (Exception e) {

		}
		return returnVal;
	}


	public static boolean withinLimits(double desiredAngle, double d1, double d2, double orb) {
		if ((d2 - d1) <= (desiredAngle + orb)) {
			if ((d2 - d1) >= desiredAngle) {
				return true;
			}
		}
		return false;
	}

	public static boolean areAtAngleForming(int aN, double a1, double a2, double orbN) {
		double difference = a2 - a1;
		if (difference < 0) {
			difference += 360;
			return (difference > 0) && (difference <= aN + orbN) && (difference >= aN);
		}
		return (difference > 0) && (difference >= aN - orbN) && (difference <= aN);
	}

    public static String dropTailEnd(String input,String toDrop) {
        // Check if the input string ends with "EventData"
        if (input != null && input.endsWith(toDrop)) {
            // If it does, return the substring without "EventData"
            return input.substring(0, input.length() - toDrop.length());
        }
        // If it doesn't end with "EventData", return the original string
        return input;
    }
    
	public static int extractDigits(String input) {
		Pattern pattern = Pattern.compile("\\b\\d{1,2}\\b");
		Matcher matcher = pattern.matcher(input);

		if (matcher.find()) {
			return Integer.parseInt(matcher.group());
		} else {
			return 0;
		}
	}


	public static List<String> listAllFiles(String folderPath) {
		List<String> filePaths = new ArrayList<>();
		File folder = new File(folderPath);

		if (!folder.exists() || !folder.isDirectory()) {
			throw new IllegalArgumentException("Invalid folder path: " + folderPath);
		}

		scanFolder(folder, filePaths);
		return filePaths;
	}

	// -----------------------------------------------------------------------------------------------------------------------------------------------------------

	private static void scanFolder(File folder, List<String> filePaths) {
		File[] files = folder.listFiles();
		if (files != null) {
			for (File file : files) {
				if (file.isFile()) {
					filePaths.add(file.getAbsolutePath());
				} else if (file.isDirectory()) {
					scanFolder(file, filePaths); // Recursive call for subfolders
				}
			}
		}
	}

	// -----------------------------------------------------------------------------------------------------------------------------------------------------------
	
    public static void main(String[] args) {

    	int pivotTimePeriod = 5;
    	//String correctPastDate = Utilities.adjustPastDate("2024-12-17",pivotTimePeriod +((1+pivotTimePeriod)/5)*2);
    	String correctPastDate = Utilities.adjustPastDate("2024-12-17",pivotTimePeriod);
    	
    	System.out.println(correctPastDate);

    	if (true) return;
    	
		// TODO: System.out.println("Should be true: " + areAtAngleForming(0.0,
		// 359,1,2));
		
		System.out.println(getDateStringAfterDays("2024-12-17", -15));
		System.out.println(getDateStringAfterDays("2024-12-17", 15));
		
		if (true) return;
		
		System.out.println("Should be true: " + areAtAngleForming(120, 319, 80, 2));
		System.out.println("Should be false: " + areAtAngleForming(120, 315, 80, 2));
		System.out.println("Should be true: " + areAtAngleForming(120, 80, 200, 2));
		System.out.println("Should be false: " + areAtAngleForming(120, 0, 121, 2));
		System.out.println("Should be true: " + areAtAngleForming(90, 100, 189, 2));
		System.out.println("Should be false: " + areAtAngleForming(90, 100, 191, 2));

		if (true) return;

		String startDateString = "1980-01-01";
		String endDateString = "2051-12-31";
		int hour = 0;

		SweDate sdStart = Utilities.getSweDate(Utilities.getTimeInMillis(startDateString), hour);
		SweDate sdEnd = Utilities.getSweDate(Utilities.getTimeInMillis(endDateString), hour);

		if (true)
			return;

		long t1 = System.currentTimeMillis();
		String ticker = "hpq";
		boolean yahooFormat = true;
		// ArrayList<RowRecord> tickerData = getTickerData(ticker,yahooFormat);
		long t2 = System.currentTimeMillis();

		String todayDate = Utilities.getTodayDate();
		System.out.println(todayDate);

		t1 = getTimeInMillis(todayDate);
		todayDate = Utilities.getDisplayDate(t1);
		System.out.println(todayDate);

		SweDate sd = getSweDate(t1, 0);
		t2 = getTimeInMillisDateAndTime(sd);
		todayDate = Utilities.getDisplayDate(t2);
		System.out.println(todayDate);

		System.out.println("t1 = " + t1);
		System.out.println("t2 = " + t2);
		System.out.println("t2-t1 = " + (t2 - t1));

		System.out.println("38 year old date = " + getDisplayDateForDaysBefore("2017-01-01", 13879));
		System.out.println("30 year old date = " + getDisplayDateForAnotherYear("2017-01-01", -38));

		System.out.println("2016-09-30    " + Utilities.getNextDateString("2016-09-30"));
		System.out.println("2016-10-09    " + Utilities.getNextDateString("2016-10-09"));
		System.out.println("2016-12-31    " + Utilities.getNextDateString("2016-12-31"));
		System.out.println("1999-12-31    " + Utilities.getNextDateString("1999-12-31"));
		System.out.println("1990-02-28    " + Utilities.getNextDateString("1990-02-28"));
		System.out.println("1988-02-28    " + Utilities.getNextDateString("1988-02-28"));

		long t11 = getTimeInMillis("2016-07-21");
		System.out.println("1980-03-17");

		System.out.println("****************** 1139558400000=   " + Utilities.getDisplayDate(1139558400));
		System.out.println("****************** 2016-07-21=   " + Utilities.getDisplayDate(t11));
		System.out.println("");

		long diffMillis = getTimeInMillisDateAndTime("1970-07-21:10:30");
		// if (true) return;
		System.out.print("2016-07-21:10:30");
		long millis = getTimeInMillisDateAndTime("2016-07-21:10:30");
		System.out.println(" " + (millis - diffMillis) / 10000000000.0);

		System.out.print("2016-07-22:10:30");
		millis = getTimeInMillisDateAndTime("2016-07-22:10:30");
		System.out.println(" " + (millis - diffMillis) / 10000000000.0);

		System.out.print("2016-07-23:10:30");
		millis = getTimeInMillisDateAndTime("2016-07-23:10:30");
		System.out.println(" " + (millis - diffMillis) / 10000000000.0);

		System.out.print("getDisplayDateTime =");
		System.out.println(getDisplayDateTime(millis));

		// double itemDouble = 1370437809.00;
		double itemDouble = 2463007.6763422433;
		SweDate d1 = getNewSWEDateAndTime(itemDouble, 0);
		System.out.println("getDisplayDateTime(d1) = " + getDisplayDateTime(d1));

		SweDate d2 = getNewSWEDateAndTime(itemDouble, 120);
		System.out.println("getDisplayDateTime(d2) = " + getDisplayDateTime(d2));

		SweDate d3 = getNewSWEDateAndTime(itemDouble, 1440);
		System.out.println("getDisplayDateTime(d3) = " + getDisplayDateTime(d3));

		System.out.println("--------------------------------------------------------------");
		System.out.println("2016-07-22:10:30");
		millis = getTimeInMillisDateAndTime("2016-07-22:10:30");
		sd = getSweDate(millis, 0);
		System.out.println("getDisplayDateTime(sd) = " + getDisplayDateTime(sd));
	}
}