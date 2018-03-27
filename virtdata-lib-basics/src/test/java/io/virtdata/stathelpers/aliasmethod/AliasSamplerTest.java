package io.virtdata.stathelpers.aliasmethod;

import io.virtdata.stathelpers.EvProbD;
import org.testng.annotations.Test;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class AliasSamplerTest {

    @Test
    public void testBasicAccuracy() {

        ByteBuffer bb = ByteBuffer.allocate(10 * AliasSampler.RECORD_LEN);
        System.out.println("remaining: " + bb.remaining());
        double pool=1.0D;
        for (int i = 0; i < 10; i++) {
            double slice = pool / (i+2);
            if (i==9) slice=pool;
            pool-=slice;

            bb.putDouble(slice);
            bb.putInt(i);
            bb.putInt(i+10);
            System.out.format("s:%f i0=%d, i1=%d ",slice,i,i+10);
            System.out.println(" remaining: " + bb.remaining());
        }
        bb.flip();

        AliasSampler as = new AliasSampler(bb);

        for (int i = 0; i < 100; i++) {
            double samplePoint = (double)i/100D;
            int i1 = as.applyAsInt(samplePoint);
            System.out.println(i1);
        }
    }

    @Test
    public void testAliasTableGeneration() {
        List<EvProbD> events = new ArrayList<>();
        events.add(new EvProbD(1,1.0D));
        events.add(new EvProbD(2,1.0D));
        events.add(new EvProbD(3,2.0D));
        events.add(new EvProbD(4,4.0D));
        events.add(new EvProbD(5,8.0D));
        events.add(new EvProbD(6,16.0D));
        events.add(new EvProbD(7,32.0D));
        events.add(new EvProbD(8,64.0D));

        AliasSampler as = new AliasSampler(events);
        int[] stats = new int[9];
        for (int i = 0; i < 10000; i++) {
            double v = (double)i / 10000D;
            int idx = as.applyAsInt(v);
            stats[idx]++;
        }
        System.out.println(Arrays.toString(stats));
        assertThat(stats).containsExactly(0,79,79,157,313,626,1250,2498,4998);

    }

    @Test
    public void testAliasTableGeneration2() {
        List<EvProbD> events = new ArrayList<>();
        events.add(new EvProbD(1,1D));
        events.add(new EvProbD(2,2D));
        events.add(new EvProbD(3,3D));

        AliasSampler as = new AliasSampler(events);

        int[] stats = new int[4];
        for (int i = 0; i < 10000; i++) {
            double v = (double)i / 10000D;
            int idx = as.applyAsInt(v);
            stats[idx]++;
        }
        System.out.println(Arrays.toString(stats));
        assertThat(stats).containsExactly(0,1667,3333,5000);
    }


    // Single threaded performance: 100_000_000 ops in 1_352_739_727 nanos for 73_924_050.579761 ops/s
    // yes, that is 73M discrete probability samples per second, but hey, it's only 3 discrete probabilities in this test
    @Test(enabled=true)
    public void testAliasMicroBenchSmallMany() {
        List<EvProbD> events = new ArrayList<>();
        events.add(new EvProbD(1,1D));
        events.add(new EvProbD(2,2D));
        events.add(new EvProbD(3,3D));

        AliasSampler as = new AliasSampler(events);

        long count=1_000_000_00;
        long startAt = System.nanoTime();
        for (int i = 0; i < count; i++) {
            double v = (double)i / count;
            int idx = as.applyAsInt(v);
        }
        long endAt = System.nanoTime();
        long nanos = endAt - startAt;
        double oprate = ((double) count / (double) nanos) * 1_000_000_000D;
        System.out.format("Single threaded performance: %d ops in %d nanos for %f ops/s\n", count, nanos, oprate);
    }

    // Single threaded performance: 100_000_000 ops in 1_232_994_499 nanos for 81_103_362.651742 ops/s
    // yes, that is 81M discrete probability samples per second, but hey, it's only 100K discrete probabilities in this test
    // Warning: Using datasets with higher counts than this will result in uncomfortably long setup times.
    @Test(enabled=true)
    public void testAliasMicroBenchLargeMany() {
        List<EvProbD> events = new ArrayList<>();
        int evt_count=1_00_000;
        for (int i = 0; i < evt_count; i++) {
            double val = (double)i/(double)evt_count;
            events.add(new EvProbD(i,val));
        }
        AliasSampler as = new AliasSampler(events);

        long count=1_000_000_00;
        long startAt = System.nanoTime();
        for (int i = 0; i < count; i++) {
            double v = (double)i / count;
            int idx = as.applyAsInt(v);
        }
        long endAt = System.nanoTime();
        long nanos = endAt - startAt;
        double oprate = ((double) count / (double) nanos) * 1_000_000_000D;
        System.out.format("Single threaded performance: %d ops in %d nanos for %f ops/s\n", count, nanos, oprate);
    }


}