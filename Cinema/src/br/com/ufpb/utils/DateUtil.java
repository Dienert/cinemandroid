/**
 * Utility to dates.
 * 
 */
package br.com.ufpb.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.commons.lang.time.DateUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.joda.time.Minutes;
import org.joda.time.Seconds;

public final class DateUtil {

	public static final String FORMAT_DATE_TIME = "dd/MM/yyyy - HH:mm";

	public static final String FORMAT_DATE_SQL = "yyyy-MM-dd";

	public static final String FORMAT_TIME = "HH:mm:ss";
	
	public static final String FORMAT_DATE_BR = "dd/MM/yyyy";
	
	public static final String FORMAT_DATE_TIME_BR = "dd/MM/yyyy HH:mm"; 
	
	public static final String FORMAT_DATE_TIME_COMPLETE = "dd/MM/yyyy HH:mm:ss"; 
	
	/**
	 * Constructor
	 */
	public DateUtil() {
		// empty constructor.
	}

	/**
	 * Returns the number of FULL-days between dates.
	 * 
	 * @param begin
	 * @param end
	 * @return int
	 */
	public static int getDaysBetween(Date begin, Date end) {
		DateTime date1 = new DateTime(begin);
		DateTime date2 = new DateTime(end);
		
		return Days.daysBetween(date1, date2).getDays();
	}

	/**
	 * Returns the number of FULL-hours between dates.
	 * 
	 * @param begin
	 * @param end
	 * @return int
	 */
	public static int getHoursBetween(Date begin, Date end) {
		DateTime date1 = new DateTime(begin);
		DateTime date2 = new DateTime(end);
		
		return Hours.hoursBetween(date1, date2).getHours();
	}

	/**
	 * Returns the number of FULL-minutes between dates.
	 * 
	 * @param begin
	 * @param end
	 * @return int
	 */
	public static int getMinutesBetween(Date begin, Date end) {
		DateTime date1 = new DateTime(begin);
		DateTime date2 = new DateTime(end);
		
		return Minutes.minutesBetween(date1, date2).getMinutes();
	}

	/**
	 * Returns the number of FULL-seconds between dates.
	 * 
	 * @param begin
	 * @param end
	 * return int
	 */
	public static int getSecondsBetween(Date begin, Date end) {
		DateTime date1 = new DateTime(begin);
		DateTime date2 = new DateTime(end);

		return Seconds.secondsBetween(date1, date2).getSeconds();
	}

	/**
	 * Gets the date formatter with the default formatting style for the default
	 * locale.
	 * 
	 * @param date
	 * @return date
	 */
	public static Date convertDate(String date) {
		DateFormat format = DateFormat.getDateInstance();
		format.setLenient(false);

		try {
			return new Date(format.parse(date).getTime());
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Convert dateTime to format "yyyy-MM-dd"
	 * @param dtConsulta
	 * @return String no formato "yyyy-MM-dd"
	 */
	public static String convertDate(Date dtConsulta) {
		return convertDateTime(dtConsulta, FORMAT_DATE_SQL);
	}
	
	/**
	 * Convert dateTime to Br format dd/MM/yyyy
	 * @param dtConsulta
	 * @return String no formato "dd/MM/yyyy"
	 */
	public static String convertDateBR(Date dtConsulta) {
		return convertDateTime(dtConsulta, FORMAT_DATE_BR);
	}

	/**
	 * Convert dateTime to hours format HH:mm:ss
	 * @param dtConsulta
	 * @return String no formato "HH:mm:ss"
	 */
	public static String convertTime(Date dtConsulta) {
		return convertDateTime(dtConsulta, FORMAT_TIME);
	}

	/**
	 * Output dateTime on format dd/MM/yyyy - HH:mm:ss
	 * @param dtConsulta
	 * @return String no formato "dd/MM/yyyy - HH:mm:ss"
	 */
	public static String outputDate(Date dtConsulta) {
		return convertDateTime(dtConsulta, FORMAT_DATE_TIME);
	}

	private static String convertDateTime(Date dt, String format) {
		try {
			SimpleDateFormat formatter = new SimpleDateFormat(format,
					new Locale("pt", "BR"));
			return formatter.format(dt);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Return a date with seconds added by parameters
	 * 
	 * @param date
	 * @param seconds
	 * @return Date
	 */
	public static Date addSeconds(Date date, int seconds) {

		DateTime d = new DateTime(date);
		d = d.plusSeconds(seconds);

		return d.toDate();
	}

	/**
	 * Return a date with minutes added by parameters
	 * 
	 * @param date
	 * @param minutes
	 * @return
	 */
	public static Date addMinutes(Date date, int minutes) {

		DateTime d = new DateTime(date);
		d = d.plusMinutes(minutes);

		return d.toDate();
	}

	/**
	 * Return a date with hours added by parameters
	 * 
	 * @param date
	 * @param hours
	 * @return
	 */
	public static Date addHours(Date date, int hours) {

		DateTime d = new DateTime(date);
		d = d.plusHours(hours);

		return d.toDate();
	}

	/**
	 * Return a date with days added by parameters.
	 * 
	 * @param date
	 * @param days
	 * @return
	 */
	public static Date addDays(Date date, int days) {

		DateTime d = new DateTime(date);
		d = d.plusDays(days);

		return d.toDate();
	}

	/**
	 * Return a date with days subtracted by parameters
	 * 
	 * @param date
	 * @param days
	 * @return Date
	 */
	public static Date minusDays(Date date, int days) {

		DateTime d = new DateTime(date);
		d = d.minusDays(days);

		return d.toDate();
	}

	/**
	 * Return a date with hours subtracted by parameters
	 * 
	 * @param date
	 * @param hours
	 * @return
	 */
	public static Date minusHours(Date date, int hours) {

		DateTime d = new DateTime(date);
		d = d.minusHours(hours);

		return d.toDate();
	}

	/**
	 * Return a date with minutes subtracted by parameters
	 * 
	 * @param date
	 * @param minutes
	 * @return
	 */
	public static Date minusMinutes(Date date, int minutes) {

		DateTime d = new DateTime(date);
		d = d.minusMinutes(minutes);

		return d.toDate();
	}

	/**
	 * Return a date with seconds subtracted by parameters
	 * 
	 * @param date
	 * @param seconds
	 * @return
	 */
	public static Date minusSeconds(Date date, int seconds) {

		DateTime d = new DateTime(date);
		d = d.minusSeconds(seconds);

		return d.toDate();
	}

	/**
	 * Checks if the date date2 is earlier than date1
	 * 
	 * @param date1
	 * @param date2
	 * @return true if the second date2 is before than date1. False otherwise.
	 */
	public static boolean isSecondBeforeFirst(Date date1, Date date2) {

		boolean isBefore = false;

		if (date2.before(date1)) {
			isBefore = true;
		}

		return isBefore;
	}

	/**
	 * Checks if parameter 'date' is earlier than Now
	 * 
	 * @param date
	 * @return TRUE if the date is before the actual date (now). FALSE
	 *         otherwise.
	 */
	public static boolean isBeforeNow(Date date) {
		
		boolean isBefore = false;
		
		GregorianCalendar now = new GregorianCalendar();
		now.setTimeZone(TimeZone.getDefault());
		
		if (date.before(now.getTime())) {
			isBefore = true;
		}
		
		return isBefore;
		
	}

	/**
	 * Checks whether there is any clash of schedules
	 * 
	 * @param timeBegin1
	 * @param timeEnd1
	 * @param timeBegin2
	 * @param timeEnd2
	 * @return Boolean		FALSE exists clash
	 */
	public static boolean isAvailable(Date timeBegin1, Date timeEnd1,
			Date timeBegin2, Date timeEnd2) {

		int compareTimeBegin = timeBegin1.compareTo(timeBegin2);
		int compareTimeEnd = timeEnd1.compareTo(timeEnd2);
		int compareTimeBeginEnd = timeBegin1.compareTo(timeEnd2);
		int compareTimeEndBegin = timeEnd1.compareTo(timeBegin2);

		// checks if the periods begin at the same time
		boolean equalBegin = compareTimeBegin == 0;

		// checks if the periods end at the same time
		boolean equalEnd = compareTimeEnd == 0;

		// checks if the next period begin at the the end of first period
//		boolean equalBeginEnd = compareTimeBeginEnd == 0;

		// checks if the first period ends at the same time of the next period
//		boolean equalEndBegin = compareTimeEndBegin == 0;

		// checks if the period begins before the other and ends during it
		boolean duringBefore = compareTimeBegin <= 0 && compareTimeEnd < 0
				&& compareTimeEndBegin > 0;

		// checks if the period begins before and ends after the other
		boolean duringOut = compareTimeBegin < 0 && compareTimeEnd > 0;

		// checks if the period begins during the other and ends after it
		boolean duringAfter = compareTimeBegin > 0 && compareTimeBeginEnd < 0
				&& compareTimeEnd >= 0;

		// checks if the period begins and ends in the other
		boolean duringIn = compareTimeBegin > 0 && compareTimeBeginEnd < 0
				&& compareTimeEndBegin > 0 && compareTimeEnd < 0;

		// checks if the periods are equals
		boolean equals = compareTimeBegin == 0 && compareTimeEnd == 0;

		return !(equalBegin || equalEnd /*|| equalBeginEnd || equalEndBegin*/
				|| duringBefore || duringOut || duringAfter || duringIn || equals);
	}

	/**
	 * Returns a date with the date of 01/01/1970 at 00:00:00.
	 * 
	 * @return a date
	 */
	public static Date getInitialDate() {

		Calendar cal = Calendar.getInstance();
		cal.setTimeZone(TimeZone.getDefault());
		cal.set(Calendar.YEAR, 1970);
		cal.set(Calendar.MONTH, Calendar.JANUARY);
		cal.set(Calendar.DAY_OF_MONTH, 01);
		cal.set(Calendar.MILLISECOND, 0);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);

		return cal.getTime();
	}
	/**
	 * Returns the Unix-date with the time contained in parameter 'date'.
	 * 
	 * @param date
	 * @return date
	 */
	public static Date getInitialDate(Date date) {

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.YEAR, 1970);
		cal.set(Calendar.MONTH, Calendar.JANUARY);
		cal.set(Calendar.DAY_OF_MONTH, 01);
		cal.set(Calendar.MILLISECOND, 0);

		return cal.getTime();
	}
	
	/**
	 * Parsing the String date and validate your use
	 * 
	 * @param date
	 * @return boolean		true==valid |false==no valid
	 */
	public static boolean isValidDate(String date) {
		if (date != null && !date.isEmpty() ) {
			SimpleDateFormat formatter = new SimpleDateFormat(FORMAT_DATE_BR);
			try {
				formatter.parse(date);
				return true;
			} catch (Exception e) {
			}
		}
		return false;
	}
	
	/**
	 * Parsing the String dateTime and validate your use
	 * 
	 * @param dateTime
	 * @return boolean		true==valid |false==no valid
	 */
	public static boolean isValidDateTime(String dateTime) {
		if (dateTime != null && !dateTime.isEmpty() ) {
			SimpleDateFormat formatter = new SimpleDateFormat(FORMAT_DATE_TIME_BR);
			try {
				formatter.parse(dateTime);
				return true;
			} catch (Exception e) {
			}
		}
		return false;
	}
	
	/**
	 * Checks if the range between dates represents the end of a day
	 * 
	 * @param begin
	 * @param end
	 * @return boolean
	 */
	public static boolean endsNextDay(Date begin, Date end) {
		DateTime in = new DateTime(begin);
		DateTime out = new DateTime(end);
		return (in.getDayOfYear() < out.getDayOfYear() && out.getMinuteOfDay() > 0);
	}
	
	/**
	 * Set a date field
	 * 
	 * @param date
	 * @param field		[MONTH,YEAR,DAY,...]
	 * @param value
	 * @return date
	 */
	public static Date setField(Date date, int field, int value) {
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.set(field, value);
		return calendar.getTime();
	}
	
	/**
	 * Checks if actual DateTime are between dates contained in parameters
	 * 
	 * @param begin
	 * @param end
	 * @return boolean		true==it's | false==no
	 */
	public static boolean isTodayBetween(Date begin, Date end) {

		GregorianCalendar now = new GregorianCalendar();
		now.setTimeZone(TimeZone.getDefault());
		
		Date today = now.getTime();
		
		boolean isToday = false;
		
		if(begin != null && end != null){
			
			if(today.equals(begin) || today.equals(end) || 
					(today.after(begin) && today.before(end))){
				isToday = true;
			}
			
		} else if (begin != null && end == null){
			
			if(today.equals(begin) || today.after(begin)){
				isToday = true;
			}
			
		}

		return isToday;
	}

	/**
	 * Returns date of tomorrow
	 * 
	 * @return Date
	 */
	public static Date getTomorrow(){
		
		GregorianCalendar now = new GregorianCalendar();
		now.setTimeZone(TimeZone.getDefault());
		
		return DateUtils.addDays(now.getTime(), 1);
	}
	
	/**
	 * Returns date of tomorrow receiving a 'date' by parameters 
	 * 
	 * @param date
	 * @return date
	 */
	public static Date getTomorrow(Date date){
		return DateUtils.addDays(date, 1);
	}
	
	/**
	 * Compare whether the date are in the range indicated by the 
	 * parameters 'begin' and 'end'
	 * 
	 * @param begin
	 * @param end
	 * @param date
	 * @return boolean		true==between |false==no
	 */
	public static boolean isThisDayBetween(Date begin, Date end, Date date) {

		boolean isToday = false;
		
		if(begin != null && end != null){
			
			if(date.equals(begin) || date.equals(end) || 
					(date.after(begin) && date.before(end))){
				isToday = true;
			}
			
		} else if (begin != null && end == null){
			
			if(date.equals(begin) || date.after(begin)){
				isToday = true;
			}
			
		}

		return isToday;
	}
	
//	public static Date convertTimeInitial(String time){
//		
//		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
//		Date date = null;
//		
//		try {
//			
//			date = sdf.parse(time);
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//			return null;
//		}
//		
//		return date;
//	}
//	
	
	/**
	 * Converts String time to DateTime of the today
	 *
	 * @param time
	 * @return date
	 */
	public static Date convertTimeToday(String time){
		
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		Date date = null;
		
		try {
			
			Calendar dateCal = Calendar.getInstance();
			dateCal.setTime(sdf.parse(time));
			
			Calendar today = Calendar.getInstance();
			dateCal.set(Calendar.DAY_OF_MONTH, today.get(Calendar.DAY_OF_MONTH));
			dateCal.set(Calendar.MONTH, today.get(Calendar.MONTH));
			dateCal.set(Calendar.YEAR, today.get(Calendar.YEAR));
			
			date = dateCal.getTime();
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		return date;
	}
	
	/**
	 * Converts string dateTime to date
	 * 
	 * @param dateTime
	 * @return Date
	 */
	public static Date convertDateTime(String dateTime){
		
		SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_DATE_TIME_COMPLETE);
		Date date = null;
		
		try {
			
			date = sdf.parse(dateTime);
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		return date;
	}
	
	/**
	 * Converts string dateS to date
	 * 
	 * @param dateTime
	 * @return Date
	 */
	public static Date convertDateBR(String dateS){
		
		SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_DATE_BR);
		Date date = null;
		
		try {
			date = sdf.parse(dateS);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		return date;
	}
	
	/**
	 * Compare dates
	 * 
	 * @param date1
	 * @param date2
	 * @return boolean	true==same date |false==different date
	 */
	public static boolean isSameTime(Date date1, Date date2){
		
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date1);
		
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(date2);
		
		if(cal1.get(Calendar.HOUR_OF_DAY) == cal2.get(Calendar.HOUR_OF_DAY) 
				&& cal1.get(Calendar.MINUTE) == cal2.get(Calendar.MINUTE)
				&& cal1.get(Calendar.SECOND) == cal2.get(Calendar.SECOND)){
			return true;
		}
		
		return false;
	}
	
	/**
	 * Verifica se as datas estão no mesmo dia. Não verifica a hora, apenas dia, mês e ano.
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean isSameDay(Date date1, Date date2){
		return DateUtils.isSameDay(date1, date2);
	}
	
	/**
	 * Converts Long to Date
	 * @param dateLong
	 * @return date
	 */
	public static Date convertLongToDate(Long dateLong){
		
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(dateLong);
		
		return cal.getTime();
	}

	public static Date changeDay(Date dateChange, Date date){
		
		DateTime calChange = new DateTime(date);
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(dateChange);
		cal.set(Calendar.DAY_OF_MONTH, calChange.getDayOfMonth());
		cal.set(Calendar.MONTH, calChange.getMonthOfYear()-1);
		cal.set(Calendar.YEAR, calChange.getYear());
		
		return cal.getTime();
		
	}
}
