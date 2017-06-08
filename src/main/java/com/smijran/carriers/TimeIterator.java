package com.smijran.carriers;

import java.time.LocalDateTime;
import java.time.temporal.TemporalUnit;
import java.util.Iterator;

/**
 * Created on 08.06.2017.
 */
public class TimeIterator implements Iterator<LocalDateTime> {

	private LocalDateTime nextTime;
	private final TemporalUnit temporalUnit;

	public TimeIterator(LocalDateTime nextTime, TemporalUnit timeUnit) {
		this.nextTime = nextTime;
		if (!nextTime.equals(nextTime.truncatedTo(timeUnit))) {
			throw new IllegalArgumentException("" + nextTime + " is incorrect for " + timeUnit);
		}
		this.temporalUnit = timeUnit;
	}

	@Override
	public boolean hasNext() {
		return true;
	}

	@Override
	public LocalDateTime next() {
		LocalDateTime result = nextTime;
		nextTime = nextTime.plus(1L, temporalUnit);
		return result;
	}

}
