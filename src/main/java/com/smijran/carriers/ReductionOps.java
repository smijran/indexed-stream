package com.smijran.carriers;

import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
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
        public < VALUE_INTERNAL, SPLITERATOR_VALUE > RESULT evaluate(
            IndexedReferencePipeline< INDEX, VALUE_INTERNAL, VALUE > helper,
            IndexedSpliterator< INDEX, SPLITERATOR_VALUE > spliterator )
        {
            return helper.wrapAndCopyInto( makeSink(), spliterator ).get();
        }

    }

    public static < INDEX, VALUE, RESULT > IndexedTerminalOp< INDEX, VALUE, RESULT > makeRef( RESULT seed,
        BiFunction< RESULT, ? super VALUE, RESULT > reducer, BinaryOperator< RESULT > combiner )
    {
        Objects.requireNonNull( reducer );
        Objects.requireNonNull( combiner );
        class ReducingSink extends Box< RESULT >
            implements AccumulatingSink< INDEX, VALUE, RESULT, ReducingSink >
        {
            @Override
            public void begin( long size )
            {
                state = seed;
            }

            @Override
            public void accept( INDEX index, VALUE value )
            {
                state = reducer.apply( state, value );
            }

            @Override
            public void combine( ReducingSink other )
            {
                state = combiner.apply( state, other.state );
            }
        }
        return new ReduceOp< INDEX, VALUE, RESULT, ReducingSink >()
        {
            @Override
            public ReducingSink makeSink()
            {
                return new ReducingSink();
            }
        };
    }

    public static < INDEX, VALUE, RESULT > IndexedTerminalOp< INDEX, VALUE, RESULT > makeRef(
        Supplier< RESULT > seedFactory,
        BiConsumer< RESULT, ? super VALUE > accumulator,
        BiConsumer< RESULT, RESULT > reducer )
    {
        Objects.requireNonNull( seedFactory );
        Objects.requireNonNull( accumulator );
        Objects.requireNonNull( reducer );
        class ReducingSink extends Box< RESULT >
            implements AccumulatingSink< INDEX, VALUE, RESULT, ReducingSink >
        {
            @Override
            public void begin( long size )
            {
                state = seedFactory.get();
            }

            @Override
            public void accept( INDEX index, VALUE value )
            {
                accumulator.accept( state, value );
            }

            @Override
            public void combine( ReducingSink other )
            {
                reducer.accept( state, other.state );
            }
        }
        return new ReduceOp< INDEX, VALUE, RESULT, ReducingSink >()
        {
            @Override
            public ReducingSink makeSink()
            {
                return new ReducingSink();
            }
        };
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
        return new ReduceOp< INDEX, VALUE, RESULT, ReducingSink >()
        {
            @Override
            public ReducingSink makeSink()
            {
                return new ReducingSink();
            }
        };
    }

    public static < INDEX, T > IndexedTerminalOp< INDEX, T, Optional< T > > makeRef(
        BinaryOperator< T > operator )
    {
        Objects.requireNonNull( operator );
        class ReducingSink
            implements AccumulatingSink< INDEX, T, Optional< T >, ReducingSink >
        {
            private boolean empty;
            private T state;

            public void begin( long size )
            {
                empty = true;
                state = null;
            }

            @Override
            public void accept( INDEX index, T t )
            {
                if( empty )
                {
                    empty = false;
                    state = t;
                }
                else
                {
                    state = operator.apply( state, t );
                }
            }

            @Override
            public Optional< T > get()
            {
                return empty ? Optional.empty() : Optional.of( state );
            }

            @Override
            public void combine( ReducingSink other )
            {
                if( !other.empty )
                    accept( null, other.state );
            }
        }
        return new ReduceOp< INDEX, T, Optional< T >, ReducingSink >()
        {
            @Override
            public ReducingSink makeSink()
            {
                return new ReducingSink();
            }
        };
    }
}
