package com.smijran.carriers;

import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;
import java.util.function.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Created by kszalkowski on 2017-06-07.
 */
abstract class IndexedReferencePipeline< INDEX, VALUE_IN, VALUE_OUT >
    implements IndexedStream< INDEX, VALUE_OUT >
{
    private IndexedSpliterator< INDEX, VALUE_OUT > spliterator;
    private boolean linkedOrConsumed = false;
    private int depth = 0;
    private IndexedReferencePipeline sourceStage;
    private IndexedReferencePipeline previousStage;
    private IndexedReferencePipeline nextStage;

    IndexedReferencePipeline( IndexedSpliterator< INDEX, VALUE_OUT > spliterator )
    {
        Objects.requireNonNull( spliterator );
        this.linkedOrConsumed = false;
        this.spliterator = spliterator;
        this.sourceStage = this;
        this.previousStage = null;
        this.nextStage = null;
    }

    IndexedReferencePipeline( IndexedReferencePipeline< INDEX, ?, ? > previousStage )
    {
        previousStage.nextStage = this;
        this.linkedOrConsumed = true;
        this.sourceStage = previousStage.sourceStage;
        this.previousStage = previousStage;
        this.depth = previousStage.depth + 1;
    }

    private < R > R evaluate( IndexedTerminalOp< INDEX, VALUE_OUT, R > terminalOp )
    {
        Objects.requireNonNull( terminalOp );
        linkedOrConsumed = true;
        return terminalOp.evaluate( this, spliterator() );
    }

    private IndexedSpliterator< INDEX, ? > spliterator()
    {
        return sourceStage.spliterator;
    }

    @Override
    public IndexedStream< INDEX, VALUE_OUT > filter(
        IndexedPredicate< INDEX, ? super VALUE_OUT > valuePredicate )
    {
        Objects.requireNonNull( valuePredicate );
        return new IndexedReferencePipeline.StatelessOp< INDEX, VALUE_OUT, VALUE_OUT >( this )
        {
            @Override
            IndexedSink< INDEX, VALUE_OUT > opWrapSink( IndexedSink< INDEX, VALUE_OUT > sink )
            {
                return new IndexedSink.IndexedChainedReference< INDEX, VALUE_OUT, VALUE_OUT >( sink )
                {
                    @Override
                    public void begin( long size )
                    {
                        downstream.begin( -1 );
                    }

                    @Override
                    public void accept( INDEX index, VALUE_OUT u )
                    {
                        if( valuePredicate.test( index, u ) )
                            downstream.accept( index, u );
                    }
                };
            }
        };
    }

    @Override
    public IndexedStream< INDEX, VALUE_OUT > filter( Predicate< ? super VALUE_OUT > valuePredicate )
    {
        Objects.requireNonNull( valuePredicate );
        return new IndexedReferencePipeline.StatelessOp< INDEX, VALUE_OUT, VALUE_OUT >( this )
        {
            @Override
            IndexedSink< INDEX, VALUE_OUT > opWrapSink( IndexedSink< INDEX, VALUE_OUT > sink )
            {
                return new IndexedSink.IndexedChainedReference< INDEX, VALUE_OUT, VALUE_OUT >( sink )
                {
                    @Override
                    public void begin( long size )
                    {
                        downstream.begin( -1 );
                    }

                    @Override
                    public void accept( INDEX index, VALUE_OUT u )
                    {
                        if( valuePredicate.test( u ) )
                            downstream.accept( index, u );
                    }
                };
            }
        };
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public < R > IndexedStream< INDEX, R > map( Function< ? super VALUE_OUT, ? extends R > mapper )
    {
        Objects.requireNonNull( mapper );
        return new IndexedReferencePipeline.StatelessOp< INDEX, VALUE_OUT, R >( this )
        {
            @Override
            IndexedSink< INDEX, VALUE_OUT > opWrapSink( IndexedSink< INDEX, R > sink )
            {
                return new IndexedSink.IndexedChainedReference< INDEX, VALUE_OUT, R >( sink )
                {
                    @Override
                    public void accept( INDEX index, VALUE_OUT u )
                    {
                        downstream.accept( index, mapper.apply( u ) );
                    }
                };
            }
        };
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public < R > IndexedStream< INDEX, R > map(
        IndexedFunction< INDEX, ? super VALUE_OUT, ? extends R > mapper )
    {
        Objects.requireNonNull( mapper );
        return new IndexedReferencePipeline.StatelessOp< INDEX, VALUE_OUT, R >( this )
        {
            @Override
            IndexedSink< INDEX, VALUE_OUT > opWrapSink( IndexedSink< INDEX, R > sink )
            {
                return new IndexedSink.IndexedChainedReference< INDEX, VALUE_OUT, R >( sink )
                {
                    @Override
                    public void accept( INDEX index, VALUE_OUT u )
                    {
                        downstream.accept( index, mapper.apply( index, u ) );
                    }
                };
            }
        };
    }

    @Override
    public < R, A > R collect( Collector< ? super VALUE_OUT, A, R > collector )
    {
        A container = evaluate( ReductionOps.makeRef( collector ) );
        return (R)container;
    }

    @Override
    public final < R > R collect( Supplier< R > supplier,
        BiConsumer< R, ? super VALUE_OUT > accumulator,
        BiConsumer< R, R > combiner )
    {
        return evaluate( ReductionOps.makeRef( supplier, accumulator, combiner ) );
    }

    @Override
    public VALUE_OUT reduce( VALUE_OUT identity, BinaryOperator< VALUE_OUT > accumulator )
    {
        return evaluate( ReductionOps.makeRef( identity, accumulator, accumulator ) );
    }

    @Override
    public Optional< VALUE_OUT > reduce( BinaryOperator< VALUE_OUT > accumulator )
    {
        return evaluate( ReductionOps.makeRef( accumulator ) );
    }

    @Override
    public < R > R reduce( R identity, BiFunction< R, ? super VALUE_OUT, R > accumulator,
        BinaryOperator< R > combiner )
    {
        return evaluate( ReductionOps.makeRef( identity, accumulator, combiner ) );
    }

    @Override
    public final Optional< VALUE_OUT > max( Comparator< ? super VALUE_OUT > comparator )
    {
        return reduce( BinaryOperator.maxBy( comparator ) );
    }

    @Override
    public final Optional< VALUE_OUT > min( Comparator< ? super VALUE_OUT > comparator )
    {
        return reduce( BinaryOperator.minBy( comparator ) );
    }

    @Override
    public long count()
    {
        return collect( Collectors.toSet() ).size();
    }

    @Override
    public final Optional< VALUE_OUT > findFirst()
    {
        return evaluate( FindOps.makeRef( true ) );
    }

    @Override
    public final Optional< VALUE_OUT > findAny()
    {
        return evaluate( FindOps.makeRef( false ) );
    }

    @Override
    public void forEach( Consumer< ? super VALUE_OUT > consumer )
    {
        evaluate( ForEachOps.makeRef( consumer ) );
    }

    @Override
    public void forEach( IndexedConsumer< INDEX, ? super VALUE_OUT > consumer )
    {
        evaluate( ForEachOps.makeRef( consumer ) );
    }

    @Override
    public void close()
    {

    }

    < SPLITERATOR_VALUE, SINK extends IndexedSink< INDEX, VALUE_OUT > > SINK wrapAndCopyInto(
        SINK sink, IndexedSpliterator< INDEX, SPLITERATOR_VALUE > spliterator )
    {
        copyInto( wrapSink( Objects.requireNonNull( sink ) ), spliterator );
        return sink;
    }

    final < P_IN > IndexedSink< INDEX, P_IN > wrapSink( IndexedSink< INDEX, VALUE_OUT > sink )
    {
        Objects.requireNonNull( sink );

        for( @SuppressWarnings( "rawtypes" )
        IndexedReferencePipeline p = IndexedReferencePipeline.this; p.depth > 0; p = p.previousStage )
        {
            sink = p.opWrapSink( sink );
        }
        return (IndexedSink< INDEX, P_IN >)sink;
    }

    final < P_IN > void copyInto( IndexedSink< INDEX, P_IN > wrappedSink,
        IndexedSpliterator< INDEX, P_IN > spliterator )
    {
        Objects.requireNonNull( wrappedSink );
        wrappedSink.begin( spliterator.getExactSizeIfKnown() );
        spliterator.forEachRemaining( wrappedSink );
        wrappedSink.end();
    }

    abstract IndexedSink< INDEX, VALUE_IN > opWrapSink( IndexedSink< INDEX, VALUE_OUT > sink );

    static class Head< INDEX, VALUE_IN, VALUE_OUT >
        extends IndexedReferencePipeline< INDEX, VALUE_IN, VALUE_OUT >
    {

        Head( IndexedSpliterator< INDEX, VALUE_OUT > spliterator )
        {
            super( spliterator );
        }

        @Override
        IndexedSink< INDEX, VALUE_IN > opWrapSink( IndexedSink< INDEX, VALUE_OUT > sink )
        {
            throw new UnsupportedOperationException();
        }
    }

    abstract static class StatelessOp< INDEX, VALUE_IN, VALUE_OUT >
        extends IndexedReferencePipeline< INDEX, VALUE_IN, VALUE_OUT >
    {
        public StatelessOp( IndexedReferencePipeline< INDEX, ?, ? > upstream )
        {
            super( upstream );
        }
    }
}
