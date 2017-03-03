package com.smijran.carriers;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.IntFunction;

/**
 * Factor class for {@link IndexCarrier}. Provide with variety of starting points.
 *
 * @author Konrad Sząłkowski
 * @since 1.0
 */
public final class Carriers {
    /**
     * Create single instance of {@link IndexCarrier}.
     *
     * @param index Index for {@link IndexCarrier}
     * @param value Value for {@link IndexCarrier}. Can be null.
     * @param <V>   Type of the value.
     * @return IndexCarrier object.
     */
    public static <V> IndexCarrier<V> create(int index, V value) {
        return new IndexCarrier<V>(index, value);
    }

    /**
     * Method which allows to create a function which accepts an int and produces the {@link IndexCarrier} based on value supplied by {@link IntFunction}.
     *
     * @param supplier Supplier for values given by ints.
     * @param <V>      Type of the value.
     * @return IntFunction producing {@link IndexCarrier}.
     */
    public static <V> IntFunction<IndexCarrier<V>> enumerate(IntFunction<V> supplier) {
        Objects.requireNonNull(supplier, "Supllier is missing.");
        return (index) ->
                create(index, supplier.apply(index));
    }

    /**
     * Method which allows to create a function which accepts a value and returns {@link IndexCarrier} with an index supplied by the {@link Indexer}.
     *
     * @param indexer Indexer to index values vy.
     * @param <V> Type of the value.
     * @return Function producing {@link IndexCarrier}.
     */
    public static <V> Function<V, IndexCarrier<V>> index(Indexer<V> indexer) {
        Objects.requireNonNull(indexer, "Indexer is missing.");
        return (value) ->
                create(indexer.computeIndex(value), value);
    }
}
