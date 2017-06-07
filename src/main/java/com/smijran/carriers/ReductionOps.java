package com.smijran.carriers;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Supplier;
import java.util.stream.Collector;

class ReductionOps
{

    private interface AccumulatingSink< INDEX, VALUE, RESULT, SINK extends AccumulatingSink< INDEX, VALUE, RESULT, SINK > >
        extends IndexedTerminalSink< INDEX, VALUE, RESULT >
    {
        void combine( SINK other );
    }

    /**
     * State box for a single state element, used as a base class for {@code AccumulatingSink} instances
     *
     * @param <U>
     *            The type of the state element
     */
    private static abstract class Box< U >
    {
        U state;

        Box()
        {
        } // Avoid creation of special accessor

        public U get()
        {
            return state;
        }
    }

    private static abstract class ReduceOp< INDEX, VALUE, RESULT, SINK extends AccumulatingSink< INDEX, VALUE, RESULT, SINK > >
        implements IndexedTerminalOp< INDEX, VALUE, RESULT >
    {

        public abstract SINK makeSink();

        @Override
        public <VALUE_INTERNAL, SPLITERATOR_VALUE> RESULT evaluate( IndexedReferencePipeline< INDEX, VALUE_INTERNAL, VALUE > helper,
            IndexedSpliterator< INDEX, SPLITERATOR_VALUE > spliterator )
        {
            return helper.wrapAndCopyInto( makeSink(), spliterator ).get();
        }

    }

    public static < INDEX, VALUE, RESULT > IndexedTerminalOp< INDEX, VALUE, RESULT > makeRef(
        Collector< ? super VALUE, RESULT, ? > collector )
    {
        Supplier< RESULT > supplier = Objects.requireNonNull( collector ).supplier();
        BiConsumer< RESULT, ? super VALUE > accumulator = collector.accumulator();
        BinaryOperator< RESULT > combiner = collector.combiner();
        class ReducingSink extends Box< RESULT >
            implements AccumulatingSink< INDEX, VALUE, RESULT, ReducingSink >
        {
            @Override
            public void begin( long size )
            {
                state = supplier.get();
            }

            @Override
            public void accept( INDEX index, VALUE t )
            {
                accumulator.accept( state, t );
            }

            @Override
            public void combine( ReducingSink other )
            {
                state = combiner.apply( state, other.state );
            }
        }
        return new ReduceOp< INDEX, VALUE, RESULT, ReducingSink >( )
        {
            @Override
            public ReducingSink makeSink()
            {
                return new ReducingSink();
            }
        };
    }
}
