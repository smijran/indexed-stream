package com.smijran.carriers;

/**
 * Created by konrad on 01.05.17.
 */
public interface IndexedSpliterator< I, V >
{
    boolean tryAdvance( IndexedConsumer< I, ? super V > consumer );

    default void forEachRemaining( IndexedConsumer< I, ? super V > action )
    {
        do
        {
        }
        while( tryAdvance( action ) );
    }
}
