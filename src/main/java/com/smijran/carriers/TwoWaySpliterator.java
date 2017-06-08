package com.smijran.carriers;

import java.util.Iterator;

/**
 * Created on 08.06.2017.
 */
public class TwoWaySpliterator<I, V> implements IndexedSpliterator<I, V> {

	private final Iterator<I> indices;
	private final Iterator<V> values;

	public TwoWaySpliterator(Iterator<I> indices, Iterator<V> values) {
		this.indices = indices;
		this.values = values;
	}

	@Override
	public boolean tryAdvance(IndexedConsumer<I, ? super V> consumer) {
		if (consumer == null) throw new NullPointerException();
		if (!values.hasNext()) {
			return false;
		}
		if (!indices.hasNext()) {
			throw new IllegalStateException("insufficient indices");
		}
		consumer.accept(indices.next(), values.next());
		return true;
	}

	@Override
	public void forEachRemaining(IndexedConsumer<I, ? super V> action) {
		do {
		} while (tryAdvance(action));
	}

	@Override
	public long estimateSize() {
		return Long.MAX_VALUE;
	}

	@Override
	public long getExactSizeIfKnown() {
		return -1L;
	}

}
