package com.smijran.carriers;

import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Iterator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.testng.Assert.*;

/**
 * Created on 08.06.2017.
 */
public class TimeIteratorTest {

	@Test
	public void shouldReturnCorrectTimestamps() {
		// given
		Iterator<LocalDateTime> iter = new TimeIterator(LocalDateTime.parse("2017-06-06T06:00"),
		 ChronoUnit.HOURS);

		// then
		assertThat(iter.next()).isEqualTo("2017-06-06T06:00");
		assertThat(iter.next()).isEqualTo("2017-06-06T07:00");
		assertThat(iter.next()).isEqualTo("2017-06-06T08:00");
		assertThat(iter.next()).isEqualTo("2017-06-06T09:00");
		assertThat(iter.next()).isEqualTo("2017-06-06T10:00");
	}

//	@Test
//	public void shouldThrowWhenMismatch() {
//		// then
//		expectedException.expect(IllegalArgumentException.class);
//
//		// when
//		new TimeIterator(LocalDateTime.parse("2017-06-06T06:06"), ChronoUnit.HOURS);
//	}

	@Test
	public void shouldThrowWhenMismatch() {
		try {
			new TimeIterator(LocalDateTime.parse("2017-06-06T06:06"), ChronoUnit.HOURS);
		} catch (IllegalArgumentException e) {
			return;
		}
		Assertions.fail("expected illegalArgumente");
	}

	@Test
	public void shouldBeInfiniteAndAscending() {
		// given
		Iterator<LocalDateTime> iter = new TimeIterator(LocalDateTime.parse("2017-06-06T06:06"),
		 ChronoUnit.MINUTES);
		LocalDateTime prev = null;

		// when-then
		for (int i = 0; i < 1_000_000; i++) {
			assertThat(iter.hasNext()).isTrue();
			LocalDateTime ldt = iter.next();
			// assertThat(ldt).isNotEqualTo(prev);
			assertThat(ldt).isNotNull();
			if (prev != null) {
				assertThat(ldt).isAfter(prev);
			}
			prev = ldt;
		}
	}

}