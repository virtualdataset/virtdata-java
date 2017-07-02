package io.virtdata.valuesapp;

import io.virtdata.api.DataMapper;
import io.virtdata.core.AllDataMapperLibraries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class ValuesCheckerRunnable implements Runnable {

    private final static Logger logger = LoggerFactory.getLogger(ValuesCheckerRunnable.class);

    private final Condition goTime;
    private final Lock lock;
    private final long start;
    private final long end;
    private final List<Object> expected;
    private final DataMapper mapper;
    private final int threadNum;
    private final ConcurrentLinkedQueue<Integer> readyQueue;
    private int bufsize;

    public ValuesCheckerRunnable(
            long start,
            long end,
            int bufsize,
            int threadNum,
            String mapperSpec,
            DataMapper<?> dataMapper,
            ConcurrentLinkedQueue<Integer> readyQueue,
            Condition goTime,
            Lock lock,
            List<Object> expected
    ) {
        this.start = start;
        this.end = end;
        this.bufsize = bufsize;
        this.threadNum = threadNum;
        this.readyQueue = readyQueue;
        this.expected = expected;
        this.goTime = goTime;
        this.lock = lock;

        if (dataMapper != null) {
            logger.debug(threadNum + ": Sharing data mapper, only expect success for explicitly thread-safe generators.");
        } else {
            logger.debug(threadNum + ": Using per-thread mapper instancing.");
        }

        this.mapper = (dataMapper != null) ? dataMapper : AllDataMapperLibraries.get().getDataMapper(mapperSpec)
                .orElseThrow(
                        () -> new RuntimeException("unable to resolve mapper for " + mapperSpec)
                );


    }

    @Override
    public void run() {
        Object[] output = new Object[bufsize];

        for (long rangeStart = start; rangeStart < end; rangeStart += bufsize) {
            String rangeInfo = "t:" + threadNum + " [" + rangeStart + ".." + (rangeStart+bufsize) + ")";

            synchronizeFor("generation " + rangeInfo);
//            logger.debug("generating for " + "range: " + rangeStart + ".." + (rangeStart + bufsize));
            for (int i = 0; i < output.length; i++) {
                output[i] = mapper.get(i + rangeStart);
//                if (i==0) {
//                    logger.debug("gen i:" + i + ", cycle: " + (i + rangeStart) + ": " + output[i]);
//                }

            }

            synchronizeFor("verification " + rangeInfo);
//            logger.debug("checker " + this.threadNum + " verifying range [" + start + ".." + (start + end) + ")");
            for (int bufidx = 0; bufidx < expected.size(); bufidx++) {
                if (!expected.get(bufidx).equals(output[bufidx])) {
                    String errmsg = "Value differs: " +
                            "iteration: " + (bufidx + rangeStart) +
                            " expected:'" + expected.get(bufidx) + "' actual:'" + output[bufidx] + "'";

                    throw new RuntimeException(errmsg);
                }
//                else
//                {
//                    System.out.println("Equal " + expected[bufidx] + " == " + output[bufidx]);
//                }
            }
            synchronizeFor("completion");

//            logger.info("verified values for thread " + Thread.currentThread().getName() + " in range " +
//                    rangeStart + ".." + (rangeStart + bufsize)
//            );
        }

    }

    private void synchronizeFor(String forWhat) {
        try {
            lock.lock();
            readyQueue.add(threadNum);
            logger.trace("awaiting signal for " + forWhat);
            goTime.await();
        } catch (Throwable e) {
            System.out.println("error while synchronizing: " + e.getMessage());
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }

    }
}
