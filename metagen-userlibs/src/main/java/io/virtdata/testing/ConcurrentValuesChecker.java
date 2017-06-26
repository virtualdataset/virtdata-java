package io.virtdata.testing;

import io.virtdata.api.DataMapper;
import io.virtdata.core.AllDataMapperLibraries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ConcurrentValuesChecker implements Callable<Boolean> {
    private static final Logger logger = LoggerFactory.getLogger(ConcurrentValuesChecker.class);

    private final String specifier;
    private final int threads;
    private final int bufsize;
    private final long end;
    private final long start;

    public ConcurrentValuesChecker(String specifier, int threads, int bufsize, long start, long end) {
        this.specifier = specifier;
        this.threads = threads;
        this.bufsize = bufsize;
        this.start = start;
        this.end = end;
    }

    @Override
    public Boolean call() throws Exception {
        testConcurrentValues("Testing concurrent values for "
        + " threads:" + threads +  ", bufsize:" + bufsize + "range:["+start+","+end+")", threads, start, end, specifier);
        return true;
    }


    private void testConcurrentValues(
            String description,
            int threads,
            long start,
            long end,
            String mapperSpec) {

        // Generate reference values in single-threaded mode.
        DataMapper<Object> mapper =
                AllDataMapperLibraries.get().getDataMapper(specifier).orElseThrow(
                        () -> new RuntimeException("Unable to map function for specifier: " + specifier)
                );

        Object[] values = new Object[bufsize];
        for (int index = 0; index < bufsize; index++) {
            values[index] = mapper.get(index);
        }

        // Setup concurrent generator pool
        ExecutorService pool = Executors.newFixedThreadPool(threads);

        for (long min = 0; min < (end-start); min += bufsize) {

            List<Future<Object[]>> futures = new ArrayList<>((int) (end-start));
            for (int t = 0; t < threads; t++) {
                futures.add(pool.submit(new ObjectMapperCallable(t, min, min+bufsize, mapperSpec, pool)));
            }
            try {
                Thread.sleep(1000);
                synchronized (pool) {
                    pool.notifyAll();
                }
            } catch (Throwable t) {
                throw new RuntimeException(t);
            }

            // Activate pool
            List<Object[]> results = new ArrayList<>();
//        long[][] results = new long[threads][iterations];

            for (int i = 0; i < futures.size(); i++) {
                try {
                    results.add(futures.get(i).get());
//                System.out.println(description + ": got results for thread " + i);
//                System.out.flush();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            for (int vthread = 0; vthread < threads; vthread++) {
                if (!Arrays.equals(results.get(vthread), values)) {
                    throw new RuntimeException("Values differ");
                }
                System.out.println(description + ": verified values for thread " + vthread + " range start at " + min);
            }

        }
        pool.shutdown();

    }

    private static class ObjectMapperCallable implements Callable<Object[]> {

        private final Object signal;
        private final int slot;
        private final String mapperSpec;
        private final long start;
        private final long end;
        private int size;

        public ObjectMapperCallable(int slot, long start, long end, String mapperSpec, Object signal) {
            this.slot = slot;
            this.start = start;
            this.end = end;
            this.mapperSpec = mapperSpec;
            this.signal = signal;
        }

        @Override
        public Object[] call() throws Exception {
            Object[] output = new Object[(int)(end-start)];
            DataMapper mapper = AllDataMapperLibraries.get().getDataMapper(mapperSpec)
                    .orElseThrow(
                            () -> new RuntimeException("unable to resolve mapper for " + mapperSpec)
                    );
//            System.out.println("resolved:" + mapper);
//            System.out.flush();

            synchronized (signal) {
                signal.wait(10000);
            }

            for (int i = 0; i < output.length; i++) {
                output[i] = mapper.get(i + start);
//                if ((i % 100) == 0) {
//                    System.out.println("wrote t:" + slot + ", iter:" + i + ", val:" + output[i]);
//                }
            }
            return output;
        }
    }

}