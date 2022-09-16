package de.cuuky.backup.utils;

import org.apache.commons.lang.time.DateUtils;

import java.util.Date;

public class TimeUtils {

	public static final long TWENTY_FOUR_HOURS = 24 * 60 * 60 * 1000;

	@SuppressWarnings("deprecation")
	public static long getNextReset(int hour) {
		Date reset = new Date();
		reset.setMinutes(0);
		reset.setSeconds(0);
		Date current = new Date();
		reset.setHours(hour);
		if (reset.before(current))
			reset = DateUtils.addDays(reset, 1);
		return (reset.getTime() - current.getTime()) / 1000;
	}
}
