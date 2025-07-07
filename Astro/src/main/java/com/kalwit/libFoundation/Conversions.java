package com.kalwit.libFoundation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import swisseph.SweDate;

public class Conversions {
	private static long oneDayMillis = 24 * 60 * 60 * 1000;

	final static double auToKm = 149597870.7;	
	final static int planetSun = 0;
	final static int planetMoon = 1;

	public final static long millisInAMinute = 1 * 60 * 1000;
	public final static long millisInAnHour = 1 * 60 * 60 * 1000;
	public final static long millisInADay = 24 * millisInAnHour;
	public final static long millisInAWeek = 7 * millisInADay;
	public final static long millisInAMonth = 30 * millisInADay;

	public final static double AU2MillionMiles = 93.0;
	
	public static String[] planetNames = {
			"Su","Mo","Me","Ve","Ma","Ju","Sa","Ur","Ne","Pl","Ra","Ke"
	};

	public static String[] signNames = {
			"Aries","Taurus","Gemini","Cancer","Leo","Virgo","Libra","Scorpio",
			"Sagittarius","Capricorn","Aquarius","Pisces"
	};
	public static String[] rashiNames = {
			"Mesh","Vrish","Mithun","Kark","Simha","Kanya","Tula","Vrisc","Dhanu",
			"Makar","Kumbh","Meen"
	};

	public static String[] fullRashiNames = {
			"Mesh","Vrishabh","Mithun","Kark","Simha","Kanya","Tula","Vrischik","Dhanu",
			"Makar","Kumbh","Meen"
	};
	public static String[] nakshatraNames = {
			"Ashvini","Bharani","Krittika","Rohini","Mrigashirsha","Ardra","Punarvasu","Pushya",
			"Ashlesha","Magha","Purva Phalguni","Uttara Phalguni","Hasta","Chitra","Swati",
			"Vishakha","Anuradha","Jyeshtha","Mula","Purva Ashadha","Uttara Ashadha","Shravana",
			"Dhanishtha","Shatabhisha","Purva Bhadrapada","Uttara Bhadrapada","Revati"};
	
	public static String getDayOfTheWeek(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        String[] days = {"Sun", "Mon", "Tues", "Wed", "Thurs", "Fri", "Sat"};
        return days[dayOfWeek-1];
    }	
	
	public static int getDayOfTheWeekNum(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        return dayOfWeek;
    }	
	
	public static long getMillis(long millis,double hours) {
		return millis + (long) (hours*60*60*1000);
	}

	public static SweDate getSweDate(long millis,double hours) {
		return getSweDate(getMillis(millis,hours));
	}
	
	public static long getPreviousDayMillis(long millis) {
		return millis - oneDayMillis;
	}
	
	public static long getNextDayMillis(long millis) {
		return millis + oneDayMillis;
	}

	// yyyy-MM-dd
	public static long getDateMillis(String strDate) {
		String strYear = strDate.substring(0, 4);
		String strMonth = strDate.substring(5, 7);
		String strDay = strDate.substring(8, 10);

		Calendar cal = new GregorianCalendar(Integer.parseInt(strYear), Integer.parseInt(strMonth) - 1,
				Integer.parseInt(strDay));
		return cal.getTimeInMillis();
	}

	public static int getNumOfCalendarDays(long d1, long d2) {
		return getNumOfCalendarDaysInternal(d1, d2, false);
	}
	
	public static int getNumOfWorkingDays(long d1, long d2) {
		return getNumOfCalendarDaysInternal(d1, d2, true);
	}
	
	public static int getNumOfCalendarDaysInternal(long d1, long d2, boolean onlyWorkingDays) {
		int weekEndDaysToSubtract = 0;
		long dSmall, dLarge;
		if (d1 < d2) {
			dSmall = d1;
			dLarge = d2;
		} else {
			dSmall = d2;
			dLarge = d1;
		}
		
		for (long dayCounter= dSmall; dayCounter<dLarge; dayCounter+=millisInADay) {
			if (getDayOfTheWeekNum(dayCounter)==0) {
				weekEndDaysToSubtract++;		
			}
			if (getDayOfTheWeekNum(dayCounter)==6) {
				weekEndDaysToSubtract++;
			}
		}
		
		long diff = Math.abs(d2-d1);
		if (onlyWorkingDays) return ((int) (diff/millisInADay)-weekEndDaysToSubtract);
		return (int) (diff/millisInADay);
	}
	
	public static String getPreviousDate(String dateStr) {
		long millis = getDateMillis(dateStr);
		millis -= oneDayMillis;
		return getDisplayDateTime(millis);
	}
	
	public static double getAbsoluteDiff(double d1, double d2) {
		double diff = Math.abs(d2-d1);
		if (diff>180) diff = 360-diff;
		return diff;
	}
	
	public static SweDate getNextDaySweDate(SweDate sweDate) {
		double hours = 24;
		return getSweDate(getTimeInMillis(sweDate),hours);
	}
	
	public static long getTimeInMillis(SweDate sweDate) {
		return getMillis(sweDate.getJulDay());
	}
	
	public static SweDate getSweDate(long millis) {
		Date date = new Date(millis);
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1; //Add one to month {0 - 11}
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		double hour = calendar.get(Calendar.HOUR_OF_DAY);
		double minute = calendar.get(Calendar.MINUTE);
		hour = hour + (minute/60.0);
		SweDate sweDate = new SweDate(year,month,day,hour); 
		return sweDate;
	}

	public static long getMillis(double julian) {
		SweDate sweDate = new SweDate(julian);
		Calendar calendar = new GregorianCalendar();
		
		calendar.set(Calendar.YEAR,sweDate.getYear());
		calendar.set(Calendar.MONTH,sweDate.getMonth()-1); //Subtract one from month {0 - 11}
		calendar.set(Calendar.DAY_OF_MONTH,sweDate.getDay());
		double hour = sweDate.getHour();
		int intHour = (int) hour;
		int minute = (int) ((hour - intHour) * 60); 
		calendar.set(Calendar.HOUR_OF_DAY,intHour);
		calendar.set(Calendar.MINUTE,minute);
		long millisecond = calendar.getTimeInMillis();
		return millisecond;
	}

	
	public static void test() {
		String myDate = "2014-10-29:18:10";
		long millis = getTimeInMillisDateAndTime(myDate);
		System.out.println(getDisplayDateTime(millis));
		
		Date date = new Date(millis);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd:HH:MM:SS");	
		System.out.println(sdf.format(date));		
	}

	public static String getDisplayDateTime(double doubleDate) {
		return getDisplayDateTime(getMillis(doubleDate));
	}

	public static String getDisplayDateTime(SweDate sweDate) {
		return getDisplayDateTime(getTimeInMillis(sweDate));		
	}

	public static String getDisplayDateTime(long dateNum) {
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd:HH:mm");
		String date1 = format1.format(dateNum);   			
		return date1;
	}

	public static String getDisplayDate(double doubleDate) {
		return getDisplayDate(getMillis(doubleDate));
	}

	public static String getDisplayDate(SweDate sweDate) {
		return getDisplayDate(getTimeInMillis(sweDate));		
	}

	public static String getDisplayDate(long dateNum) {
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		String date1 = format1.format(dateNum);   			
		return date1;
	}

	public static long getTimeInMillisDateAndTime(SweDate sweDate) {
		return getTimeInMillis(sweDate);
	}
	
	// Known bug: This seems to reduce the minutes 6/30/2022
	//yyyy-MM-dd:HH:MM
	public static long getTimeInMillisDateAndTime(String strDate) {
		String strYear = strDate.substring(0,4);
		String strMonth = strDate.substring(5,7);
		String strDay = strDate.substring(8,10);
		String strHr = strDate.substring(11,13);
		String strMin = strDate.substring(14,16);

		long millisToAdd = Long.parseLong(strHr) * 60 * 60 * 1000 + Long.parseLong(strMin) * 60 * 1000; 
		Calendar cal = new GregorianCalendar(Integer.parseInt(strYear), Integer.parseInt(strMonth)-1, Integer.parseInt(strDay)); 
		return millisToAdd + cal.getTimeInMillis();
	}

	public static SweDate getSweDate(String strDate) {
		long millis = getTimeInMillis(strDate);
		return getSweDate(millis);
	}
	
	
	//yyyy-MM-dd
	public static long getTimeInMillisOnlyForDate(String strDate) {
		String strYear = strDate.substring(0,4);
		String strMonth = strDate.substring(5,7);
		String strDay = strDate.substring(8,10);

		Calendar cal = new GregorianCalendar(Integer.parseInt(strYear), Integer.parseInt(strMonth)-1, Integer.parseInt(strDay));
		return cal.getTimeInMillis();
	}

	//yyyy-MM-dd
	public static long getTimeInMillis(String strDate) {
		String strYear = strDate.substring(0,4);
		String strMonth = strDate.substring(5,7);
		String strDay = strDate.substring(8,10);

		Calendar cal = new GregorianCalendar(Integer.parseInt(strYear), Integer.parseInt(strMonth)-1, Integer.parseInt(strDay));
		return cal.getTimeInMillis();
	}

	public static String getDblStrWithDecimal(double num) {
		NumberFormat formatter = new DecimalFormat("#0.00");
		String numStr = formatter.format(num);
		return numStr;
	}

	public static String getDblStr(double num) {
		NumberFormat formatter = new DecimalFormat("#0");
		String numStr = formatter.format(num);
		return numStr;
	}

	public static boolean areConjunct (double degree1, double degree2, double orb) {
		return (Math.abs(degree1-degree2) < orb);
	}

	public static boolean areTrine (double degree1, double degree2, double orb) {
		double angularDistance = Math.abs(degree1-degree2);
		return (Math.abs(angularDistance-120.0) < orb);
	}

	public static boolean areOpposed(double degree1, double degree2, double orb) {
		double angularDistance = Math.abs(degree1-degree2);
		return (Math.abs(angularDistance-180.0) < orb);
	}

	public static boolean areSquared(double degree1, double degree2, double orb) {
		double angularDistance = Math.abs(degree1-degree2);
		return (Math.abs(angularDistance-90.0) < orb);
	}

	public static int getIngress(boolean nakshatra,double degree1) {
		if (degree1 < 0) degree1 = 360.0 + degree1;
		if (nakshatra) return (int) (degree1/13.333);
		return (int) (degree1/30.0);
	}
	
	public static String getPlanetName(int planetNum) {
		return planetNames[planetNum];
	}

	public static String get1BasedPlanetName(int planetNum) {
		return getPlanetName(planetNum-1);
	}

	public static String getRashiName(int num) {
		return rashiNames[num];
	}

	public static String getSignName(int num) {
		return signNames[num];
	}

	public static String get1BasedSignName(int num) {
		return getSignName(num-1);
	}

	public static String getNakshatraName(int num) {
		return nakshatraNames[num];
	}

	public static String get1BasedNakshatraName(int num) {
		return getNakshatraName(num-1);
	}

	public static int getZeroBasedNavamansh(double degrees) {
		int navamanshRashi = 1;
		navamanshRashi = ((int)(degrees/3.333))%12;
		return navamanshRashi;
	}

	public static double getBukta(double degrees) {
		int navamanshRashi = 1;
		navamanshRashi = (int)(degrees/3.333);
		return degrees-(navamanshRashi*3.333);
	}

    public static Map<String, SuMoAspectRecord> loadFileIntoMap(String filePath) {
        Map<String, SuMoAspectRecord> recordMap = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\t");
                if (parts.length >= 4) {
                	SuMoAspectRecord record = new SuMoAspectRecord(parts[0], parts[1], Integer.parseInt(parts[2]), parts[3]);
                    recordMap.put(parts[1], record);
                } else if (parts.length == 3) {
                	SuMoAspectRecord record = new SuMoAspectRecord(parts[0], parts[1], Integer.parseInt(parts[2]), "");
                    recordMap.put(parts[1], record);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return recordMap;
    }

	
	public static void main(String... args) {
		String strDate = "2022-07-01:01:12";
		System.out.println(strDate);
		System.out.println(getDisplayDate(getTimeInMillisDateAndTime(strDate)));
		System.out.println(getDisplayDateTime(getTimeInMillisDateAndTime(strDate)));
		
		String startDateString = "1980-01-01";
		String endDateString = "2051-12-30";
		
		SweDate sdStart = getSweDate(getTimeInMillis(startDateString));
		SweDate sdEnd = getSweDate(getTimeInMillis(endDateString));
		
		System.out.println(getDisplayDateTime(getTimeInMillis(endDateString)));
		
		System.out.println("sdStart.getJulDay() = " + getDisplayDateTime(sdStart));
		System.out.println("sdEnd.getJulDay() = " + getDisplayDateTime(sdEnd));
		
	}
}
