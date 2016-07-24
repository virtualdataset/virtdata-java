package io.virtdata.libraryimpl;

import io.virtdata.api.Generator;
import io.virtdata.core.GeneratorFunctionMapper;

import java.util.function.Function;
import java.util.function.LongFunction;
import java.util.function.LongUnaryOperator;


public class FunctionAssembler {

    LongUnaryOperator longlongfunction;
    LongFunction<?> longRFunction;

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
        throw new RuntimeException("Function object was not a type recognized by " + FunctionAssembler.class.getSimpleName()
        + ", object:" + functionObject);
    }


    /**
     * Valid for any number of calls from the beginning of assembly.
     *
     * @param andThen a LongUnaryOperator
     * @return this FunctionAssemble
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
            longRFunction = andThen;
        } else {
            // We'll presume that the outer type of the inner function is already a Long, and try to coerce a lambda
            final LongFunction<Long> extant = (LongFunction<Long>) longRFunction;
            longRFunction = (long l) -> andThen.apply(extant.apply(l));
        }

        // prepend extant long operators and clear them
        if (longlongfunction != null) {
            final LongUnaryOperator luo = longlongfunction;
            longRFunction = (long l) -> andThen.apply(luo.applyAsLong(l));
            longlongfunction = null;
        }

        return this;
    }

    /**
     * <p>Allows for mapping generic functions into the composed lambda. This is not yet type safe, because type erasure.</p>
     * <p>If this is called after a LongFunction&lt;?&gt; has been added, then the types are presumed to match, and the
     * lamda is coerced via casting.</p>
     *
     * @param andThen Function to add to the outer calling layer
     * @return this FunctionAssembler
     */
    @SuppressWarnings("unchecked")
    public FunctionAssembler andThen(Function andThen) {
        if (longRFunction == null) {
            if (longlongfunction == null) {
                // If you provided nothing but Function<T,R>, then assume T==Long
                longRFunction = ((Function<Long, ?>) andThen)::apply;
            } else {
                // If you provided long unary operators and then a Function<T,R>, assume T==Long
                // else you'll get an exception
                longRFunction = (long l) -> ((Function<Long, ?>) andThen).apply(longlongfunction.applyAsLong(l));
            }
        } else {
            // attempt to wrap existing function with generic signature
            final LongFunction<?> extant = (LongFunction<?>) longRFunction;
            longRFunction = (long l) -> andThen.apply(extant.apply(l));
        }
        return this;
    }

    public LongFunction<?> getFunction() {
        if (longRFunction != null) {
            return longRFunction;
        }
        if (longlongfunction != null) {
            return (long l) -> longlongfunction.applyAsLong(l);
        }

        throw new RuntimeException("There are no functions added to this FunctionAssembler.");
    }

    @SuppressWarnings("unchecked")
    public <T> Generator<T> getGenerator() {
        return (Generator<T>) GeneratorFunctionMapper.map(getFunction());
    }


    public static class LongIdentity implements LongFunction<Long> {
        @Override
        public Long apply(long input) {
            return input;
        }
    }

}
