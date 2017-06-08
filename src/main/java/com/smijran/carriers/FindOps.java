package com.smijran.carriers;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Created by kszalkowski on 2017-06-08.
 */
public class FindOps
{

    /**
     * Constructs a {@code TerminalOp} for streams of objects.
     *
     * @param <T>
     *            the type of elements of the stream
     * @param mustFindFirst
     *            whether the {@code TerminalOp} must produce the first element in the encounter order
     * @return a {@code TerminalOp} implementing the find operation
     */
    public static < INDEX, T > IndexedTerminalOp< INDEX, T, Optional< T > > makeRef( boolean mustFindFirst )
    {
        return new FindOps.FindOp<>( mustFindFirst, Optional.empty(),
            Optional::isPresent, FindSink.OfRef::new );
    }

    private static final class FindOp< INDEX, VALUE, OPT > implements IndexedTerminalOp< INDEX, VALUE, OPT >
    {
        final boolean mustFindFirst;
        final OPT emptyValue;
        final Predicate< OPT > presentPredicate;
        final Supplier< IndexedTerminalSink< INDEX, VALUE, OPT > > sinkSupplier;

        FindOp( boolean mustFindFirst,
            OPT emptyValue,
            Predicate< OPT > presentPredicate,
            Supplier< IndexedTerminalSink< INDEX, VALUE, OPT > > sinkSupplier )
        {
            this.mustFindFirst = mustFindFirst;
            this.emptyValue = emptyValue;
            this.presentPredicate = presentPredicate;
            this.sinkSupplier = sinkSupplier;
        }

        @Override
        public < VALUE_INTERNAL, SPLITERATOR_VALUE > OPT evaluate(
            IndexedReferencePipeline< INDEX, VALUE_INTERNAL, VALUE > helper,
            IndexedSpliterator< INDEX, SPLITERATOR_VALUE > valueSpliterator )
        {
            {
                OPT result = helper.wrapAndCopyInto( sinkSupplier.get(), valueSpliterator ).get();
                return result != null ? result : emptyValue;
            }
        }
    }

    private static abstract class FindSink< INDEX, VALUE, OPT >
        implements IndexedTerminalSink< INDEX, VALUE, OPT >
    {
        boolean hasValue;
        VALUE value;

        FindSink()
        {
        } // Avoid creation of special accessor

        @Override
        public void accept( INDEX index, VALUE value )
        {
            if( !hasValue )
            {
                hasValue = true;
                this.value = value;
            }
        }

        @Override
        public boolean cancellationRequested()
        {
            return hasValue;
        }

        /** Specialization of {@code FindSink} for reference streams */
        static final class OfRef< INDEX, VALUE >extends FindSink< INDEX, VALUE, Optional< VALUE > >
        {
            @Override
            public Optional< VALUE > get()
            {
                return hasValue ? Optional.of( value ) : null;
            }
        }
    }

}
