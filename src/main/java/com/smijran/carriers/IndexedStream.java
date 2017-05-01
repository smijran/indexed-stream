package com.smijran.carriers;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.*;
import java.util.stream.BaseStream;
import java.util.stream.Collector;
import java.util.stream.Stream;

/**
 * Created by konrad on 03.03.17.
 */
public interface IndexedStream<V> extends BaseStream<V, IndexedStream<V>> {

    static <V> IndexedStream<V> range(int from, int to, IntFunction<V> supplier) {
        return null;
    }

    static <V> IndexedStream<V> from(Collection<V> collection, Indexer<V> indexer) {
        return null;
    }

    static <V> IndexedStream<V> from(List<V> list) {
        return null;
    }

    static <V> IndexedStream<V> from(V[] array) {
        return null;
    }

    static <V> IndexedStream<V> from(Supplier<IndexCarrier<V>> supplier) {
        return null;
    }

    static <V> IndexedStreamBuilder<V> builder()
    {
        return new IndexedStreamBuilder<V>();
    }

    <R> Stream<R> map(IntBiFunction<V, R> function);

    <R> IndexedStream<R> map(Function<V, R> function);

    void forEach(Consumer<V> consumer);

    void forEach(IndexedConsumer<V> consumer);

    Stream<IndexCarrier<V>> asIndexedStream();

    Stream<V> asStream();

    IndexedStream<V> filterByIndex(IntPredicate predicate);

    IndexedStream<V> filterByValue(Predicate<V> predicate);

    Object[] toArray();

    <A> A[] toArray(IntFunction<A[]> generator);

    V reduce(V identity, BinaryOperator<V> accumulator);

    Optional<V> reduce(BinaryOperator<V> accumulator);

    <U> U reduce(U identity,
                 BiFunction<U, ? super V, U> accumulator,
                 BinaryOperator<U> combiner);

    <R> R collect(Supplier<R> supplier,
                  BiConsumer<R, ? super V> accumulator,
                  BiConsumer<R, R> combiner);


    <R, A> R collect(Collector<? super V, A, R> collector);

    Optional<V> min(Comparator<? super V> comparator);

    Optional<V> max(Comparator<? super V> comparator);

    long count();

    boolean anyMatch(Predicate<? super V> predicate);

    boolean allMatch(Predicate<? super V> predicate);

    boolean noneMatch(Predicate<? super V> predicate);

    Optional<V> findFirst();

    Optional<V> findAny();


}
