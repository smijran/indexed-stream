package com.smijran.carriers;

/**
 * Created by konrad on 05.03.17.
 */
@FunctionalInterface
public interface IndexedConsumer<V> {
    void accept(int first, V value);
}
