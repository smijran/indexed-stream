package com.smijran.carriers;

import java.util.*;
import java.util.function.*;
import java.util.stream.BaseStream;
import java.util.stream.Collector;
import java.util.stream.Stream;

/**
 *
 * @param <I> Index type.
 * @param <V> Value type.
 */
public interface IndexedStream<I, V> extends AutoCloseable {

    /**
     * Closes this stream, causing all close handlers for this stream pipeline
     * to be called.
     *
     * @see AutoCloseable#close()
     */
    void close();
}
