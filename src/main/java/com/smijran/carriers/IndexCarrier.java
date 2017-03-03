package com.smijran.carriers;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.function.Predicate;

/**
 * Simple class that bounds together index and value. Will be useful in next steps of this project.
 *
 * @author Konrad Sząłkowski
 * @see java.util.stream.IntStream
 * @since 1.0
 */
final class IndexCarrier<V> implements Serializable {
    private final int index;
    private final V value;

    IndexCarrier(int index, V value) {
        this.index = index;
        this.value = value;
    }

    /**
     * Returns index of the value.
     *
     * @return Index.
     */
    public int index() {
        return index;
    }

    /**
     * Returns index of the value.
     *
     * @return Index.
     */
    public int getIndex() {
        return index;
    }

    /**
     * Returns value.
     *
     * @return Value.
     */
    public V value() {
        return value;
    }

    /**
     * Returns value.
     *
     * @return Value.
     */
    public V getValue() {
        return value;
    }

    /**
     * True if value is not null.
     *
     * @return True if value is not null.
     */
    public boolean isPresent() {
        return value != null;
    }

    /**
     * Transforms object value using function.
     *
     * @param function Function to be applied.
     * @param <T>      Type of the new object produced.
     * @return Index carrier with transformed object.
     */
    public <T> IndexCarrier<T> map(Function<V, T> function) {
        Objects.requireNonNull(function, "Missing function.");
        return new IndexCarrier<T>(index, function.apply(value));
    }

    /**
     * Transforms object value using function.
     *
     * @param function Function to be applied.
     * @param <T>      Type of the new object produced.
     * @return Index carrier with transformed object.
     */
    public <T> T map(IntBiFunction<V, T> function) {
        Objects.requireNonNull(function, "Missing function.");
        return function.apply(index, value);
    }

    /**
     * Test if value satisfiers the predicate.
     *
     * @param predicate Predicate to be tested.
     * @return True if predicate is satisfied.
     */
    public boolean test(Predicate<V> predicate) {
        Objects.requireNonNull(predicate, "Missing predicate.");
        return predicate.test(value);
    }

    /**
     * Test if index satisfiers the predicate.
     *
     * @param predicate Predicate to be tested.
     * @return True if predicate is satisfied.
     */
    public boolean test(IntPredicate predicate) {
        Objects.requireNonNull(predicate, "Missing predicate.");
        return predicate.test(index);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IndexCarrier<?> that = (IndexCarrier<?>) o;

        if (index != that.index) return false;
        return value != null ? value.equals(that.value) : that.value == null;
    }

    @Override
    public int hashCode() {
        int result = index;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "IndexCarrier{" +
                "index=" + index +
                ", value=" + value +
                '}';
    }
}
