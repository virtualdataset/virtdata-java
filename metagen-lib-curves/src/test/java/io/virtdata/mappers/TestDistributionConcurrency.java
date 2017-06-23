package io.virtdata.mappers;

import io.virtdata.mappers.hashed_discrete.IDistHashedCoupler;
import io.virtdata.mappers.hashed_discrete.IDistHashedResolver;
import io.virtdata.mappers.mapped_discrete.IDistMappedCoupler;
import io.virtdata.mappers.mapped_discrete.IDistMappedResolver;
import org.apache.commons.math3.distribution.BinomialDistribution;
import org.apache.commons.math3.distribution.IntegerDistribution;
import org.apache.commons.math3.distribution.PascalDistribution;
import org.assertj.core.data.Offset;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.assertj.core.api.Assertions.assertThat;

public class TestDistributionConcurrency {

    @Test
    public void testBinomialICDR() {
        IDistMappedResolver tb = new IDistMappedResolver(BinomialDistribution.class.getCanonicalName(), "8", "0.5");
        IDistMappedCoupler idc = tb.resolve();
        IntegerDistribution distribution = idc.getDistribution();
        Offset<Double> offset = Offset.offset(0.00001d);

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
        IDistMappedResolver tb = new IDistMappedResolver(BinomialDistribution.class, "8", "0.5");
        IDistMappedCoupler idc = tb.resolve();

        long half = Long.MAX_VALUE / 2;
        Long expected = idc.applyAsLong(half);
        assertThat(expected).isEqualTo(4L);
        expected = idc.applyAsLong(1L);
        assertThat(expected).isEqualTo(0L);

        // threshold test against CDF
        expected = idc.applyAsLong((long) (0.03515d * (double) Long.MAX_VALUE));
        assertThat(expected).isEqualTo(1);
        expected = idc.applyAsLong((long) (0.03600d * (double) Long.MAX_VALUE));
        assertThat(expected).isEqualTo(2);
    }

    @Test(enabled = false)
    public void testBinomialValues() {
        IDistHashedResolver tb = new IDistHashedResolver(BinomialDistribution.class, "8", "0.5");
        IDistHashedCoupler mapper = tb.resolve();
        int iterations = 1000;
        long[] values = new long[iterations];
        for (int index = 0; index < iterations; index++) {
            values[index] = mapper.applyAsLong(index);
        }
        System.out.println(Arrays.toString(values));
    }

    @Test
    public void testConcurrentBinomialHashValues() {
//        testConcurrentIntegerHashDistValues("binomial.t100.i1000", 100, 1000,
//                new String[]{BinomialDistribution.class.getCanonicalName(), "8", "0.5"});
        new ArrayList<String[]>() {{
            add(new String[]{PascalDistribution.class.getCanonicalName(), "5", "0.7"});
        }}.forEach(td -> {
            System.out.println("Testing concurrency isolation for " + td[0]);
            testConcurrentIntegerHashDistValues(td[0],100, 1000, td);
        });
    }

    private void testConcurrentIntegerHashDistValues(String description, int threads, int iterations, String[] ctor) {

        IDistHashedResolver tb = new IDistHashedResolver(ctor);
        IDistHashedCoupler mapper = tb.resolve();
        long[] values = new long[iterations];
        for (int index = 0; index < iterations; index++) {
            values[index] = mapper.applyAsLong(index);
        }

        ExecutorService pool = Executors.newFixedThreadPool(threads);

        List<Future<long[]>> futures = new ArrayList<>();
        for (int t = 0; t < threads; t++) {
            futures.add(pool.submit(new IntegerDistributionCallable(t, iterations, tb, pool)));
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
            System.out.println(description +": verified values for thread " + vthread);
        }


    }

    private static class IntegerDistributionCallable implements Callable<long[]> {

        private final Object signal;
        private final int slot;
        private int size;
        private IDistHashedResolver resolver;

        public IntegerDistributionCallable(int slot, int size, IDistHashedResolver resolver, Object signal) {
            this.slot = slot;
            this.size = size;
            this.resolver = resolver;
            this.signal = signal;
        }

        @Override
        public long[] call() throws Exception {
            long[] output = new long[size];
            IDistHashedCoupler mapper = resolver.resolve();

            synchronized (signal) {
                signal.wait(10000);
            }

            for (int i = 0; i < output.length; i++) {
                output[i] = mapper.applyAsLong(i);
                if ((i % 100) == 0) {
                    System.out.println("wrote t:" + slot + ", iter:" + i + ", val:" + output[i]);
                }
            }
            return output;
        }
    }

}