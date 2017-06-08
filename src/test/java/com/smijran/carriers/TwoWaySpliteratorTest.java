package com.smijran.carriers;

import org.testng.annotations.Test;

import java.util.stream.IntStream;
import java.util.stream.Stream;

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

}
