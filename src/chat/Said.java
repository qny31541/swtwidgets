package chat;

import static java.util.concurrent.TimeUnit.DAYS;
import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.HashSet;
import java.util.Set;

public class Said {

	final Voice voice;
	final long when;
	final Object what;

	Said(Voice voice, Object what) {
		this.voice = voice;
		when = System.currentTimeMillis();
		this.what = what;
	}

	public String who() {
		return voice.who;
	}

	public String when() {
		final long duration = System.currentTimeMillis() - when;
		return TimeUtils.formatMillis(duration, DAYS, SECONDS).trim();
	}

	public static class Voice {

		static Set<String> whos = new HashSet<String>();

		String who;

		public Voice(String who) {
			if (whos.contains(who)) throw new IllegalArgumentException("cannot create new Voice for existing who \""+ who + "\"");
			this.who = who;
		}

		public Said say(Object what) {
			return new Said(this, what);
		}
	}

}
