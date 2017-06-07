package com.smijran.carriers;

import java.util.Objects;

/**
 * Created by konrad on 01.05.17.
 */
public class IndexedSpliterators
{

    public static < V > IndexedSpliterator< Integer, V > spliterator( V[] array )
    {
        return new IndexedSpliterators.ArrayIndexedSpliterator<>(Objects.requireNonNull(array));
    }

    public static < V > IndexedSpliterator< Integer, V > spliterator( V[] array, int fromIndex,
        int toIndex )
    {
        checkFromToBounds( Objects.requireNonNull( array ).length, fromIndex, toIndex );
        return new IndexedSpliterators.ArrayIndexedSpliterator<>(array, fromIndex, toIndex);
    }

    private static void checkFromToBounds( int arrayLength, int origin, int fence )
    {
        if( origin > fence )
        {
            throw new ArrayIndexOutOfBoundsException(
                "origin(" + origin + ") > fence(" + fence + ")" );
        }
        if( origin < 0 )
        {
            throw new ArrayIndexOutOfBoundsException( origin );
        }
        if( fence > arrayLength )
        {
            throw new ArrayIndexOutOfBoundsException( fence );
        }
    }

    static final class ArrayIndexedSpliterator< V > implements IndexedSpliterator< Integer, V >
    {
        private final Object[] objects;
        private final int terminator;
        private int index;

        public ArrayIndexedSpliterator( Object[] objects )
        {
            this( objects, 0, objects.length );
        }

        /**
         * Creates a spliterator covering the given array and range
         *
         * @param objects
         *            the array, assumed to be unmodified during use
         * @param origin
         *            the least index (inclusive) to cover
         * @param terminator
         *            one past the greatest index to cover
         */
        public ArrayIndexedSpliterator( Object[] objects, int origin, int terminator )
        {
            this.objects = objects;
            this.index = origin;
            this.terminator = terminator;
        }

        @Override
        public boolean tryAdvance( IndexedConsumer< Integer, ? super V > consumer )
        {
            if( consumer == null )
                throw new NullPointerException();
            if( index >= 0 && index < terminator )
            {
                @SuppressWarnings( "unchecked" )
                V e = (V)objects[ index ];
                consumer.accept( index++, e );
                return true;
            }
            return false;
        }

        @Override
        public void forEachRemaining( IndexedConsumer< Integer, ? super V > action )
        {
            Object[] a;
            int i, hi; // hoist accesses and checks from loop
            if( action == null )
                throw new NullPointerException();
            if( (a = objects).length >= (hi = terminator) &&
                (i = index) >= 0 && i < (index = hi) )
            {
                do
                {
                    action.accept( i, (V)a[ i ] );
                }
                while( ++i < hi );
            }
        }

        @Override
        public long estimateSize()
        {
            return objects.length;
        }

        @Override
        public long getExactSizeIfKnown()
        {
            return objects.length;
        }
    }
}
