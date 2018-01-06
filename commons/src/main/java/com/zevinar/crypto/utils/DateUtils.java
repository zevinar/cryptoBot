package com.zevinar.crypto.utils;

import java.util.Calendar;

public final class DateUtils {
	public static final int MIN_IN_MS = 60 * 1000;
	public static final int HOUR_IN_MS = 60 * MIN_IN_MS;
	public static final int DAY_IN_MS = 24 * HOUR_IN_MS;
	private DateUtils() {
		throw new UnsupportedOperationException("Should not instantiate utility class!");
	}
	public static long roundToClosetHour(long timeMS){
		long newTime;
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(timeMS);
		newTime = timeMS - calendar.get(Calendar.MINUTE) * 60 * 1000;
		return newTime;
	}
	
	public static String buildDateHourlyKey(long fromTime) {
		long roundedTime = roundToClosetHour(fromTime);
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(roundedTime);
		String template = "%s-%s-%s:%s:00";
		return String.format(template, calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR), calendar.get(Calendar.HOUR_OF_DAY));
	}
	
}
