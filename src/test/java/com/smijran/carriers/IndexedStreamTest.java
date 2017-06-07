package com.smijran.carriers;

import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Java6Assertions.assertThat;

/**
 * Created by kszalkowski on 2017-06-07.
 */
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
    public void testMap() throws Exception
    {
        // Given
        final List< Integer > integers =
            IndexedStream.of( 1, 2, 3 ).map( ( index, value ) -> index ).collect( Collectors.toList() );

        // Then
        assertThat( integers ).contains( 0, 1, 2 );
    }
}
