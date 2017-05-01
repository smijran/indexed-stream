package com.smijran.carriers;

import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;

/**
 * Created by konrad on 01.05.17.
 */
public class IndexedSpliterators {

    public static <T> IndexedSpliterator<T> spliterator(Object[] array,
                                                 int additionalCharacteristics) {
        return new IndexedSpliterators.ArrayIndexedSpliterator<>(Objects.requireNonNull(array),
                additionalCharacteristics);
    }

    public static <T> IndexedSpliterator<T> spliterator(Object[] array, int fromIndex, int toIndex,
                                                 int additionalCharacteristics) {
        checkFromToBounds(Objects.requireNonNull(array).length, fromIndex, toIndex);
        return new IndexedSpliterators.ArrayIndexedSpliterator<>(array, fromIndex, toIndex, additionalCharacteristics);
    }

    private static void checkFromToBounds(int arrayLength, int origin, int fence) {
        if (origin > fence) {
            throw new ArrayIndexOutOfBoundsException(
                    "origin(" + origin + ") > fence(" + fence + ")");
        }
        if (origin < 0) {
            throw new ArrayIndexOutOfBoundsException(origin);
        }
        if (fence > arrayLength) {
            throw new ArrayIndexOutOfBoundsException(fence);
        }
    }

    static final class ArrayIndexedSpliterator<V> implements IndexedSpliterator<V> {
        private final Object[] objects;
        private final int terminator;
        private final int characteristics;
        private int index;

        public ArrayIndexedSpliterator(Object[] objects, int additionalCharacteristics) {
            this(objects, 0, objects.length, additionalCharacteristics);
        }

        /**
         * Creates a spliterator covering the given array and range
         *
         * @param objects                   the array, assumed to be unmodified during use
         * @param origin                    the least index (inclusive) to cover
         * @param terminator                one past the greatest index to cover
         * @param additionalCharacteristics Additional spliterator characteristics
         *                                  of this spliterator's source or elements beyond {@code SIZED} and
         *                                  {@code SUBSIZED} which are are always reported
         */
        public ArrayIndexedSpliterator(Object[] objects, int origin, int terminator, int additionalCharacteristics) {
            this.objects = objects;
            this.index = origin;
            this.terminator = terminator;
            this.characteristics = additionalCharacteristics | Spliterator.SIZED | Spliterator.SUBSIZED;
        }


        @Override
        public boolean tryAdvance(IndexedConsumer<? super V> consumer) {
            Objects.requireNonNull(consumer);
            if (index >= 0 && index < terminator) {
                @SuppressWarnings("unchecked") V e = (V) objects[index];
                consumer.accept(index, e);
                index++;
                return true;
            }
            return false;
        }

        @SuppressWarnings("unchecked")
        @Override
        public void forEachRemaining(IndexedConsumer<? super V> action) {
            Objects.requireNonNull(action);
            Object[] a;
            int i, hi;
            if ((a = objects).length >= (hi = terminator) &&
                    (i = index) >= 0 && i < (index = hi)) {
                do {
                    action.accept(i, (V) a[i]);
                } while (++i < hi);
            }
        }

        @Override
        public int characteristics() {
            return characteristics;
        }
    }
}
