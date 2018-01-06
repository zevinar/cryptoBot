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
	
}
