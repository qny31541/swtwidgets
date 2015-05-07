package chat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.*;

/**
 * http://stackoverflow.com/a/5062810/97790
 */
public class TimeUtils {

	public static String dateTime() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
	}

	public static String timeDate() {
		return new SimpleDateFormat("HH:mm:ss dd-MM-yyyy").format(Calendar.getInstance().getTime());
	}

	/**
	 * Converts time to a human readable format within the specified range
	 *
	 * @param duration the time in milliseconds to be converted
	 * @param max	  the highest time unit of interest
	 * @param min	  the lowest time unit of interest
	 */
	public static String formatMillis(long duration, TimeUnit max, TimeUnit min) {
		StringBuilder res = new StringBuilder();
		
		TimeUnit current = max;
		
		while (duration > 0) {
			long temp = current.convert(duration, MILLISECONDS);
			
			if (temp > 0) {
				duration -= current.toMillis(temp);
				res.append(temp).append(" ").append(current.name().toLowerCase());
				if (temp < 2) res.deleteCharAt(res.length() - 1);
				res.append(", ");
			}
			
			if (current == min) break;
			
			current = TimeUnit.values()[current.ordinal() - 1];
		}
		
		// clean up our formatting....
		
		// we never got a hit, the time is lower than we care about
		if (res.lastIndexOf(", ") < 0) return "0 " + min.name().toLowerCase();
		
		// yank trailing  ", "
		res.deleteCharAt(res.length() - 2);
		
		//  convert last ", " to " and"
		int i = res.lastIndexOf(", ");
		if (i > 0) {
			res.deleteCharAt(i);
			res.insert(i, " and");
		}
		
		return res.toString();
	}

	public static void main(String[] args) throws InterruptedException {
		long[] durations = new long[]{
				123,
				SECONDS.toMillis(5) + 123,
				DAYS.toMillis(1) + HOURS.toMillis(1),
				DAYS.toMillis(1) + SECONDS.toMillis(2),
				DAYS.toMillis(1) + HOURS.toMillis(1) + MINUTES.toMillis(2),
				DAYS.toMillis(4) + HOURS.toMillis(3) + MINUTES.toMillis(2) + SECONDS.toMillis(1),
				DAYS.toMillis(5) + HOURS.toMillis(4) + MINUTES.toMillis(1) + SECONDS.toMillis(23) + 123,
				DAYS.toMillis(42)
			};
		
			for (long duration : durations) {
				System.out.println(TimeUtils.formatMillis(duration, DAYS, SECONDS));
			}
			
			System.out.println("\nAgain in only hours and minutes\n");
			for (long duration : durations) {
				System.out.println(TimeUtils.formatMillis(duration, HOURS, MINUTES));
			}
			
			System.out.println("\n1 second ago");
			long start = System.currentTimeMillis();
			Thread.sleep(1000);
			long stop = System.currentTimeMillis();
			long duration = stop - start;
			System.out.println(TimeUtils.formatMillis(duration, HOURS, MINUTES));
	}
}