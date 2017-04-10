package com.kitever.utils;

import android.widget.DatePicker;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateHelper {

	private static final SimpleDateFormat FULL_DATE_FORMATTER = new SimpleDateFormat(
			"MM/dd/yyyy 'at' HH:mm");

	private static SimpleDateFormat format;
	private static final SimpleDateFormat SHORT_DATE_FORMATTER = new SimpleDateFormat(
			"MM/dd/yyyy");

	private static final SimpleDateFormat MY_DATE_FORMATTER = new SimpleDateFormat(
			"dd MMM yyyy hh:mm aa");

	private static final SimpleDateFormat MY_DATE_FORMATTER2 = new SimpleDateFormat(
			"yyyy-MM-dd hh:mm a");

	private static final SimpleDateFormat MY_DATE_FORMATTER3 = new SimpleDateFormat(
			"dd/MM/yyyy hh:mm a");

	// "dd/MM/yyyy hh:mm a"

	public static String getLastMonth() {
		String pattern = "MMM, yyyy";
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, 1);
		long month = cal.getTimeInMillis();
		format = new SimpleDateFormat(pattern);
		return format.format(new Date(month - 86400000L));
	}

	public static String getFirstDayOfMonth() {
		String pattern = "d-MMM, yyyy";
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, 1);
		long monthInmill = cal.getTimeInMillis();
		format = new SimpleDateFormat(pattern);
		return format.format(new Date(monthInmill));
	}

	public static String getFirstDayOfWeek() {
		String pattern = "d-MMM, yyyy";
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
		long weekMonth = cal.getTimeInMillis();
		format = new SimpleDateFormat(pattern);
		return format.format(new Date(weekMonth));
	}

	public static String getYestardayDate(Long dateInMillis) {
		String pattern = "d-MMM, yyyy";
		format = new SimpleDateFormat(pattern);
		return format.format(new Date(dateInMillis - 86400000L));
	}

	public static String getTodayDate() {
		String pattern = "d-MMM, yyyy";
		format = new SimpleDateFormat(pattern);
		return format.format(new Date());

	}

	public static String format(long date) {
		return FULL_DATE_FORMATTER.format(date);
	}

	public static String formatShortly(long date) {
		return SHORT_DATE_FORMATTER.format(date);
	}

	public static String myFormat(long date) {
		return MY_DATE_FORMATTER.format(date);
	}

	public static String myFormat2(long date) {
		return MY_DATE_FORMATTER2.format(date);
	}

	public static String myFormat3(long date) {
		return MY_DATE_FORMATTER3.format(date);
	}

	public static int getDayOfMonth(long date) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(date);
		return c.get(Calendar.DAY_OF_MONTH);
	}

	public static int getMonthOfYear(long date) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(date);
		return c.get(Calendar.MONTH);
	}

	public static int getYear(long date) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(date);
		return c.get(Calendar.YEAR);
	}

	public static int getHourOfDay(long date) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(date);
		return c.get(Calendar.HOUR_OF_DAY);
	}

	public static int getMinute(long date) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(date);
		return c.get(Calendar.MINUTE);
	}

	public static long getDate(int year, int monthOfYear, int dayOfMonth) {
		Calendar c = Calendar.getInstance();
		c.set(year, monthOfYear, dayOfMonth);
		return c.getTimeInMillis();
	}

	public static long getDateInmillis() {
		Calendar c = Calendar.getInstance();
		final int year = c.get(Calendar.YEAR);
		final int month = c.get(Calendar.MONTH);
		final int day = c.get(Calendar.DAY_OF_MONTH);
		c.set(year, month, day);
		return c.getTimeInMillis();
	}

	/**
	 * @param datePicker
	 * @return date with the hours/minutes/seconds/milliseconds stripped off
	 */
	public static long getDate(DatePicker datePicker) {
		return getDate(datePicker.getYear(), datePicker.getMonth(),
				datePicker.getDayOfMonth());
	}

	public static void initDatePicker(DatePicker datePicker, long date) {
		datePicker.init(getYear(date), getMonthOfYear(date),
				getDayOfMonth(date), null);
	}

	public static void initTimePicker(TimePicker timePicker, long date) {

		int currentApiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentApiVersion > android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
			timePicker.setMinute(getMinute(date));
			timePicker.setHour(getHourOfDay(date));
		} else {
			timePicker.setCurrentMinute(getMinute(date));
			timePicker.setCurrentHour(getHourOfDay(date));
		}

	}

	/**
	 * @param timePicker
	 * @return time as milliseconds since 00:00:00:000 (midnight)
	 */
	@SuppressWarnings("deprecation")
	public static long getTime(TimePicker timePicker) {

		int currentApiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentApiVersion > android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
			int h = timePicker.getHour();
			int m = timePicker.getMinute();
			return (h * 3600 + m * 60) * 1000;
		} else {
			int h = timePicker.getCurrentHour().intValue();
			int m = timePicker.getCurrentMinute().intValue();
			return (h * 3600 + m * 60) * 1000;
		}

	}

	public static long getStartOfTheDay(long date) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(date);

		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);

		return c.getTimeInMillis();
	}
}
