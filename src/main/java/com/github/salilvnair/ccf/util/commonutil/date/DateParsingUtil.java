package com.github.salilvnair.ccf.util.commonutil.date;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class DateParsingUtil {

	public enum TimeFormat {
		HH_A("hha"),
		HH_MM_A("hh:mm a")
		;

		private final String timeFormat;

		TimeFormat(String timeFormat) {this.timeFormat = timeFormat;}

		public String value() {
			return timeFormat;
		}

		public static String[] getAllTimeFormats() {
			TimeFormat[] timeFormats = values();
			String[] formats = new String[timeFormats.length];

			for (int i = 0; i < timeFormats.length; i++) {
				formats[i] = timeFormats[i].value();
			}

			return formats;
		}
	}

	public enum TimeZone {
		ACT("ACT", "Australia/Darwin"),
		AET("AET", "Australia/Sydney"),
		AGT("AGT", "America/Argentina/Buenos_Aires"),
		ART("ART", "Africa/Cairo"),
		AST("AST", "America/Anchorage"),
		BET("BET", "America/Sao_Paulo"),
		BST("BST", "Asia/Dhaka"),
		CAT("CAT", "Africa/Harare"),
		CNT("CNT", "America/St_Johns"),
		CST("CST", "America/Chicago"),
		CTT("CTT", "Asia/Shanghai"),
		EAT("EAT", "Africa/Addis_Ababa"),
		EST("EST", "America/New_York"),
		ECT("ECT", "Europe/Paris"),
		IET("IET", "America/Indiana/Indianapolis"),
		IST("IST", "Asia/Kolkata"),
		JST("JST", "Asia/Tokyo"),
		MIT("MIT", "Pacific/Apia"),
		NET("NET", "Asia/Yerevan"),
		NST("NST", "Pacific/Auckland"),
		PLT("PLT", "Asia/Karachi"),
		PNT("PNT", "America/Phoenix"),
		PRT("PRT", "America/Puerto_Rico"),
		PST("PST", "America/Los_Angeles"),
		SST("SST", "Pacific/Guadalcanal"),
		VST("VST", "Asia/Ho_Chi_Minh");

		private final String abbreviation;
		private final String region;

		TimeZone(String abbreviation, String region) {
			this.abbreviation = abbreviation;
			this.region = region;
		}

		public String abbreviation() {
			return abbreviation;
		}

		public String region() {
			return region;
		}
	}
 	public enum DateFormat {
	        DASH_YY_M_D("yy-M-d"),
	        DASH_D_M_YY("d-M-yy"),
	        DASH_YY_MM_DD("yy-MM-dd"),
	        DASH_DD_MM_YY("dd-MM-yy"),
	        DASH_YY_MMM_DD("yy-MMM-dd"), 
	        DASH_DD_MMM_YY("dd-MMM-yy"),
	        DASH_YYYY_MM_DD("yyyy-MM-dd"), 
	        DASH_DD_MM_YYYY("dd-MM-yyyy"),
	        DASH_YYYY_MMM_DD("yyyy-MMM-dd"), 
	        DASH_DD_MMM_YYYY("dd-MMM-yyyy"),
	        DASH_YY_D_M("yy-d-M"),
	        DASH_M_D_YY("d-M-yy"),
	        DASH_YY_DD_MM("yy-dd-MM"),
	        DASH_MM_DD_YY("MM-dd-yy"),
	        DASH_YY_DD_MMM("yy-dd-MM"), 
	        DASH_MMM_DD_YY("MMM-dd-yy"),
	        DASH_YYYY_DD_MM("yyyy-dd-MM"), 
	        DASH_MM_DD_YYYY("MM-dd-yyyy"),
	        DASH_YYYY_DD_MMM("yyyy-dd-MMM"), 
	        DASH_MMM_DD_YYYY("MMM-dd-yyyy"),
	        
	        DOT_YY_M_D("yy.M.d"),
	        DOT_D_M_YY("d.M.yy"),
	        DOT_YY_MM_DD("yy.MM.dd"),
	        DOT_DD_MM_YY("dd.MM.yy"),
	        DOT_YY_MMM_DD("yy.MMM.dd"), 
	        DOT_DD_MMM_YY("dd.MMM.yy"),
	        DOT_YYYY_MM_DD("yyyy.MM.dd"), 
	        DOT_DD_MM_YYYY("dd.MM.yyyy"),
	        DOT_YYYY_MMM_DD("yyyy.MMM.dd"), 
	        DOT_DD_MMM_YYYY("dd.MMM.yyyy"),
	        DOT_YY_D_M("yy.d.M"),
	        DOT_M_D_YY("d.M.yy"),
	        DOT_YY_DD_MM("yy.dd.MM"),
	        DOT_MM_DD_YY("MM.dd.yy"),
	        DOT_YY_DD_MMM("yy.dd.MM"), 
	        DOT_MMM_DD_YY("MMM.dd.yy"),
	        DOT_YYYY_DD_MM("yyyy.dd.MM"), 
	        DOT_MM_DD_YYYY("MM.dd.yyyy"),
	        DOT_YYYY_DD_MMM("yyyy.dd.MMM"), 
	        DOT_MMM_DD_YYYY("MMM.dd.yyyy"),
	        
	        
	        SLASH_YY_M_D("yy/M/d"),
	        SLASH_D_M_YY("d/M/yy"),
	        SLASH_DD_M_YY("dd/M/yyyy"),
	        SLASH_YY_MM_DD("yy/MM/dd"),
	        SLASH_DD_MM_YY("dd/MM/yy"),
	        SLASH_YY_MMM_DD("yy/MMM/dd"), 
	        SLASH_DD_MMM_YY("dd/MMM/yy"),
	        SLASH_YYYY_MM_DD("yyyy/MM/dd"), 
	        SLASH_DD_MM_YYYY("dd/MM/yyyy"),
	        SLASH_YYYY_MMM_DD("yyyy/MMM/dd"), 
	        SLASH_DD_MMM_YYYY("dd/MMM/yyyy"),
	        SLASH_YY_D_M("yy/d/M"),
	        SLASH_M_D_YY("d/M/yy"),
	        SLASH_YY_DD_MM("yy/dd/MM"),
	        SLASH_MM_DD_YY("MM/dd/yy"),
	        SLASH_YY_DD_MMM("yy/dd/MM"), 
	        SLASH_MMM_DD_YY("MMM/dd/yy"),
	        SLASH_YYYY_DD_MM("yyyy/dd/MM"), 
	        SLASH_MM_DD_YYYY("MM/dd/yyyy"),
	        SLASH_YYYY_DD_MMM("yyyy/dd/MMM"), 
	        SLASH_MMM_DD_YYYY("MMM/dd/yyyy");
	        
	        private final String dateFormat;

	        DateFormat(String dateFormat) {this.dateFormat = dateFormat;}

	        public String value() {
	            return dateFormat;
	        }
	        
	        public static String[] getAllDateFormats() {
	        	DateFormat[] dateFormat = values();
	            String[] formats = new String[dateFormat.length];

	            for (int i = 0; i < dateFormat.length; i++) {
	            	formats[i] = dateFormat[i].value();
	            }

	            return formats;
	        }
	       
	    }
	 	
	public enum DateType {
		;
		public static class Value {
			public static final String TODAY = "TODAY";
		}
	 }

	public enum DateTimeFormat {
		DASH_YYYY_MM_DD_T_HH_MM_SS_Z("yyyy-MM-dd'T'HH:mm:ss'Z'"),
		DASH_YYYY_DD_MM_T_HH_MM_SS_Z("yyyy-dd-MM'T'HH:mm:ss'Z'"),
		DASH_MM_DD_YYYY_HH_MM("MM-dd-yyyy HH:mm"),
		DASH_YY_MM_DD_C_HH_MMA("yy-MMM-dd, hh:mma"),
		DASH_DD_MMM_YY_C_HH_MMA("dd-MMM-yy, hh:mma"),
		DASH_YY_MMM_DD_C_HH_MMA("yy-MMM-dd, hh:mma"),
		DASH_YYYY_MM_DD_C_HH_MMA("yyyy-MM-dd, hh:mma"),
		DASH_DD_MM_YYYY_C_HH_MMA("dd-MM-yyyy, hh:mma"),
		DASH_YYYY_MMM_DD_C_HH_MMA("yyyy-MMM-dd, hh:mma"),
		DASH_DD_MMM_YYYY_C_HH_MMA("dd-MMM-yyyy, hh:mma"),
		DASH_YY_MMM_DD_C_HH_MM_SSA("yy-MMM-dd, hh:mm:ssa"),
		DASH_DD_MMM_YY_C_HH_MM_SSA("dd-MMM-yy, hh:mm:ssa"),
		DASH_YYYY_MM_DD_C_HH_MM_SSA("yyyy-MM-dd, hh:mm:ssa"),
		DASH_YYYY_MM_DD_HH_MM_SS("yyyy-MM-dd hh:mm:ss"),
		DASH_DD_MM_YYYY_C_HHMM_SSA("dd-MM-yyyy, hh:mm:ssa"),
		DASH_YYYY_MMM_DD_C_HH_MM_SSA("yyyy-MMM-dd, hh:mm:ssa"),
		DASH_DD_MMM_YYYY_C_HH_MM_SSA("dd-MMM-yyyy, hh:mm:ssa"),

		SLASH_MM_DD_YYYY_HH_MM("MM/dd/yyyy HH:mm"),
		SLASH_MM_DD_YYYY_HH_MM_SS("MM/dd/yyyy hh:mm:ss"),
		SLASH_YY_MM_DD_C_HH_MMA("yy/MM/dd, hh:mma"),
		SLASH_DD_MM_YY_C_HH_MMA("dd/MM/yy, hh:mma"),
		SLASH_YY_MMM_DD_C_HH_MMA("yy/MMM/dd, hh:mma"),
		SLASH_DD_MMM_YY_C_HH_MMA("dd/MMM/yy, hh:mma"),
		SLASH_YYYY_MM_DD_C_HH_MMA("yyyy/MM/dd, hh:mma"),
		SLASH_DD_MM_YYYY_C_HH_MMA("dd/MM/yyyy, hh:mma"),
		SLASH_YYYY_MMM_DD_C_HH_MMA("yyyy/MMM/dd, hh:mma"),
		SLASH_DD_MMM_YYYY_C_HH_MMA("dd/MMM/yyyy, hh:mma"),
		SLASH_YY_MM_DD_HH_C_MM_SSA("yy/MM/dd, hh:mm:ssa"),
		SLASH_DD_MM_YY_HH_C_MM_SSA("dd/MM/yy, hh:mm:ssa"),
		SLASH_YY_MMM_DD_HH_C_MM_SSA("yy/MMM/dd, hh:mm:ssa"),
		SLASH_DD_MMM_YY_C_HH_MM_SSA("dd/MMM/yy, hh:mm:ssa"),
		SLASH_YYYY_MM_DD_C_HH_MM_SSA("yyyy/MM/dd, hh:mm:ssa"),
		SLASH_DD_MM_YYYY_C_HH_MM_SSA("dd/MM/yyyy, hh:mm:ssa"),
		SLASH_YYYY_MMM_DD_C_HH_MM_SSA("yyyy/MMM/dd, hh:mm:ssa"),
		SLASH_DD_MMM_YYYY_C_HH_MM_SSA("dd/MMM/yyyy, hh:mm:ssa");

		private final String dateTimeFormat;

		DateTimeFormat(String dateTimeFormat) {this.dateTimeFormat = dateTimeFormat;}

		public String value() {
			return dateTimeFormat;
		}

		public static String[] getAllDateTimeFormats() {
			DateTimeFormat[] dateTimeFormats = values();
			String[] formats = new String[dateTimeFormats.length];

			for (int i = 0; i < dateTimeFormats.length; i++) {
				formats[i] = dateTimeFormats[i].value();
			}

			return formats;
		}

	}


	public static long dateOnly(String date) {
		try {
			SimpleDateFormat sample = new SimpleDateFormat(DateFormat.SLASH_MM_DD_YYYY.value(), Locale.getDefault());
			return sample.parse(date).getTime();
		}
		catch (Exception ignore) {}
		return 0;
	}

	public static String dateOnly(long time) {
		return new SimpleDateFormat(DateFormat.SLASH_MM_DD_YYYY.value(), Locale.getDefault()).format(time);
	}

	public static String timeOnly(long time) {
		SimpleDateFormat sample = new SimpleDateFormat("hh:mm a", Locale.getDefault());
		return sample.format(time);
	}

	public static String todayWithTime() {
		SimpleDateFormat dateFormat = new SimpleDateFormat(DateTimeFormat.SLASH_MM_DD_YYYY_HH_MM_SS.value(), Locale.getDefault());
		return dateFormat.format(new Date());
	}

	public static String todayWithTime(DateTimeFormat dateTimeFormat) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(dateTimeFormat.value(), Locale.getDefault());
		return dateFormat.format(new Date());
	}

	public static String todayWithTime(String dateTimeFormat) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(dateTimeFormat, Locale.getDefault());
		return dateFormat.format(new Date());
	}

	public static String today() {
		return today(DateFormat.SLASH_MM_DD_YYYY);
	}

	public static String today(String dateFormatString) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormatString, Locale.getDefault());
		return simpleDateFormat.format(new Date());
	}

	public static String today(DateFormat dateFormat) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat.value(), Locale.getDefault());
		return simpleDateFormat.format(new Date());
	}

	public static String today(DateFormat dateFormat, TimeZone timeZone) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat.value());
		simpleDateFormat.setTimeZone(java.util.TimeZone.getTimeZone(timeZone.abbreviation()));
		return simpleDateFormat.format(new Date());
	}

	public static String yesterday() {
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new SimpleDateFormat(DateFormat.SLASH_MM_DD_YYYY.value(), Locale.getDefault()).parse(today()));
			calendar.add(Calendar.DATE, -1);
			Date tomorrow = calendar.getTime();
			return new SimpleDateFormat(DateFormat.SLASH_MM_DD_YYYY.value(), Locale.getDefault()).format(tomorrow);
		}
		catch (Exception ignore) {}
		return null;
	}

	public static String tomorrow() {
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).parse(today()));
			calendar.add(Calendar.DATE, 1);
			Date tomorrow = calendar.getTime();
			return new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(tomorrow);
		}
		catch (Exception ignore) {}
		return null;
	}


	public static long findDaysBetweenTwoDates(String old, String newDate, DateFormat dateFormat) {
		try {
			SimpleDateFormat myFormat = new SimpleDateFormat(dateFormat.value(), Locale.getDefault());
			Date date1 = myFormat.parse(old);
			Date date2 = myFormat.parse(newDate);
			long diff = date1.getTime() - date2.getTime();
			long seconds = diff / 1000;
			long minutes = seconds / 60;
			long hours = minutes / 60;
			return hours / 24;
		}
		catch (Exception ignore) {}
		return 0;
	}

	public static long findHoursBetweenTwoDates(String old, String newDate, DateFormat dateFormat) {
		try {
			SimpleDateFormat myFormat = new SimpleDateFormat(dateFormat.value(), Locale.getDefault());
			Date date1 = myFormat.parse(old);
			Date date2 = myFormat.parse(newDate);
			long diff = date1.getTime() - date2.getTime();
			long seconds = diff / 1000;
			long minutes = seconds / 60;
			return minutes / 60;
		}
		catch (Exception ignore) {}
		return 0;
	}

	public static long findMinutesBetweenTwoDates(String old, String newDate, DateFormat dateFormat) {
		try {
			SimpleDateFormat myFormat = new SimpleDateFormat(dateFormat.value(), Locale.getDefault());
			Date date1 = myFormat.parse(old);
			Date date2 = myFormat.parse(newDate);
			long diff = date1.getTime() - date2.getTime();
			long seconds = diff / 1000;
			return seconds / 60;
		}
		catch (Exception ignore) {}
		return 0;
	}

	public static long minutesBetweenTwoDates(long old, long newDate) {
		long diff = old - newDate;
		long seconds = diff / 1000;
		return seconds / 60;
	}

	public static boolean futureDate(long timestamp) {
		Date date = new Date(timestamp);
		return date.getTime() - new Date().getTime() >= 0;
	}

	public static boolean futureDate(Date date, DateFormat format) {
		return date != null && compareWithCurrentDate(date, format) > 0L;
	}

	public static boolean futureDate(String dateString, DateFormat format) {
		Date givenDate = parseDate(dateString, format);
		return givenDate != null && compareWithCurrentDate(givenDate, format) > 0L;
	}

	public static boolean todayOrFutureDate(Date date, DateFormat format) {
		return date != null && compareWithCurrentDate(date, format) >= 0L;
	}

	public static boolean pastDate(String dateString, DateFormat format) {
		Date givenDate = parseDate(dateString, format);
		return givenDate != null && compareWithCurrentDate(givenDate, format) < 0L;
	}

	public static boolean timeRangeNotExpired(String dateString, DateFormat dateFormat, String timeRangeString, String delimiter, TimeFormat timeFormat) {
		return timeRangeNotExpired(new Date(), dateString, dateFormat, timeRangeString, delimiter, timeFormat);
	}

	public static boolean timeRangeNotExpired(String dateString, DateFormat dateFormat, String timeRangeString, String delimiter, TimeFormat timeFormat, TimeZone timeZone) {
		return timeRangeNotExpired(new Date(), dateString, dateFormat, timeRangeString, delimiter, timeFormat);
	}

	public static boolean timeRangeNotExpired(Date compareWithDate, String dateString, DateFormat dateFormat, String timeRangeString, String delimiter, TimeFormat timeFormat, TimeZone timeZone) {
		String[] timeParts = timeRangeString.split(delimiter);
		SimpleDateFormat inputTimeDateFormat = new SimpleDateFormat(dateFormat.value() + " " + timeFormat.value());
		inputTimeDateFormat.setTimeZone(java.util.TimeZone.getTimeZone(timeZone.abbreviation()));
		Date currentTime;
		Date startTime;
		Date endTime;
		SimpleDateFormat currentDateFormat = new SimpleDateFormat(DateFormat.SLASH_MM_DD_YYYY.value() + " "+TimeFormat.HH_MM_A.value());
		currentDateFormat.setTimeZone(java.util.TimeZone.getTimeZone(timeZone.abbreviation()));
		String currentTimeString = currentDateFormat.format(compareWithDate);
		try {
			currentTime = currentDateFormat.parse(currentTimeString);
			startTime = inputTimeDateFormat.parse(dateString + " "+timeParts[0].trim());
			endTime = inputTimeDateFormat.parse(dateString + " "+timeParts[1].trim());
		}
		catch (Exception e) {
			throw new IllegalArgumentException("Invalid time format in the range string", e);
		}

		return currentTime.after(startTime) && currentTime.before(endTime) || currentTime.before(startTime);
	}

	public static boolean timeRangeNotExpired(Date compareWithDate, String dateString, DateFormat dateFormat, String timeRangeString, String delimiter, TimeFormat timeFormat) {
		String[] timeParts = timeRangeString.split(delimiter);
		SimpleDateFormat inputTimeDateFormat = new SimpleDateFormat(dateFormat.value() + " " + timeFormat.value());

		Date currentTime;
		Date startTime;
		Date endTime;
        SimpleDateFormat currentDateFormat = new SimpleDateFormat(DateFormat.SLASH_MM_DD_YYYY.value() + " "+TimeFormat.HH_MM_A.value());
		String currentTimeString = currentDateFormat.format(compareWithDate);
		try {
			currentTime = currentDateFormat.parse(currentTimeString);
			startTime = inputTimeDateFormat.parse(dateString + " "+timeParts[0].trim());
			endTime = inputTimeDateFormat.parse(dateString + " "+timeParts[1].trim());
		}
		catch (Exception e) {
			throw new IllegalArgumentException("Invalid time format in the range string", e);
		}

		return currentTime.after(startTime) && currentTime.before(endTime) || currentTime.before(startTime);
	}

	public static boolean timeRangeExpired(Date compareWithDate, String dateString, DateFormat dateFormat, String timeRangeString, String delimiter, TimeFormat timeFormat, TimeZone timeZone) {
		return !timeRangeNotExpired(compareWithDate, dateString, timeRangeString, delimiter, timeFormat, timeZone);
	}

	public static boolean timeRangeExpired(Date compareWithDate, String dateString, DateFormat dateFormat, String timeRangeString, String delimiter, TimeFormat timeFormat) {
		return !timeRangeNotExpired(compareWithDate, dateString, timeRangeString, delimiter, timeFormat);
	}

	public static boolean timeRangeNotExpired(Date compareWithDate, String dateString, String timeRangeString, String delimiter, TimeFormat timeFormat) {
		return timeRangeNotExpired(compareWithDate, dateString, DateFormat.SLASH_MM_DD_YYYY, timeRangeString, delimiter, timeFormat);
	}

	public static boolean timeRangeNotExpired(Date compareWithDate, String dateString, String timeRangeString, String delimiter, TimeFormat timeFormat, TimeZone timeZone) {
		return timeRangeNotExpired(compareWithDate, dateString, DateFormat.SLASH_MM_DD_YYYY, timeRangeString, delimiter, timeFormat, timeZone);
	}

	public static boolean timeRangeNotExpired(Date compareWithDate, String dateString, String timeRangeString, TimeFormat timeFormat) {
		return timeRangeNotExpired(compareWithDate, dateString, DateFormat.SLASH_MM_DD_YYYY, timeRangeString, " - ", timeFormat);
	}

	public static boolean timeRangeExpired(Date compareWithDate, String dateString, String timeRangeString, TimeFormat timeFormat) {
		return !timeRangeNotExpired(compareWithDate, dateString, timeRangeString, " - ", timeFormat);
	}

	public static boolean timeRangeExpired(Date compareWithDate, String dateString, String timeRangeString, String delimiter, TimeFormat timeFormat) {
		return !timeRangeNotExpired(compareWithDate, dateString, timeRangeString, delimiter, timeFormat);
	}

	public static boolean timeRangeExpired(Date compareWithDate, String dateString, String timeRangeString, TimeFormat timeFormat, TimeZone timeZone) {
		return !timeRangeNotExpired(compareWithDate, dateString, timeRangeString, " - ", timeFormat, timeZone);
	}

	public static boolean timeRangeExpired(Date compareWithDate, String dateString, String timeRangeString, String delimiter, TimeFormat timeFormat, TimeZone timeZone) {
		return !timeRangeNotExpired(compareWithDate, dateString, timeRangeString, delimiter, timeFormat, timeZone);
	}

	public static long compareWithCurrentDate(Date date, DateFormat format) {
		String dateString = format(format, date);
		String currentDateString = format(format, new Date());
		Date givenDate = parseDate(dateString, format);
		Date currentDate = parseDate(currentDateString, format);
		return givenDate != null && currentDate != null ? givenDate.getTime() - currentDate.getTime() : 0L;
	}

	public static int compareDate(Date date1, Date date2) {
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal1.setTime(date1);
		cal2.setTime(date2);
		String formattedDate1 = cal1.get(Calendar.DAY_OF_MONTH)+"/"+(cal1.get(Calendar.MONTH)+1)+"/"+cal1.get(Calendar.YEAR);
		String formattedDate2 = cal2.get(Calendar.DAY_OF_MONTH)+"/"+(cal2.get(Calendar.MONTH)+1)+"/"+cal2.get(Calendar.YEAR);
		Date extractedDate1 = parseDate(formattedDate1, DateFormat.SLASH_DD_M_YY);
		Date extractedDate2 = parseDate(formattedDate2, DateFormat.SLASH_DD_M_YY);
		return extractedDate1 != null ? extractedDate1.compareTo(extractedDate2) : 0;
	}

	public static int compareDate(String date1String, String date2String, DateFormat dateFormat) {
		 return compareDate(date1String, date2String, dateFormat, dateFormat);
	}

	public static int compareDate(String date1String, String date2String, DateFormat date1Format, DateFormat date2Format) {
		Date date1 = parseDate(date1String, date1Format);
		Date date2 = parseDate(date2String, date2Format);
		return compareDate(date1, date2);
	}

	public static int compareDateTime(Date date1, Date date2) {
		return Long.compare(date1.getTime(), date2.getTime());
	}

	public static Date parseAnyDate(String dateString) {
		Date date = null;
		for (DateFormat formats : DateFormat.values()) {
			try {
				SimpleDateFormat format = new SimpleDateFormat(formats.value(), Locale.getDefault());
				date = format.parse(dateString);
			}
			catch (Exception ignore) {}
		}
		return date;
	}

	public static boolean isDate(String date) {
		for (DateFormat formats : DateFormat.values()) {
			try {
				SimpleDateFormat format = new SimpleDateFormat(formats.value(), Locale.getDefault());
				Long time = format.parse(date).getTime();
				return true;
			}
			catch (Exception ignore) {}
		}
		return false;
	}

	public static boolean isDateTime(String date) {
		for (DateTimeFormat formats : DateTimeFormat.values()) {
			try {
				SimpleDateFormat format = new SimpleDateFormat(formats.value(), Locale.getDefault());
				Long time = format.parse(date).getTime();
				return true;
			}
			catch (Exception ignore) {}
		}
		return false;
	}

	public static boolean isDate(String dateString, String dateFormat) {
		try {
			SimpleDateFormat format = new SimpleDateFormat(dateFormat,Locale.getDefault());
			Date formattedDate = format.parse(dateString);
			String parsedDate = format.format(formattedDate);
			if(dateString.equals(parsedDate)) {
				return true;
			}
		}
		catch (Exception ignore) {}
		return false;
	}

	public static String findDateFormat(String dateString, String[] dateFormat) {
		for(String format:dateFormat) {
			try {
				SimpleDateFormat sdFormat = new SimpleDateFormat(format,Locale.getDefault());
				Date formattedDate = sdFormat.parse(dateString);
				String parsedDate = sdFormat.format(formattedDate);
				if(dateString.equals(parsedDate)) {
					return format;
				}
			}
			catch (Exception ignore) {}
		}
		return null;
	}

	public static boolean isDate(String date, List<String> dateFormat) {
		boolean isFormattedDate = false;
	   for(String format:dateFormat) {
		   isFormattedDate = isDate(date, format);
	   }
	   return isFormattedDate;
	}

	public static boolean isDate(String date, String[] dateFormat) {
		boolean isFormattedDate = false;
	   for(String format:dateFormat) {
		   isFormattedDate = isDate(date, format);
		   if(isFormattedDate) {
			   return isFormattedDate;
		   }
	   }
	   return isFormattedDate;
	}

	public static Date parseDate(String dateString, String dateFormat) {
		try {
			SimpleDateFormat format = new SimpleDateFormat(dateFormat, Locale.getDefault());
			return format.parse(dateString);
		}
		catch (Exception ignore) {}
		return null;
	}

	public static Date dateOnly(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		String formattedDate = cal.get(Calendar.DAY_OF_MONTH)+"/"+(cal.get(Calendar.MONTH)+1)+"/"+cal.get(Calendar.YEAR);
		return parseDate(formattedDate, DateFormat.SLASH_DD_M_YY);
	}

	public static Date parseDate(String dateString, String[] dateFormat) {
		String format = findDateFormat(dateString, dateFormat);
		return parseDate(dateString,  format);
	}

	public static Date parseDate(String dateString, DateFormat dateFormat) {
		try {
			SimpleDateFormat format = new SimpleDateFormat(dateFormat.value(), Locale.getDefault());
			return format.parse(dateString);
		}
		catch (Exception ignore) {}
		return null;
	}

	public static Date parseDate(String dateTimeString, DateTimeFormat dateTimeFormat) {
		try {
			SimpleDateFormat format = new SimpleDateFormat(dateTimeFormat.value());
			return format.parse(dateTimeString);
		}
		catch (Exception ignore) {}
		return null;
	}

	public static Date parseDate(String dateString, DateFormat dateFormat, TimeZone timeZone) {
		try {
			SimpleDateFormat format = new SimpleDateFormat(dateFormat.value());
			format.setTimeZone(java.util.TimeZone.getTimeZone(timeZone.abbreviation()));
			return format.parse(dateString);
		}
		catch (Exception ignore) {}
		return null;
	}

	public static Date parseDate(String dateTimeString, DateTimeFormat dateTimeFormat, TimeZone timeZone) {
		try {
			SimpleDateFormat format = new SimpleDateFormat(dateTimeFormat.value());
			format.setTimeZone(java.util.TimeZone.getTimeZone(timeZone.abbreviation()));
			return format.parse(dateTimeString);
		}
		catch (Exception ignore) {}
		return null;
	}

	public static String findDate(String date, DateFormat originalFormat, DateFormat newFormat) {
		try {
			SimpleDateFormat sample = new SimpleDateFormat(originalFormat.value());
			long time = sample.parse(date).getTime();
			sample = new SimpleDateFormat(newFormat.value(), Locale.getDefault());
			return sample.format(time);
		}
		catch (Exception ignore) {}
		return date;
	}

	public static String format(DateFormat format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format.value());
		return sdf.format(new Date());
	}

	public static String format(DateFormat format, long date) {
		if (date == 0) return "";
		SimpleDateFormat sdf = new SimpleDateFormat(format.value(), Locale.getDefault());
		return sdf.format(date);
	}


	public static String format(DateFormat format, Date date) {
		if (date == null) return "";
		SimpleDateFormat sdf = new SimpleDateFormat(format.value(), Locale.getDefault());
		return sdf.format(date);
	}

	public static String format(DateFormat format, Date date, TimeZone timeZone) {
		if (date == null) return "";
		SimpleDateFormat sdf = new SimpleDateFormat(format.value());
		sdf.setTimeZone(java.util.TimeZone.getTimeZone(timeZone.abbreviation()));
		return sdf.format(date);
	}

	public static String format(String format, Date date) {
		if (date == null) return "";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.getDefault());
		return simpleDateFormat.format(date);
	}

	public static String format(DateTimeFormat format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format.value(), Locale.getDefault());
		return sdf.format(new Date());
	}

	public static String format(DateTimeFormat format, long date) {
		if (date == 0) return "";
		SimpleDateFormat sdf = new SimpleDateFormat(format.value(), Locale.getDefault());
		return sdf.format(date);
	}


	public static String format(DateTimeFormat format, Date date) {
		if (date == null) return "";
		SimpleDateFormat sdf = new SimpleDateFormat(format.value(), Locale.getDefault());
		return sdf.format(date);
	}

	public static String format(DateTimeFormat format, Date date, TimeZone timeZone) {
		if (date == null) return "";
		SimpleDateFormat sdf = new SimpleDateFormat(format.value());
		sdf.setTimeZone(java.util.TimeZone.getTimeZone(timeZone.abbreviation()));
		return sdf.format(date);
	}

	public static String format(DateTimeFormat format, String dateString, TimeZone timeZone) {
		if (dateString == null) return "";
		SimpleDateFormat sdf = new SimpleDateFormat(format.value());
		sdf.setTimeZone(java.util.TimeZone.getTimeZone(timeZone.abbreviation()));
		Date date = parseDate(dateString, format, timeZone);
		return sdf.format(date);
	}

	public static Date futureDate(int noOfDays) {
		return futureDate(new Date(), noOfDays);
	}

	public static Date futureDate(Date fromDate, int noOfDays) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(fromDate);
		// Add 2 days to the current date
		calendar.add(Calendar.DAY_OF_MONTH, noOfDays);
		return calendar.getTime();
	}

	public static Date dayOfMonth(Date fromDate, int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(fromDate);
		// Add 2 days to the current date
		calendar.set(Calendar.DAY_OF_MONTH, day);
		return calendar.getTime();
	}

	public static Date dayOfMonth(int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		// Add 2 days to the current date
		calendar.set(Calendar.DAY_OF_MONTH, day);
		return calendar.getTime();
	}

	public static Date pastDate(int noOfDays) {
		return pastDate(new Date(), noOfDays);
	}

	public static Date pastDate(Date fromDate, int noOfDays) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(fromDate);
		// Add 2 days to the current date
		calendar.add(Calendar.DAY_OF_MONTH, -(noOfDays));
		return calendar.getTime();
	}

	public static String convert(String inputDateString, DateTimeFormat inputDateTimeFormat, DateFormat outputFormat) {
		Date date = parseDate(inputDateString, inputDateTimeFormat);
		return format(outputFormat, date);
	}

	public static String convert(String inputDateString, DateTimeFormat inputDateTimeFormat, String outputFormat) {
		Date date = parseDate(inputDateString, inputDateTimeFormat);
		return format(outputFormat, date);
	}
}
