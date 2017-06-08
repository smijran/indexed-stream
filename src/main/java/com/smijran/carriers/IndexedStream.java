package com.smijran.carriers;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;

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
        return new IndexedReferencePipeline.Head<>( IndexedSpliterators.spliterator( values ) );
    }

    static < V > IndexedStream< Integer, V > of( List< V > aList )
    {
        return new IndexedReferencePipeline.Head<>( IndexedSpliterators.spliterator( aList ) );
    }

    static < I, V > IndexedStream< I, V > of( Iterator< I > indexIterator, Iterator< V > valueIterator )
    {
        return new IndexedReferencePipeline.Head<>( new TwoWaySpliterator<>( indexIterator, valueIterator ) );
    }

    static < K, V > IndexedStream< K, V > of( Map< K, V > map )
    {
        return new IndexedReferencePipeline.Head<>( IndexedSpliterators.spliterator( map ) );
    }

    IndexedStream< INDEX, VALUE > filter( IndexedPredicate< INDEX, ? super VALUE > valuePredicate );

    IndexedStream< INDEX, VALUE > filter( Predicate< ? super VALUE > valuePredicate );

    < R > IndexedStream< INDEX, R > map( Function< ? super VALUE, ? extends R > mapper );

    < R > IndexedStream< INDEX, R > map( IndexedFunction< INDEX, ? super VALUE, ? extends R > mapper );

    < R, A > R collect( Collector< ? super VALUE, A, R > collector );

    < R > R collect( Supplier< R > supplier,
        BiConsumer< R, ? super VALUE > accumulator,
        BiConsumer< R, R > combiner );

    VALUE reduce( final VALUE identity, final BinaryOperator< VALUE > accumulator );

    Optional< VALUE > reduce( BinaryOperator< VALUE > accumulator );

    < R > R reduce( R identity, BiFunction< R, ? super VALUE, R > accumulator, BinaryOperator< R > combiner );

    Optional< VALUE > max( Comparator< ? super VALUE > comparator );

    Optional< VALUE > min( Comparator< ? super VALUE > comparator );

    long count();

    Optional<VALUE> findFirst();

    Optional<VALUE> findAny();

    void forEach(Consumer< ? super VALUE > consumer );

    void forEach( IndexedConsumer< INDEX, ? super VALUE > consumer );

    /**
     * Closes this stream, causing all close handlers for this stream pipeline to be called.
     *
     * @see AutoCloseable#close()
     */
    void close();
}
