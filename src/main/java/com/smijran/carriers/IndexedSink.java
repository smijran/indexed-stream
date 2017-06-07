package com.smijran.carriers;

import java.util.Objects;

interface IndexedSink< INDEX, VALUE >extends IndexedConsumer< INDEX, VALUE >
{
    default void begin( long size )
    {
    }

    default void end()
    {
    }

    default boolean cancellationRequested()
    {
        return false;
    }

    abstract class IndexedChainedReference< INDEX, VALUE, DOWNSTREAM_VALUE >
        implements IndexedSink< INDEX, VALUE >
    {
        final IndexedSink< INDEX, ? super DOWNSTREAM_VALUE > downstream;

        public IndexedChainedReference( IndexedSink< INDEX, ? super DOWNSTREAM_VALUE > downstream )
        {
            this.downstream = Objects.requireNonNull( downstream );
        }

        @Override
        public void begin( long size )
        {
            downstream.begin( size );
        }

        @Override
        public void end()
        {
            downstream.end();
        }

        @Override
        public boolean cancellationRequested()
        {
            return downstream.cancellationRequested();
        }
    }
}
