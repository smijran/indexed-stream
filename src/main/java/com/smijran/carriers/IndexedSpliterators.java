package com.smijran.carriers;

import java.util.*;
import java.util.ListIterator;
import java.util.Objects;

/**
 * Created by konrad on 01.05.17.
 */
public class IndexedSpliterators
{

    public static < V > IndexedSpliterator< Integer, V > spliterator( V[] array )
    {
        return new IndexedSpliterators.ArrayIndexedSpliterator<>( Objects.requireNonNull( array ) );
    }

    public static < V > IndexedSpliterator< Integer, V > spliterator( V[] array, int fromIndex,
        int toIndex )
    {
        checkFromToBounds( Objects.requireNonNull( array ).length, fromIndex, toIndex );
        return new IndexedSpliterators.ArrayIndexedSpliterator<>( array, fromIndex, toIndex );
    }

    public static < V > IndexedSpliterator< Integer, V > spliterator( List< V > list )
    {
        return new IndexedSpliterators.ListIndexedSpliterator<>( Objects.requireNonNull( list ) );
    }

    public static < K, V > IndexedSpliterator< K, V > spliterator( Map< K, V > map )
    {
        return new IndexedSpliterators.MapIndexedSpliterator<>( Objects.requireNonNull( map ) );
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

    static final class ListIndexedSpliterator< V > implements IndexedSpliterator< Integer, V >
    {
        private final List< V > list;
        private ListIterator< V > iterator;
        private int size;

        public ListIndexedSpliterator( List< V > list )
        {
            this.list = list;
        }

        private void assureInit()
        {
            if( iterator == null )
            {
                iterator = list.listIterator();
                size = list.size();
            }
        }

        @Override
        public boolean tryAdvance( IndexedConsumer< Integer, ? super V > consumer )
        {
            if( consumer == null )
                throw new NullPointerException();
            assureInit();
            if( iterator.hasNext() )
            {
                final V next = iterator.next();
                final int index = iterator.previousIndex();
                consumer.accept( index, next );
                return true;
            }
            return false;
        }

        @Override
        public void forEachRemaining( IndexedConsumer< Integer, ? super V > consumer )
        {
            if( consumer == null )
            {
                throw new NullPointerException();
            }
            assureInit();
            while( iterator.hasNext() )
            {
                final V next = iterator.next();
                final int index = iterator.previousIndex();
                consumer.accept( index, next );
            }
        }

        @Override
        public long estimateSize()
        {
            return size;
        }

        @Override
        public long getExactSizeIfKnown()
        {
            return size;
        }
    }

    static final class MapIndexedSpliterator< K, V > implements IndexedSpliterator< K, V >
    {
        private final Map< K, V > map;
        private Iterator< Map.Entry< K, V > > iterator;
        private int size;

        public MapIndexedSpliterator( Map< K, V > list )
        {
            this.map = list;
        }

        private void assureInit()
        {
            if( iterator == null )
            {
                iterator = map.entrySet().iterator();
                size = map.size();
            }
        }

        @Override
        public boolean tryAdvance( IndexedConsumer< K, ? super V > consumer )
        {
            if( consumer == null )
                throw new NullPointerException();
            assureInit();
            if( iterator.hasNext() )
            {
                final Map.Entry< K, V > next = iterator.next();
                consumer.accept( next.getKey(), next.getValue() );
                return true;
            }
            return false;
        }

        @Override
        public void forEachRemaining( IndexedConsumer< K, ? super V > consumer )
        {
            if( consumer == null )
            {
                throw new NullPointerException();
            }
            assureInit();
            while( iterator.hasNext() )
            {
                final Map.Entry< K, V > next = iterator.next();
                consumer.accept( next.getKey(), next.getValue() );
            }
        }

        @Override
        public long estimateSize()
        {
            return size;
        }

        @Override
        public long getExactSizeIfKnown()
        {
            return size;
        }
    }

}
