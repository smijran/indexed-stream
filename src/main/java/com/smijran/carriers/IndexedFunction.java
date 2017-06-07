package com.smijran.carriers;

import java.util.Objects;
import java.util.function.Function;

/**
 * Created by kszalkowski on 2017-06-07.
 */
public interface IndexedFunction< INDEX, VALUE, RESULT >
{
    /**
     * Applies this function to the given argument.
     *
     * @param t
     *            the function argument
     * @return the function result
     */
    RESULT apply( INDEX index, VALUE t );

    /**
     * Returns a function that always returns its input argument.
     *
     * @param <T>
     *            the type of the input and output objects to the function
     * @return a function that always returns its input argument
     */
    static < INDEX, T > IndexedFunction< INDEX, T, T > identity()
    {
        return ( i, t ) -> t;
    }
}
