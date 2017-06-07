package com.smijran.carriers;

/**
 * Created by kszalkowski on 2017-06-07.
 */
public interface IndexedTerminalOp< INDEX, VALUE, RESULT >
{
    < VALUE_INTERNAL, SPLITERATOR_VALUE > RESULT evaluate(
        IndexedReferencePipeline< INDEX, VALUE_INTERNAL, VALUE > pipeline,
        IndexedSpliterator< INDEX, SPLITERATOR_VALUE > valueSpliterator );
}
