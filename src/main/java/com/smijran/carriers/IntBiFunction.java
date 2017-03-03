package com.smijran.carriers;

/**
 * Function which can combine int value (index) and some ambigous value into some result.
 *
 * @author Konrad Sząłkowski
 * @see java.util.stream.IntStream
 * @since 1.0
 */
@FunctionalInterface
public interface IntBiFunction<V, R> {
    /**
     * Function which will produce result from combination of int value and value.
     *
     * @param index Int value (index)
     * @param value Value. Can be null.
     * @return Result.
     */
    R apply(int index, V value);
}
