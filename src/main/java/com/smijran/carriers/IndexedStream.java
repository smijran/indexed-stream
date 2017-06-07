package com.smijran.carriers;

import java.util.*;
import java.util.function.*;
import java.util.stream.BaseStream;
import java.util.stream.Collector;
import java.util.stream.Stream;

/**
 * @param <INDEX>
 *            Index type.
 * @param <VALUE>
 *            Value type.
 */
public interface IndexedStream< INDEX, VALUE >extends AutoCloseable
{
    static < V > IndexedStream< Integer, V > of( V ... values )
    {
        return new IndexedReferencePipeline<>( IndexedSpliterators.spliterator( values ) );
    }

    < R > IndexedStream< INDEX, R > map( IndexedFunction< INDEX, VALUE, R > mapper );

    < R, A > R collect( Collector< ? super VALUE, A, R > collector );

    /**
     * Closes this stream, causing all close handlers for this stream pipeline to be called.
     *
     * @see AutoCloseable#close()
     */
    void close();
}
