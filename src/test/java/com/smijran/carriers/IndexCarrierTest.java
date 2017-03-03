package com.smijran.carriers;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Tests for sample int carrier
 */
public class IndexCarrierTest {

    @Test
    public void shouldCreateIntCarrier() {
        // Given
        final Object value = new Object();

        // When
        final IndexCarrier<Object> carrier = Carriers.create(1, value);

        // Then
        Assert.assertEquals(1, carrier.index());
        Assert.assertEquals(value, carrier.value());
        Assert.assertEquals(1, carrier.getIndex());
        Assert.assertEquals(value, carrier.getValue());
    }

    @Test
    public void shouldMapValue() throws Exception {
        // Given
        final Object value = new Object();

        // When
        final IndexCarrier<Object> carrier = Carriers.create(1, value);
        final IndexCarrier<Integer> carrierMapped = carrier.map(Object::hashCode);

        // Then
        Assert.assertNotSame(carrier, carrierMapped);
        Assert.assertEquals(value.hashCode(), (int)carrierMapped.getValue());
    }

    @Test
    public void shouldMapBiValue() throws Exception {
        // Given
        final Object value = new Object();
        final Object after = new Object();

        // When
        final IndexCarrier<Object> carrier = Carriers.create(1, value);
        final Object result = carrier.map((i, val) -> after);

        // Then
        Assert.assertEquals(after, result);
    }

    @Test
    public void shouldMapIntStream()
    {
        // Given
        final List<String> list  = Arrays.asList("I", "am", "terminator");

        // When
        String result = IntStream
                .range(0, list.size())
                .mapToObj(Carriers.enumerate(list::get))
                .map(ic -> ic.index() + ":" + ic.value())
                .collect(Collectors.joining(","));

        // Then
        Assert.assertEquals("0:I,1:am,2:terminator", result);
    }

    @Test
    public void shouldMapStringStream()
    {
        // Given
        final List<String> list  = Arrays.asList("I", "am", "terminator");

        // When
        String result = list
                .stream()
                .map(Carriers.index(list::indexOf))
                .map(ic -> ic.index() + ":" + ic.value())
                .collect(Collectors.joining(","));

        // Then
        Assert.assertEquals("0:I,1:am,2:terminator", result);
    }
}