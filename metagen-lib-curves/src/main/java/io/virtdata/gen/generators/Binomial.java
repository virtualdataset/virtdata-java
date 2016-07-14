package io.virtdata.gen.generators;

import io.virtdata.gen.internal.Murmur3Hash;
import io.virtdata.gen.internal.BinomialAdapter;

import java.util.function.LongUnaryOperator;

public class Binomial implements LongUnaryOperator {

    private final BinomialAdapter binomialAdapter;
    private Murmur3Hash m3h = new Murmur3Hash();

    public Binomial(String tries, String probability) {
        this(Integer.valueOf(tries), Double.valueOf(probability));
    }

    public Binomial(int tries, double probability) {
        binomialAdapter = new BinomialAdapter(tries, probability);
    }

    @Override
    public long applyAsLong(long operand) {
        long hashed = m3h.applyAsLong(operand);
        long value = binomialAdapter.applyAsLong(hashed);
        return value;
    }
}
