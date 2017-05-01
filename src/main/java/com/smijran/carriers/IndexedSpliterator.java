package com.smijran.carriers;

/**
 * Created by konrad on 01.05.17.
 */
public interface IndexedSpliterator<V> {
    boolean tryAdvance(IndexedConsumer<? super V> consumer);

    default void forEachRemaining(IndexedConsumer<? super V> action) {
        do { } while (tryAdvance(action));
    }

    int characteristics();

    default boolean hasCharacteristics(int characteristics) {
        return (characteristics() & characteristics) == characteristics;
    }

    int ORDERED    = 0x00000010;

    int DISTINCT   = 0x00000001;

    int SORTED     = 0x00000004;

    int SIZED      = 0x00000040;

    int NONNULL    = 0x00000100;

    int IMMUTABLE  = 0x00000400;

    int CONCURRENT = 0x00001000;

    int SUBSIZED = 0x00004000;




}
