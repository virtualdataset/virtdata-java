package io.virtdata.valuesapp;

import io.virtdata.api.DataMapper;
import io.virtdata.core.AllDataMapperLibraries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ValuesCheckerCoordinator implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(ValuesCheckerCoordinator.class);

    private final String specifier;
    private final int threads;
    private final int bufsize;
    private final long end;
    private final long start;
    private final boolean isolated;
    //    private final Semaphore semaphore;
    private final ReentrantLock lock;
    private final Condition goTime;
    private final ConcurrentLinkedDeque<Throwable> errors = new ConcurrentLinkedDeque<>();
    private final ConcurrentLinkedQueue<Integer> readyQueue = new ConcurrentLinkedQueue<>();

    public ValuesCheckerCoordinator(
            String specifier,
            int threads,
            int bufsize,
            long start,
            long end,
            boolean isolated) {
        this.specifier = specifier;
        this.threads = threads;
        this.bufsize = bufsize;
        this.start = start;
        this.end = end;
        this.isolated = isolated;
//        this.semaphore = new Semaphore(threads,false);
        this.lock = new ReentrantLock();
        this.goTime = lock.newCondition();
    }


    @Override
    public void run() {
        testConcurrentValues(threads, start, end, specifier);
        if (this.errors.size() > 0) {
            for (Throwable error : errors) {
                System.out.println(error.getMessage());
            }
            throw new RuntimeException("Errors in verification: " + this.errors);
        }
    }


    private void testConcurrentValues(
            int threads,
            long start,
            long end,
            String mapperSpec) {

        // Generate reference values in single-threaded mode.
        DataMapper<Object> mapper =
                AllDataMapperLibraries.get().getDataMapper(specifier).orElseThrow(
                        () -> new RuntimeException("Unable to map function for specifier: " + specifier)
                );

        final List<Object> reference = new CopyOnWriteArrayList<>();

        // Setup concurrent generator pool
        ValuesCheckerExceptionHandler valuesCheckerExceptionHandler = new ValuesCheckerExceptionHandler(this);
        IndexedThreadFactory tf = new IndexedThreadFactory("values-checker", valuesCheckerExceptionHandler);
        ExecutorService pool = Executors.newFixedThreadPool(threads, tf);

        logger.info("Checking [{}..{}) in chunks of {}", start, end, bufsize);

        for (int t = 0; t < threads; t++) {
            ValuesCheckerRunnable runnable;
            if (isolated) {
                runnable = new ValuesCheckerRunnable(
                        start, end, bufsize, t, mapperSpec, null, readyQueue, goTime, lock, reference
                );
            } else {
                DataMapper<?> threadMapper = AllDataMapperLibraries.get()
                        .getDataMapper(mapperSpec)
                        .orElseThrow(
                                () -> new RuntimeException("Unable to map function for specifier: " + specifier)
                        );
                runnable = new ValuesCheckerRunnable(
                        start, end, bufsize, t, null, threadMapper, readyQueue, goTime, lock, reference
                );
            }
            pool.execute(runnable);
        }

        logger.info("starting generation loops...");

        for (long intervalStart = 0; intervalStart < (end - start); intervalStart += bufsize) {

            logger.debug("generating reference data for [" + intervalStart + ".." + (intervalStart + bufsize) + ")");

            List<Object> genRef = new ArrayList<>(bufsize);
            for (int refidx = 0; refidx < bufsize; refidx++) {
                genRef.add(mapper.get(refidx + intervalStart));
                if (refidx == 0) {
                    logger.debug("ref i:" + refidx + ", " + "ref cycle: " + (refidx + intervalStart) + ": " + genRef.get(genRef.size() - 1));
                }

            }
            reference.clear();
            reference.addAll(genRef);


            String rangeInfo = "[" + intervalStart + ".." + (intervalStart + bufsize) + ")";
            coordinateFor("generation " + rangeInfo);
            throwInjectedExceptions();
            coordinateFor("verification " + rangeInfo);
            throwInjectedExceptions();
            coordinateFor("completion " + rangeInfo);
            throwInjectedExceptions();
        }
        pool.shutdown();
        try {
            pool.awaitTermination(60000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private synchronized void throwInjectedExceptions() {
        if (errors.peekFirst() != null) {
            int count = 0;
            for (Throwable error : errors) {
                System.out.print("EXCEPTION " + count++ + ": ");
                System.out.println(error.getMessage());
            }
            throw new RuntimeException(errors.peekFirst());
        }
    }

    synchronized void handleException(Thread t, Throwable e) {
        this.errors.add(e);
    }

    private void coordinateFor(String forWhat) {
        try {
            long delay = 10;
            while (readyQueue.size() < threads) {
                logger.debug("threads ready for " + forWhat + ": " + readyQueue.size() + ", delaying " + delay + "ms");
                Thread.sleep(delay);
                delay = Math.min(1024, delay * 2);
                throwInjectedExceptions();
            }
            readyQueue.clear();
            lock.lock();
            goTime.signalAll();
        } catch (Exception e) {
            logger.error("Error while signaling threads: " + e.getMessage(), e);
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }


    }
}