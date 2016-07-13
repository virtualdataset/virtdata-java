package io.virtdata.libraryimpl;

import io.virtdata.api.Generator;
import io.virtdata.core.GeneratorFunctionMapper;

import java.util.function.Function;
import java.util.function.LongFunction;
import java.util.function.LongUnaryOperator;

public class FunctionAssembler {

    LongUnaryOperator longlongfunction;
    LongFunction longRFunction;

    // ((LongFunction<Object>)longRFunction).apply(l)
    public FunctionAssembler() {
    }

    public FunctionAssembler andThen(Object functionObject) {
        if (functionObject instanceof LongUnaryOperator) {
            return andThen((LongUnaryOperator) functionObject);
        }
        if (functionObject instanceof LongFunction) {
            return andThen((LongFunction) functionObject);
        }
        if (functionObject instanceof Function) {
            return andThen((Function) functionObject);
        }
        throw new RuntimeException("Function object was not a type recognized by " + FunctionAssembler.class.getSimpleName());
    }

    /**
     * Valid for any number of calls from the beginning of assembly.
     *
     * @param andThen a LongUnaryOperator
     * @return this FunctionAssembler
     */
    public FunctionAssembler andThen(LongUnaryOperator andThen) {

        // TODO: add type checks or possible miswiring assertions here.

        if (longlongfunction == null) {
            longlongfunction = andThen;
        } else {
            longlongfunction = longlongfunction.andThen(andThen);
        }
        return this;
    }

    /**
     * Only valid after 0 or more LongUnaryOperators, but not after another LongFunction which isn't a LongFunction&lt;Long&gt;.
     * Without explicit type annotations (type erasure doesn't help you for late binding), we will assume that multiple LongFunctions
     * in sequence follow LongFunction&lt;Long&gt;s and cast or error if not.
     *
     * @param andThen a LongFunction
     * @return this FunctionAssembler
     */
    @SuppressWarnings("unchecked")
    public FunctionAssembler andThen(LongFunction andThen) {

        if (longRFunction == null) {
            if (longlongfunction==null) {
                // no previous functions, this one becomes the compose function
                longRFunction = andThen;
            } else {
                // We have an inner composed function for longs already, compose this around it.
                longRFunction = (long l) -> andThen.apply(longlongfunction.applyAsLong(l));
                longlongfunction=null; // reset this. The longRFunction is not the centerpiece.
            }
        } else {
            // We'll presume that the outer type of the inner function is already a Long, and try to coerce a lambda
            longRFunction = (long l) -> andThen.apply(((LongFunction<Long>)longRFunction).apply(l));
        }
        return this;
    }

    /**
     * <p>Allows for mapping generic functions into the composed lambda. This is not yet type safe, because type erasure.</p>
     * <p>If this is called after a LongFunction&lt;?&gt; has been added, then the types are presumed to match, and the
     * lamda is coerced via casting.</p>
     * @param andThen Function to add to the outer calling layer
     * @return this FunctionAssembler
     */
    @SuppressWarnings("unchecked")
    public FunctionAssembler andThen(Function andThen) {
        if (longRFunction==null) {
            longRFunction=new LongIdentity();
        }
        longRFunction = (long l) -> andThen.apply(((LongFunction<?>)longRFunction).apply(l));
        return this;
    }

    public LongFunction getFunction() {
        return longRFunction;
    }

    public <T> Generator<T> getGenerator() {
        return GeneratorFunctionMapper.map(longRFunction);
    }

    public static class LongIdentity implements LongFunction<Long> {
        @Override
        public Long apply(long input) {
            return input;
        }
    }


}
