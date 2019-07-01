package io.virtdata.libbasics.shared.from_long.to_object;

import io.virtdata.annotations.Categories;
import io.virtdata.annotations.Category;
import io.virtdata.annotations.Example;
import io.virtdata.annotations.ThreadSafeMapper;
import io.virtdata.libbasics.shared.from_long.to_double.HashedDoubleRange;

import java.util.function.Function;
import java.util.function.LongFunction;
import java.util.function.LongUnaryOperator;

/**
 * This is a higher-order function which takes an input value,
 * and flips a coin. The first parameter is used as the threshold
 * for choosing a function. If the sample values derived from the
 * input is lower than the threshold value, then the first following
 * function is used, and otherwise the second is used.
 *
 * For example, if the threshold is 0.23, and the input value is
 * hashed and sampled in the unit interval to 0.43, then the second
 * of the two provided functions will be used.
 *
 * The input value does not need to be hashed beforehand, since the
 * user may need to use the full input value before hashing as the
 * input to one or both of the functions.
 *
 * This function will accept either a LongFunction or a {@link Function}
 * or a LongUnaryOperator in either position. If necessary, use
 * {@link java.util.function.ToLongFunction} to adapt other function forms to be
 * compatible with these signatures.
 */

@Categories(Category.distributions)
@ThreadSafeMapper
public class CoinFunc implements Function<Long,Object> {

    private final double threshold;
    private final LongFunction<? extends Object> first;
    private final LongFunction<? extends Object> second;
    private final HashedDoubleRange cointoss = new HashedDoubleRange(0.0d,1.0d);


    @Example({"CoinFunc(0.15,NumberNameToString(),Combinations('A:1:B:23'))","use the first function 15% of the time"})
    public CoinFunc(double threshold, LongFunction<? extends Object> first, LongFunction<? extends Object> second) {
        this.threshold = threshold;
        this.first = first;
        this.second = second;
    }

    public CoinFunc(double threshold, LongFunction<? extends Object> first, Function<Long,? extends Object> second) {
        this.threshold = threshold;
        this.first = first;
        this.second = second::apply;
    }

    public CoinFunc(double threshold, LongFunction<? extends Object> first, LongUnaryOperator second) {
        this.threshold = threshold;
        this.first = first::apply;
        this.second = second::applyAsLong;
    }



    public CoinFunc(double threshold, Function<Long,? extends Object> first, Function<Long,? extends Object> second) {
        this.threshold = threshold;
        this.first = first::apply;
        this.second = second::apply;
    }

    public CoinFunc(double threshold, Function<Long,? extends Object> first, LongFunction<? extends Object> second) {
        this.threshold = threshold;
        this.first = first::apply;
        this.second = second;
    }

    public CoinFunc(double threshold, Function<Long,? extends Object> first, LongUnaryOperator second) {
        this.threshold = threshold;
        this.first = first::apply;
        this.second = second::applyAsLong;
    }



    public CoinFunc(double threshold, LongUnaryOperator first, Function<Long,? extends Object> second) {
        this.threshold = threshold;
        this.first = first::applyAsLong;
        this.second = second::apply;
    }
    public CoinFunc(double threshold, LongUnaryOperator first, LongFunction<? extends Object> second) {
        this.threshold = threshold;
        this.first = first::applyAsLong;
        this.second = second::apply;
    }

    public CoinFunc(double threshold, LongUnaryOperator first, LongUnaryOperator second) {
        this.threshold = threshold;
        this.first = first::applyAsLong;
        this.second = second::applyAsLong;
    }

    @Override
    public Object apply(Long aLong) {
        double unfaircoin = cointoss.applyAsDouble(aLong);
        Object result = (unfaircoin < threshold) ? first.apply(aLong) : second.apply(aLong);
        return result;
    }

}
