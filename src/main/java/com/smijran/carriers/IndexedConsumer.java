package com.smijran.carriers;

/**
 * Interface of consumer.
 * @param <I> Index type.
 * @param <V> Value type.
 */
@FunctionalInterface
public interface IndexedConsumer<I, V> {
    /**
     * Transform index and value.
     * @param index Index.
     * @param value Value.
     */
    void accept(I index, V value);
}
