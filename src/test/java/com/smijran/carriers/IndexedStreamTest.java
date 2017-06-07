package com.smijran.carriers;

import org.testng.annotations.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class IndexedStreamTest {

    @Test
    public void testCollect() throws Exception {
        // Given
        final List<Integer> integers = IndexedStream.of(1, 2, 3).collect(Collectors.toList());

        // Then
        assertThat(integers).contains(1, 2, 3);
    }

    @Test
    public void testIndexedMap() throws Exception {
        // Given
        final List<Integer> integers =
                IndexedStream.of(1, 2, 3).map((index, value) -> index * value).collect(Collectors.toList());

        // Then
        assertThat(integers).contains(0, 2, 6);
    }

    @Test
    public void testSimpleMap() {
        // Given
        final List<Integer> integers =
                IndexedStream.of(1, 2, 3).map((value) -> 2 * value).collect(Collectors.toList());

        // Then
        assertThat(integers).contains(2, 4, 6);
    }


    @Test
    public void testIndexedFilter() {
        // Given
        final List<Integer> integers =
                IndexedStream.of(1, 2, 3).filter((index, value) -> index >= 2).collect(Collectors.toList());

        // Then
        assertThat(integers).contains(3);
    }

    @Test
    public void testSimpleFilter() {
        // Given
        final List<Integer> integers =
                IndexedStream.of(1, 2, 3).filter((value) -> value > 2).collect(Collectors.toList());

        // Then
        assertThat(integers).contains(3);
    }
}
