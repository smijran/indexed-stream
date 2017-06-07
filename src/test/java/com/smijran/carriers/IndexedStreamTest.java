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
    public void testMap() throws Exception {
        // Given
        final List<Integer> integers =
                IndexedStream.of(1, 2, 3).map((index, value) -> index * value).collect(Collectors.toList());

        // Then
        assertThat(integers).contains(0, 2, 6);
    }
}
