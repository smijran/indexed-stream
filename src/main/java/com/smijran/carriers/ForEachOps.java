package com.smijran.carriers;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * Created by kszalkowski on 2017-06-08.
 */
public class ForEachOps
{

    private ForEachOps()
    {
    }

    public static < INDEX, VALUE > IndexedTerminalOp< INDEX, VALUE, Void > makeRef(
        Consumer< ? super VALUE > action )
    {
        Objects.requireNonNull( action );
        return new ForEachOp.OfRef<>( action );
    }

    public static < INDEX, VALUE > IndexedTerminalOp< INDEX, VALUE, Void > makeRef(
        IndexedConsumer< INDEX, ? super VALUE > action )
    {
        Objects.requireNonNull( action );
        return new ForEachOp.OfRef<>( action );
    }

    static abstract class ForEachOp< INDEX, VALUE >
        implements IndexedTerminalOp< INDEX, VALUE, Void >, IndexedTerminalSink< INDEX, VALUE, Void >
    {

        @Override
        public < VALUE_INTERNAL, SPLITERATOR_VALUE > Void evaluate(
            IndexedReferencePipeline< INDEX, VALUE_INTERNAL, VALUE > helper,
            IndexedSpliterator< INDEX, SPLITERATOR_VALUE > spliterator )
        {
            return helper.wrapAndCopyInto( this, spliterator ).get();
        }

        // TerminalSink

        @Override
        public Void get()
        {
            return null;
        }

        // Implementations

        /**
         * Implementation class for reference streams
         */
        static final class OfRef< INDEX, VALUE >extends ForEachOps.ForEachOp< INDEX, VALUE >
        {
            final IndexedConsumer< INDEX, ? super VALUE > indexedConsumer;
            final Consumer< ? super VALUE > consumer;

            OfRef( IndexedConsumer< INDEX, ? super VALUE > consumer )
            {
                this.indexedConsumer = consumer;
                this.consumer = null;
            }

            OfRef( Consumer< ? super VALUE > consumer )
            {
                this.indexedConsumer = null;
                this.consumer = consumer;
            }

            @Override
            public void accept( INDEX index, VALUE value )
            {
                if( indexedConsumer != null )
                {
                    indexedConsumer.accept( index, value );
                }
                else if( consumer != null )
                {
                    consumer.accept( value );
                }
            }
        }
    }
}
