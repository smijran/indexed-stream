package com.smijran.carriers;

import org.mockito.Mockito;
import org.testng.annotations.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;

/**
 * Created by konrad on 01.05.17.
 */
public class IndexedSpliteratorsTest {

    @Test
    public void shouldProduceSpliteratorFromEmptyArray() throws Exception {
        //When
        IndexedSpliterator<Object> spliterator = IndexedSpliterators.spliterator(new Object[0], 0, 0, 0);

        // Then
        assertThat(spliterator).isNotNull();
    }

    @Test
    public void shouldProduceSpliteratorFromEmptyArray2() throws Exception {
        //When
        IndexedSpliterator<Object> spliterator = IndexedSpliterators.spliterator(new Object[0], 0);

        // Then
        assertThat(spliterator).isNotNull();
    }


    @Test
    public void shouldArraySpliteratorCallConsumerInTryAdvance() throws Exception {
        //When
        Object testObject = new Object();
        IndexedSpliterator<Object> spliterator = IndexedSpliterators.spliterator(new Object[]{testObject}, 0);
        IndexedConsumer<Object> consumer = Mockito.mock(IndexedConsumer.class);

        // Then
        boolean advance = spliterator.tryAdvance(consumer);
        assertThat(advance).isTrue();
        Mockito.verify(consumer).accept(0, testObject);
    }

    @Test
    public void shouldArraySpliteratorCallConsumerInForEachRemaining() throws Exception {
        //When
        Object testObject = new Object();
        IndexedSpliterator<Object> spliterator = IndexedSpliterators.spliterator(new Object[]{testObject}, 0);
        IndexedConsumer<Object> consumer = Mockito.mock(IndexedConsumer.class);

        // Then
        spliterator.forEachRemaining(consumer);
        Mockito.verify(consumer).accept(0, testObject);
    }


}