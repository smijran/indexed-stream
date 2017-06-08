package com.smijran.carriers;

import static org.assertj.core.api.Java6Assertions.assertThat;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.assertj.core.util.Lists;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.testng.annotations.Test;

public class IndexedStreamTest
{

    @Test
    public void testNoneMatch() throws Exception
    {
        // Given
        boolean result = IndexedStream.of( 1, 2, 3 ).noneMatch( ( a ) -> a.equals( 4 ) );

        // Then
        assertThat( result ).isTrue();
    }

    @Test
    public void testNoneMatch2() throws Exception
    {
        // Given
        boolean result = IndexedStream.of( 1, 2, 3 ).noneMatch( ( a ) -> a.equals( 3 ) );

        // Then
        assertThat( result ).isFalse();
    }

    @Test
    public void testAnyMatch() throws Exception
    {
        // Given
        boolean result = IndexedStream.of( 1, 2, 3 ).anyMatch( ( a ) -> a.equals( 4 ) );

        // Then
        assertThat( result ).isFalse();
    }

    @Test
    public void testAnyMatch2() throws Exception
    {
        // Given
        boolean result = IndexedStream.of( 1, 2, 3 ).anyMatch( ( a ) -> a.equals( 3 ) );

        // Then
        assertThat( result ).isTrue();
    }

    @Test
    public void testAllMatch() throws Exception
    {
        // Given
        boolean result = IndexedStream.of( 1, 2, 3 ).allMatch( ( a ) -> a instanceof Integer );

        // Then
        assertThat( result ).isTrue();
    }

    @Test
    public void testAllMatch2() throws Exception
    {
        // Given
        boolean result = IndexedStream.of( 1, 2, 3 ).allMatch( ( a ) -> a.equals( 3 ) );

        // Then
        assertThat( result ).isFalse();
    }

    @Test
    public void testIndexedNoneMatch() throws Exception
    {
        // Given
        boolean result = IndexedStream.of( 1, 2, 3 ).noneMatch( ( index, a ) -> a.equals( 4 ) );

        // Then
        assertThat( result ).isTrue();
    }

    @Test
    public void testIndexedNoneMatch2() throws Exception
    {
        // Given
        boolean result = IndexedStream.of( 1, 2, 3 ).noneMatch( ( index, a ) -> a.equals( 3 ) );

        // Then
        assertThat( result ).isFalse();
    }

    @Test
    public void testIndexedAnyMatch() throws Exception
    {
        // Given
        boolean result = IndexedStream.of( 1, 2, 3 ).anyMatch( ( index, a ) -> a.equals( 4 ) );

        // Then
        assertThat( result ).isFalse();
    }

    @Test
    public void testIndexedAnyMatch2() throws Exception
    {
        // Given
        boolean result = IndexedStream.of( 1, 2, 3 ).anyMatch( ( index, a ) -> a.equals( 3 ) );

        // Then
        assertThat( result ).isTrue();
    }

    @Test
    public void testIndexedAllMatch() throws Exception
    {
        // Given
        boolean result = IndexedStream.of( 1, 2, 3 ).allMatch( ( index, a ) -> a instanceof Integer );

        // Then
        assertThat( result ).isTrue();
    }

    @Test
    public void testIndexedAllMatch2() throws Exception
    {
        // Given
        boolean result = IndexedStream.of( 1, 2, 3 ).allMatch( ( index, a ) -> a.equals( 3 ) );

        // Then
        assertThat( result ).isFalse();
    }

    @Test
    public void testFindFirst() throws Exception
    {
        // Given
        final Optional< Integer > found = IndexedStream.of( 1, 2, 3 ).findFirst();

        // Then
        assertThat( found.isPresent() ).isTrue();
        assertThat( found.get() ).isEqualTo( 1 );
    }

    @Test
    public void testFindFirst2() throws Exception
    {
        // Given
        final Optional< Integer > found = IndexedStream.of( new Integer[ 0 ] ).findFirst();

        // Then
        assertThat( found.isPresent() ).isFalse();
    }

    @Test
    public void testFindAny() throws Exception
    {
        // Given
        final Optional< Integer > found = IndexedStream.of( 1, 2, 3 ).findAny();

        // Then
        assertThat( found.isPresent() ).isTrue();
    }

    @Test
    public void testFindAny2() throws Exception
    {
        // Given
        final Optional< Integer > found = IndexedStream.of( new Integer[ 0 ] ).findAny();

        // Then
        assertThat( found.isPresent() ).isFalse();
    }

    @Test
    public void testCollect() throws Exception
    {
        // Given
        final List< Integer > integers = IndexedStream.of( 1, 2, 3 ).collect( Collectors.toList() );

        // Then
        assertThat( integers ).contains( 1, 2, 3 );
    }

    @Test
    public void testCollect2() throws Exception
    {
        // Given
        final List< Integer > integers =
            IndexedStream.of( 1, 2, 3 ).collect( Lists::newArrayList, List::add, ( a, b ) -> a.addAll( b ) );

        // Then
        assertThat( integers ).contains( 1, 2, 3 );
    }

    @Test
    public void testReduce() throws Exception
    {
        // Given
        final Integer sum = IndexedStream.of( 1, 2, 3 ).reduce( 0, ( a, b ) -> a + b );

        // Then
        assertThat( sum ).isEqualTo( 6 );
    }

    @Test
    public void testReduce2() throws Exception
    {
        // Given
        final Optional< Integer > sum = IndexedStream.of( 1, 2, 3 ).reduce( ( a, b ) -> a + b );

        // Then
        assertThat( sum.isPresent() ).isTrue();
        assertThat( sum.get() ).isEqualTo( 6 );
    }

    @Test
    public void testReduce3() throws Exception
    {
        // Given
        final Integer sum = IndexedStream.of( 1, 2, 3 ).reduce( 0, ( a, b ) -> a + b, ( a, b ) -> a + b );

        // Then
        assertThat( sum ).isEqualTo( 6 );
    }

    @Test
    public void testMin() throws Exception
    {
        // Given
        final Optional< Integer > min = IndexedStream.of( 1, 2, 3 ).min( Comparator.naturalOrder() );

        // Then
        assertThat( min.isPresent() ).isTrue();
        assertThat( min.get() ).isEqualTo( 1 );
    }

    @Test
    public void testMax() throws Exception
    {
        // Given
        final Optional< Integer > max = IndexedStream.of( 1, 2, 3 ).max( Comparator.naturalOrder() );

        // Then
        assertThat( max.isPresent() ).isTrue();
        assertThat( max.get() ).isEqualTo( 3 );
    }

    @Test
    public void testIndexedMap() throws Exception
    {
        // Given
        final List< Integer > integers =
            IndexedStream.of( 1, 2, 3 ).map( ( index, value ) -> index * value )
                .collect( Collectors.toList() );

        // Then
        assertThat( integers ).contains( 0, 2, 6 );
    }

    @Test
    public void testSimpleMap()
    {
        // Given
        final List< Integer > integers =
            IndexedStream.of( 1, 2, 3 ).map( ( value ) -> 2 * value ).collect( Collectors.toList() );

        // Then
        assertThat( integers ).contains( 2, 4, 6 );
    }

    @Test
    public void testIndexedFilter()
    {
        // Given
        final List< Integer > integers =
            IndexedStream.of( 1, 2, 3 ).filter( ( index, value ) -> index >= 2 )
                .collect( Collectors.toList() );

        // Then
        assertThat( integers ).contains( 3 );
    }

    @Test
    public void testSimpleFilter()
    {
        // Given
        final List< Integer > integers =
            IndexedStream.of( 1, 2, 3 ).filter( ( value ) -> value > 2 ).collect( Collectors.toList() );

        // Then
        assertThat( integers ).contains( 3 );
    }

    @Test
    public void testForEach()
    {
        // Create consumer mock
        final Consumer< Integer > mock = Mockito.mock( Consumer.class );
        final ArgumentCaptor< Integer > captor = ArgumentCaptor.forClass( Integer.class );
        Mockito.doNothing().when( mock ).accept( captor.capture() );

        // When
        IndexedStream.of( 1, 2, 3 ).forEach( mock );

        // Assert
        assertThat( captor.getAllValues() ).contains( 1, 2, 3 );
    }

    @Test
    public void testIndexedForEach()
    {
        // Create consumer mock
        final IndexedConsumer< Integer, Integer > mock = Mockito.mock( IndexedConsumer.class );
        final ArgumentCaptor< Integer > indexCaptor = ArgumentCaptor.forClass( Integer.class );
        final ArgumentCaptor< Integer > captor = ArgumentCaptor.forClass( Integer.class );
        Mockito.doNothing().when( mock ).accept( indexCaptor.capture(), captor.capture() );

        // When
        IndexedStream.of( 1, 2, 3 ).forEach( mock );

        // Assert
        assertThat( indexCaptor.getAllValues() ).contains( 0, 1, 2 );
        assertThat( captor.getAllValues() ).contains( 1, 2, 3 );
    }

    @Test
    public void testCount()
    {
        // Given
        final long count = IndexedStream.of( 1, 2, 3 ).count();

        // Then
        assertThat( count ).isEqualTo( 3L );
    }

    @Test
    public void testCountFilter()
    {
        // Given
        final long count = IndexedStream.of( 1, 2, 3 ).filter( ( index, value ) -> index > 1 ).count();

        // Then
        assertThat( count ).isEqualTo( 1L );
    }
}
