package com.smijran.carriers;

import java.util.function.Supplier;

/**
 * Created by kszalkowski on 2017-06-07.
 */
public interface IndexedTerminalSink< INDEX, VALUE, RESULT >
    extends IndexedSink< INDEX, VALUE >, Supplier< RESULT >
{}
