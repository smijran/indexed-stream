package com.smijran.carriers;

import org.testng.annotations.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.time.LocalDateTime.parse;
import static org.mockito.Mockito.*;

/**
 * Created on 08.06.2017.
 */
public class TwoWaySpliteratorTest {

	@Test
	public void shouldConsumeInSync() {
		// given
		IndexedSpliterator<Integer, String> s = new TwoWaySpliterator<>(IntStream.of(0, 1, 2, 3, 4)
		 .boxed().iterator(), Stream.of("a", "b", "c", "d").iterator());
		IndexedConsumer<Integer, String> consumer = (IndexedConsumer<Integer, String>) mock
		 (IndexedConsumer.class);

		// when
		s.forEachRemaining(consumer);

		// then
		verify(consumer).accept(0, "a");
		verify(consumer).accept(1, "b");
		verify(consumer).accept(2, "c");
		verify(consumer).accept(3, "d");
		verifyNoMoreInteractions(consumer);
	}

	@Test
	public void tsmUseCase() {
		// given
		LocalDateTime startTime = parse("2017-06-08T00:00");
		TemporalUnit timeUnit = ChronoUnit.HOURS;
		List<Integer> dailyValues = Arrays.asList(2, 2, 3, 3, 4, 5, 6, 8, 12, 10, 10, 9, 6, 5, 9, 11,
		 14, 11, 9, 8, 5, 4, 4, 3);

		// when
		IndexedSpliterator<LocalDateTime, Integer> s = new TwoWaySpliterator<>(new TimeIterator
		 (startTime, timeUnit), dailyValues.iterator());
		IndexedConsumer<LocalDateTime, Integer> consumer = (IndexedConsumer<LocalDateTime, Integer>)
		 mock(IndexedConsumer.class);
		s.forEachRemaining(consumer);

		// then
		verify(consumer).accept(parse("2017-06-08T00:00"), 2);
		verify(consumer).accept(parse("2017-06-08T01:00"), 2);
		verify(consumer).accept(parse("2017-06-08T02:00"), 3);
		verify(consumer).accept(parse("2017-06-08T03:00"), 3);
		verify(consumer).accept(parse("2017-06-08T04:00"), 4);
		verify(consumer).accept(parse("2017-06-08T05:00"), 5);
		verify(consumer).accept(parse("2017-06-08T06:00"), 6);
		verify(consumer).accept(parse("2017-06-08T07:00"), 8);
		verify(consumer).accept(parse("2017-06-08T08:00"), 12);
		verify(consumer).accept(parse("2017-06-08T09:00"), 10);

		verify(consumer).accept(parse("2017-06-08T10:00"), 10);
		verify(consumer).accept(parse("2017-06-08T11:00"), 9);
		verify(consumer).accept(parse("2017-06-08T12:00"), 6);
		verify(consumer).accept(parse("2017-06-08T13:00"), 5);
		verify(consumer).accept(parse("2017-06-08T14:00"), 9);
		verify(consumer).accept(parse("2017-06-08T15:00"), 11);
		verify(consumer).accept(parse("2017-06-08T16:00"), 14);
		verify(consumer).accept(parse("2017-06-08T17:00"), 11);
		verify(consumer).accept(parse("2017-06-08T18:00"), 9);
		verify(consumer).accept(parse("2017-06-08T19:00"), 8);

		verify(consumer).accept(parse("2017-06-08T20:00"), 5);
		verify(consumer).accept(parse("2017-06-08T21:00"), 4);
		verify(consumer).accept(parse("2017-06-08T22:00"), 4);
		verify(consumer).accept(parse("2017-06-08T23:00"), 3);

		verifyNoMoreInteractions(consumer);
	}

}
