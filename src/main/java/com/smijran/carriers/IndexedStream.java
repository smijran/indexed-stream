package com.smijran.carriers;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;

/**
 * @param <INDEX> Index type.
 * @param <VALUE> Value type.
 */
public interface IndexedStream<INDEX, VALUE> extends AutoCloseable {
    static <V> IndexedStream<Integer, V> of(V... values) {
        return new IndexedReferencePipeline.Head<>(IndexedSpliterators.spliterator(values));
    }


    IndexedStream<INDEX, VALUE> filter(IndexedPredicate<INDEX, ? super VALUE> valuePredicate);

    IndexedStream<INDEX, VALUE> filter(Predicate<? super VALUE> valuePredicate);

    <R> IndexedStream<INDEX, R> map(Function<? super VALUE, ? extends R> mapper);

    <R> IndexedStream<INDEX, R> map(IndexedFunction<INDEX, ? super VALUE, ? extends R> mapper);

    <R, A> R collect(Collector<? super VALUE, A, R> collector);

    /**
     * Closes this stream, causing all close handlers for this stream pipeline to be called.
     *
     * @see AutoCloseable#close()
     */
    void close();
}
