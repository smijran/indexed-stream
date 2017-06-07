package com.smijran.carriers;

/**
 * Created by konrad on 07.06.17.
 */
public interface IndexedPredicate<INDEX, VALUE> {

    boolean test(INDEX index, VALUE value);
}