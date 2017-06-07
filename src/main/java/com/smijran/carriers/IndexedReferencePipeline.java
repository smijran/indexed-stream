package com.smijran.carriers;

import java.util.Objects;
import java.util.stream.Collector;

/**
 * Created by kszalkowski on 2017-06-07.
 */
abstract class IndexedReferencePipeline<INDEX, VALUE_IN, VALUE_OUT>
        implements IndexedStream<INDEX, VALUE_OUT> {
    private IndexedSpliterator<INDEX, VALUE_OUT> spliterator;
    private boolean linkedOrConsumed = false;
    private int depth = 0;
    private IndexedReferencePipeline sourceStage;
    private IndexedReferencePipeline previousStage;
    private IndexedReferencePipeline nextStage;

    IndexedReferencePipeline(IndexedSpliterator<INDEX, VALUE_OUT> spliterator) {
        Objects.requireNonNull(spliterator);
        this.linkedOrConsumed = false;
        this.spliterator = spliterator;
        this.sourceStage = this;
        this.previousStage = null;
        this.nextStage = null;
    }

    IndexedReferencePipeline(IndexedReferencePipeline<INDEX, ?, ?> previousStage) {
        previousStage.nextStage = this;
        this.linkedOrConsumed = true;
        this.sourceStage = previousStage.sourceStage;
        this.previousStage = previousStage;
        this.depth = previousStage.depth + 1;
    }

    private <R> R evaluate(IndexedTerminalOp<INDEX, VALUE_OUT, R> terminalOp) {
        Objects.requireNonNull(terminalOp);
        linkedOrConsumed = true;
        return terminalOp.evaluate(this, spliterator());
    }

    private IndexedSpliterator<INDEX, ?> spliterator() {
        return sourceStage.spliterator;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <R> IndexedStream<INDEX, R> map(IndexedFunction<INDEX, VALUE_OUT, R> mapper) {
        Objects.requireNonNull(mapper);
        return new IndexedReferencePipeline.StatelessOp<INDEX, VALUE_OUT, R>(this) {
            @Override
            IndexedSink<INDEX, VALUE_OUT> opWrapSink(IndexedSink<INDEX, R> sink) {
                return new IndexedSink.IndexedChainedReference<INDEX, VALUE_OUT, R>(sink) {
                    @Override
                    public void accept(INDEX index, VALUE_OUT u) {
                        downstream.accept(index, mapper.apply(index, u));
                    }
                };
            }
        };
    }

    @Override
    public <R, A> R collect(Collector<? super VALUE_OUT, A, R> collector) {
        A container = evaluate(ReductionOps.makeRef(collector));
        return (R) container;
    }

    @Override
    public void close() {

    }

    <SPLITERATOR_VALUE, SINK extends IndexedSink<INDEX, VALUE_OUT>> SINK wrapAndCopyInto(
            SINK sink, IndexedSpliterator<INDEX, SPLITERATOR_VALUE> spliterator) {
        copyInto(wrapSink(Objects.requireNonNull(sink)), spliterator);
        return sink;
    }

    final <P_IN> IndexedSink<INDEX, P_IN> wrapSink(IndexedSink<INDEX, VALUE_OUT> sink) {
        Objects.requireNonNull(sink);

        for (@SuppressWarnings("rawtypes")
             IndexedReferencePipeline p = IndexedReferencePipeline.this; p.depth > 0; p = p.previousStage) {
            sink = p.opWrapSink(sink);
        }
        return (IndexedSink<INDEX, P_IN>) sink;
    }

    final <P_IN> void copyInto(IndexedSink<INDEX, P_IN> wrappedSink,
                               IndexedSpliterator<INDEX, P_IN> spliterator) {
        Objects.requireNonNull(wrappedSink);
        wrappedSink.begin(spliterator.getExactSizeIfKnown());
        spliterator.forEachRemaining(wrappedSink);
        wrappedSink.end();
    }

    abstract IndexedSink<INDEX, VALUE_IN> opWrapSink(IndexedSink<INDEX, VALUE_OUT> sink);

    static class Head<INDEX, VALUE_IN, VALUE_OUT>
            extends IndexedReferencePipeline<INDEX, VALUE_IN, VALUE_OUT> {

        Head(IndexedSpliterator<INDEX, VALUE_OUT> spliterator) {
            super(spliterator);
        }

        @Override
        IndexedSink<INDEX, VALUE_IN> opWrapSink(IndexedSink<INDEX, VALUE_OUT> sink) {
            throw new UnsupportedOperationException();
        }
    }

    abstract static class StatelessOp<INDEX, VALUE_IN, VALUE_OUT>
            extends IndexedReferencePipeline<INDEX, VALUE_IN, VALUE_OUT> {
        public StatelessOp(IndexedReferencePipeline<INDEX, ?, ?> upstream) {
            super(upstream);
        }
    }
}
