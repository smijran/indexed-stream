package com.smijran.carriers;

import static org.assertj.core.api.Java6Assertions.assertThat;

import java.util.Arrays;
import java.util.Map;

import org.mockito.Mockito;
import org.testng.annotations.Test;
import org.testng.collections.Maps;

/**
 * Created by konrad on 01.05.17.
 */
public class IndexedSpliteratorsTest
{

    @Test
    public void shouldProduceSpliteratorFromEmptyArray() throws Exception
    {
        // When
        IndexedSpliterator< Integer, Object > spliterator =
            IndexedSpliterators.spliterator( new Object[ 0 ], 0, 0 );

        // Then
        assertThat( spliterator ).isNotNull();
    }

    @Test
    public void shouldProduceSpliteratorFromEmptyArray2() throws Exception
    {
        // When
        IndexedSpliterator< Integer, Object > spliterator =
            IndexedSpliterators.spliterator( new Object[ 0 ] );

        // Then
        assertThat( spliterator ).isNotNull();
    }

    @Test
    public void shouldArraySpliteratorCallConsumerInTryAdvance() throws Exception
    {
        // When
        Object testObject = new Object();
        IndexedSpliterator< Integer, Object > spliterator = IndexedSpliterators.spliterator( new Object[]
        { testObject } );
        IndexedConsumer< Integer, Object > consumer = Mockito.mock( IndexedConsumer.class );

        // Then
        boolean advance = spliterator.tryAdvance( consumer );
        assertThat( advance ).isTrue();
        Mockito.verify( consumer ).accept( 0, testObject );
    }

    @Test
    public void shouldArraySpliteratorCallConsumerInForEachRemaining() throws Exception
    {
        // When
        Object testObject = new Object();
        IndexedSpliterator< Integer, Object > spliterator = IndexedSpliterators.spliterator( new Object[]
        { testObject } );
        IndexedConsumer< Integer, Object > consumer = Mockito.mock( IndexedConsumer.class );

        // Then
        spliterator.forEachRemaining( consumer );
        Mockito.verify( consumer ).accept( 0, testObject );
    }

    @Test
    public void shouldProduceSpliteratorFromEmptyList() throws Exception
    {
        // When
        IndexedSpliterator< Integer, Object > spliterator =
            IndexedSpliterators.spliterator( Arrays.asList( new Object[ 0 ] ) );

        // Then
        assertThat( spliterator ).isNotNull();
    }

    @Test
    public void shouldListSpliteratorCallConsumerInTryAdvance() throws Exception
    {
        // When
        Object testObject = new Object();
        IndexedSpliterator< Integer, Object > spliterator =
            IndexedSpliterators.spliterator( Arrays.asList( new Object[]
            { testObject } ) );
        IndexedConsumer< Integer, Object > consumer = Mockito.mock( IndexedConsumer.class );

        // Then
        boolean advance = spliterator.tryAdvance( consumer );
        assertThat( advance ).isTrue();
        Mockito.verify( consumer ).accept( 0, testObject );
    }

    @Test
    public void shouldListSpliteratorCallConsumerInForEachRemaining() throws Exception
    {
        // When
        Object testObject = new Object();
        IndexedSpliterator< Integer, Object > spliterator =
            IndexedSpliterators.spliterator( Arrays.asList( new Object[]
            { testObject } ) );
        IndexedConsumer< Integer, Object > consumer = Mockito.mock( IndexedConsumer.class );

        // Then
        spliterator.forEachRemaining( consumer );
        Mockito.verify( consumer ).accept( 0, testObject );
    }

    @Test
    public void shouldProduceSpliteratorFromEmptyMap() throws Exception
    {
        // When
        IndexedSpliterator< Object, Object > spliterator =
            IndexedSpliterators.spliterator( Maps.< Object, Object > newHashMap() );

        // Then
        assertThat( spliterator ).isNotNull();
    }

    @Test
    public void shouldMapSpliteratorCallConsumerInTryAdvance() throws Exception
    {
        // When
        Object testKey = new Object();
        Object testObject = new Object();
        final Map< Object, Object > map = Maps.newHashMap();
        map.put( testKey, testObject );
        IndexedSpliterator< Object, Object > spliterator =
            IndexedSpliterators.spliterator( map );
        IndexedConsumer< Object, Object > consumer = Mockito.mock( IndexedConsumer.class );

        // Then
        boolean advance = spliterator.tryAdvance( consumer );
        assertThat( advance ).isTrue();
        Mockito.verify( consumer ).accept( testKey, testObject );
    }

    @Test
    public void shouldMapSpliteratorCallConsumerInForEachRemaining() throws Exception
    {
        // When
        Object testKey = new Object();
        Object testObject = new Object();
        final Map< Object, Object > map = Maps.newHashMap();
        map.put( testKey, testObject );
        IndexedSpliterator< Object, Object > spliterator =
            IndexedSpliterators.spliterator( map );
        IndexedConsumer< Object, Object > consumer = Mockito.mock( IndexedConsumer.class );

        // Then
        spliterator.forEachRemaining( consumer );
        Mockito.verify( consumer ).accept( testKey, testObject );
    }
}