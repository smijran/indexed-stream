package com.smijran.carriers;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Created by kszalkowski on 2017-06-08.
 */
public class MatchOps
{
    private MatchOps()
    {
    }

    enum MatchKind
    {
        /** Do all elements match the predicate? */
        ANY( true, true ),

        /** Do any elements match the predicate? */
        ALL( false, false ),

        /** Do no elements match the predicate? */
        NONE( true, false );

        private final boolean stopOnPredicateMatches;
        private final boolean shortCircuitResult;

        private MatchKind( boolean stopOnPredicateMatches,
            boolean shortCircuitResult )
        {
            this.stopOnPredicateMatches = stopOnPredicateMatches;
            this.shortCircuitResult = shortCircuitResult;
        }
    }

    public static < INDEX, T > IndexedTerminalOp< INDEX, T, Boolean > makeRef(
        Predicate< ? super T > predicate,
        MatchOps.MatchKind matchKind )
    {
        Objects.requireNonNull( predicate );
        Objects.requireNonNull( matchKind );
        class MatchSink extends MatchOps.BooleanTerminalSink< INDEX, T >
        {
            MatchSink()
            {
                super( matchKind );
            }

            @Override
            public void accept( INDEX index, T t )
            {
                if( !stop && predicate.test( t ) == matchKind.stopOnPredicateMatches )
                {
                    stop = true;
                    value = matchKind.shortCircuitResult;
                }
            }
        }

        return new MatchOps.MatchOp<>( matchKind, MatchSink::new );
    }

    public static < INDEX, T > IndexedTerminalOp< INDEX, T, Boolean > makeRef(
        IndexedPredicate< INDEX, ? super T > predicate,
        MatchOps.MatchKind matchKind )
    {
        Objects.requireNonNull( predicate );
        Objects.requireNonNull( matchKind );
        class MatchSink extends MatchOps.BooleanTerminalSink< INDEX, T >
        {
            MatchSink()
            {
                super( matchKind );
            }

            @Override
            public void accept( INDEX index, T t )
            {
                if( !stop && predicate.test( index, t ) == matchKind.stopOnPredicateMatches )
                {
                    stop = true;
                    value = matchKind.shortCircuitResult;
                }
            }
        }

        return new MatchOps.MatchOp<>( matchKind, MatchSink::new );
    }

    private static final class MatchOp< INDEX, T > implements IndexedTerminalOp< INDEX, T, Boolean >
    {
        final MatchOps.MatchKind matchKind;
        final Supplier< MatchOps.BooleanTerminalSink< INDEX, T > > sinkSupplier;

        MatchOp(
            MatchOps.MatchKind matchKind,
            Supplier< MatchOps.BooleanTerminalSink< INDEX, T > > sinkSupplier )
        {
            this.matchKind = matchKind;
            this.sinkSupplier = sinkSupplier;
        }

        @Override
        public < VALUE_INTERNAL, SPLITERATOR_VALUE > Boolean evaluate(
            IndexedReferencePipeline< INDEX, VALUE_INTERNAL, T > pipeline,
            IndexedSpliterator< INDEX, SPLITERATOR_VALUE > valueSpliterator )
        {
            return pipeline.wrapAndCopyInto( sinkSupplier.get(), valueSpliterator ).getAndClearState();
        }
    }

    /**
     * Boolean specific terminal sink to avoid the boxing costs when returning results. Subclasses implement
     * the shape-specific functionality.
     *
     * @param <T>
     *            The output type of the stream pipeline
     */
    private static abstract class BooleanTerminalSink< INDEX, T > implements IndexedSink< INDEX, T >
    {
        boolean stop;
        boolean value;

        BooleanTerminalSink( MatchOps.MatchKind matchKind )
        {
            value = !matchKind.shortCircuitResult;
        }

        public boolean getAndClearState()
        {
            return value;
        }

        @Override
        public boolean cancellationRequested()
        {
            return stop;
        }
    }
}
