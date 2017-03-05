package com.smijran.carriers;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by konrad on 05.03.17.
 */
public class IndexedStreamTest {

    @Test
    public void shouldCreateFromArray() {
        // Given
        final String[] test = {"1", "2", "3"};

        // When
        final IndexedStream<String> indexedStream = IndexedStream.from(test);

        // Assert
        Assert.assertNotNull(indexedStream);
    }

    @Test
    public void shouldCreateFromList() {
        // Given
        final List<String> test = Arrays.asList("1", "2", "3");

        // When
        final IndexedStream<String> indexedStream = IndexedStream.from(test);

        // Assert
        Assert.assertNotNull(indexedStream);
    }

    @Test
    public void shouldCreateFromCollection() {
        // Given
        final List<String> test = Arrays.asList("1", "2", "3");

        // When
        final IndexedStream<String> indexedStream = IndexedStream.from(test, test::indexOf);

        // Assert
        Assert.assertNotNull(indexedStream);
    }

    @Test
    public void shouldCreateFromRange() {
        // Given
        final List<String> test = Arrays.asList("1", "2", "3");

        // When
        final IndexedStream<String> indexedStream = IndexedStream.range(0, test.size(), test::get);

        // Assert
        Assert.assertNotNull(indexedStream);
    }


    @Test
    public void shouldMapWithIndex()
    {
        // Given
        final List<String> test = Arrays.asList("1", "2", "3");

        // When
        final List<String> result = IndexedStream
                .from(test)
                .map((index, value) -> index + value)
                .collect(Collectors.toList());

        // Assert
        Assert.assertEquals(Arrays.asList("11", "22", "#3"), result);
    }

    @Test
    public void shouldForEachWithIndex()
    {
        // Given
        final List<String> test = Arrays.asList("1", "2", "3");

        // When
        IndexedStream
                .from(test)
                // Then
                .forEach((index, value) -> Assert.assertEquals(index, Integer.valueOf(value).intValue()));
    }

    @Test
    public void shouldCollectToArray()
    {
        // Given
        final List<String> test = Arrays.asList("1", "2", "3");

        // When
        final Object[] array = IndexedStream
                .from(test)
                .toArray();

        // Then
        Assert.assertEquals(new String[]{"1", "2", "3"}, array);
    }

}