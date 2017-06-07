package com.smijran.carriers;

import java.util.stream.Collector;

/**
 * Created by kszalkowski on 2017-06-07.
 */
public class IndexedReferencePipeline< INDEX, VALUE_IN, VALUE_OUT >
    implements IndexedStream< INDEX, VALUE_OUT >
{

    public IndexedReferencePipeline( IndexedSpliterator< INDEX, VALUE_OUT > spliterator )
    {

    }

    @Override
    public < R > IndexedStream< INDEX, R > map( IndexedFunction< INDEX, VALUE_OUT, R > mapper )
    {
        return null;
    }

    @Override
    public < R, A > R collect( Collector< ? super VALUE_OUT, A, R > collector )
    {
        return null;
    }

    @Override
    public void close()
    {

    }
}
