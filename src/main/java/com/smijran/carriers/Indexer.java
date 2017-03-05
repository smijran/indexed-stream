package com.smijran.carriers;

/**
 * Interface of a function which will supply index given the value.
 *
 * @author Konrad Sząłkowski
 * @see java.util.stream.IntStream
 * @since 1.0
 */
@FunctionalInterface
public interface Indexer<V> {
    /**
     * Method should return a index of given value.
     *
     * @param value Value to compute index for.
     * @return Index.
     */
    int computeIndex(V value);
}
