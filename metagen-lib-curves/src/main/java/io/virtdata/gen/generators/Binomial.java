package io.virtdata.gen.generators;

import io.virtdata.gen.internal.Murmur3Hash;
import io.virtdata.gen.internal.BinomialAdapter;

import java.util.function.LongUnaryOperator;

public class Binomial implements LongUnaryOperator {

    private ThreadLocal<BinomialAdapter> binomialAdapter;
    private Murmur3Hash m3h = new Murmur3Hash();

    public Binomial(String tries, String probability) {
        this(Integer.valueOf(tries), Double.valueOf(probability));
    }

    public Binomial(int tries, double probability) {
        binomialAdapter = new ThreadLocalBinomialAdapter(tries,probability);
    }

    @Override
    public long applyAsLong(long operand) {
        long hashed = m3h.applyAsLong(operand);
        long value = binomialAdapter.get().applyAsLong(hashed);
        return value;
    }

    private static class ThreadLocalBinomialAdapter extends ThreadLocal<BinomialAdapter> {
        private int tries;
        private double probability;

        public ThreadLocalBinomialAdapter(int tries, double probability) {
            this.tries = tries;
            this.probability = probability;
        }

        @Override
        protected BinomialAdapter initialValue() {
            return new BinomialAdapter(tries,probability);
        }
    }
}
