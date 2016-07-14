package io.virtdata.gen.generators;

import io.virtdata.gen.internal.LogNormalAdapter;
import io.virtdata.gen.internal.Murmur3Hash;

import java.util.function.LongFunction;

public class LogNormal implements LongFunction<Double> {

    private ThreadLocalLogNormalAdapter logNormalAdapter;
    private Murmur3Hash m3h = new Murmur3Hash();

    public LogNormal(String mean, String stddev) {
        this(Double.valueOf(mean), Double.valueOf(stddev));
    }

    public LogNormal(double mean, double stddev) {
        logNormalAdapter = new ThreadLocalLogNormalAdapter(mean, stddev);
    }

    @Override
    public Double apply(long operand) {
        long hashed = m3h.applyAsLong(operand);
        double value = logNormalAdapter.get().apply(hashed);
        return value;
    }

    private static class ThreadLocalLogNormalAdapter extends ThreadLocal<LogNormalAdapter> {
        private double mean;
        private double stddev;

        public ThreadLocalLogNormalAdapter(double mean, double stddev) {
            this.mean = mean;
            this.stddev = stddev;
        }

        @Override
        protected LogNormalAdapter initialValue() {
            return new LogNormalAdapter(mean, stddev);
        }
    }
}
