package com.smijran.carriers;

import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.testng.annotations.Test;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class IndexedStreamTest
{

    @Test
    public void testCollect() throws Exception
    {
        // Given
        final List< Integer > integers = IndexedStream.of( 1, 2, 3 ).collect( Collectors.toList() );

        // Then
        assertThat( integers ).contains( 1, 2, 3 );
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
