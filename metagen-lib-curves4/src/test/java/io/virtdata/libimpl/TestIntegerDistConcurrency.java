package io.virtdata.libimpl;

import io.virtdata.libimpl.discrete.IntegerDistributions;
import org.apache.commons.math4.distribution.BinomialDistribution;
import org.assertj.core.data.Offset;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.LongUnaryOperator;

import static org.assertj.core.api.Assertions.assertThat;

public class TestIntegerDistConcurrency {

    @Test
    public void testBinomialICDR() {
        Offset<Double> offset = Offset.offset(0.00001d);
        BinomialDistribution distribution = new BinomialDistribution(8, 0.5);
        assertThat(distribution.probability(0)).isCloseTo(0.00390d, offset);
        assertThat(distribution.probability(1)).isCloseTo(0.03125d, offset);
        assertThat(distribution.probability(2)).isCloseTo(0.10937d, offset);
        assertThat(distribution.probability(3)).isCloseTo(0.21875d, offset);
        assertThat(distribution.probability(4)).isCloseTo(0.27343d, offset);
        assertThat(distribution.probability(5)).isCloseTo(0.21875d, offset);
        assertThat(distribution.probability(6)).isCloseTo(0.10937d, offset);
        assertThat(distribution.probability(7)).isCloseTo(0.03125d, offset);
        assertThat(distribution.probability(8)).isCloseTo(0.00390d, offset);
    }

    @Test
    public void testBinomialCurvePoints() {
        LongUnaryOperator idc = IntegerDistributions.forSpec("mapto_binomial(8,0.5)");

        long half = Long.MAX_VALUE / 2;
        long expected = idc.applyAsLong(half);
        assertThat(expected).isEqualTo(4);
        expected = idc.applyAsLong(1);
        assertThat(expected).isEqualTo(0);

        // threshold test against CDF
        expected = idc.applyAsLong((long) (0.03515d * (double) Long.MAX_VALUE));
        assertThat(expected).isEqualTo(1);
        expected = idc.applyAsLong((long) (0.03600d * (double) Long.MAX_VALUE));
        assertThat(expected).isEqualTo(2);
    }

    @Test
    public void testConcurrentBinomialHashValues() {
        testConcurrentIntegerHashDistValues(
                "binomial(8,0.5)/100 threads/1000 iterations",
                100,
                1000,
                "binomial(8,0.5)");
    }

    private void testConcurrentIntegerHashDistValues(
            String description,
            int threads,
            int iterations,
            String mapperSpec) {

        LongUnaryOperator mapper = IntegerDistributions.forSpec(mapperSpec);
        long[] values = new long[iterations];
        for (int index = 0; index < iterations; index++) {
            values[index] = mapper.applyAsLong(index);
        }

        ExecutorService pool = Executors.newFixedThreadPool(threads);

        List<Future<long[]>> futures = new ArrayList<>();
        for (int t = 0; t < threads; t++) {
            futures.add(pool.submit(new IntegerDistributionCallable(t, iterations, mapperSpec, pool)));
        }
        try {
            Thread.sleep(1000);
            synchronized (pool) {
                pool.notifyAll();
            }
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }

        long[][] results = new long[threads][iterations];

        for (int i = 0; i < futures.size(); i++) {
            try {
                results[i] = futures.get(i).get();
                System.out.println(description + ": got results for thread " + i);
                System.out.flush();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        pool.shutdown();

        for (int vthread = 0; vthread < threads; vthread++) {
            assertThat(results[vthread]).isEqualTo(values);
            System.out.println(description + ": verified values for thread " + vthread);
        }


    }

    private static class IntegerDistributionCallable implements Callable<long[]> {

        private final Object signal;
        private final int slot;
        private final String mapperSpec;
        private int size;

        public IntegerDistributionCallable(int slot, int size, String mapperSpec, Object signal) {
            this.slot = slot;
            this.size = size;
            this.mapperSpec = mapperSpec;
            this.signal = signal;
        }

        @Override
        public long[] call() throws Exception {
            long[] output = new long[size];
            LongUnaryOperator mapper = IntegerDistributions.forSpec(mapperSpec);
//            System.out.println("resolved:" + mapper);
//            System.out.flush();

            synchronized (signal) {
                signal.wait(10000);
            }

            for (int i = 0; i < output.length; i++) {
                output[i] = mapper.applyAsLong(i);
//                if ((i % 100) == 0) {
//                    System.out.println("wrote t:" + slot + ", iter:" + i + ", val:" + output[i]);
//                }
            }
            return output;
        }
    }

}