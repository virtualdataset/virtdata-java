package io.virtdata.libimpl;

import io.virtdata.libimpl.discrete.IMappedDistFunction;
import org.apache.commons.math4.distribution.BinomialDistribution;
import org.testng.annotations.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class IMappedDistFunctionTest {
    private static double[] binomial85steps = new double[]{
            0.00390d, 0.03125d, 0.10937d, 0.21875d, 0.27343d, 0.21875d, 0.10937d, 0.03125d, 0.00390d,
    };

    @Test
    public void testBinomialMappedDist() {
        IMappedDistFunction b85 = new IMappedDistFunction(new BinomialDistribution(8, 0.5D));
        assertThat(b85.applyAsInt(0L)).isEqualTo(0);
        assertThat(b85.applyAsInt(Long.MAX_VALUE)).isEqualTo(8);
        double c[] = new double[binomial85steps.length];
        c[0]=binomial85steps[0];
        for (int i = 1; i < c.length; i++) {
            c[i] = c[i-1]+binomial85steps[i];
        }
        System.out.println("cumulative density points:"  + Arrays.toString(c));
        long[] t = Arrays.stream(c).mapToLong(d -> (long) (d * Long.MAX_VALUE)).toArray();
        double maxv = (double) Long.MAX_VALUE;

        double phi=0.001D;
        for (int b = 0; b < c.length-1; b++) {

            long beforeBoundary = (long)(Math.max(0.0D,(c[b])-phi)*maxv);
            double beforeDouble = (double)beforeBoundary / (double)Long.MAX_VALUE;
            int vb = b85.applyAsInt(beforeBoundary);

            System.out.println("vb:" + vb + ", before:" + b + " bb:" + beforeBoundary + ", reconverted: " + beforeDouble);
            System.out.flush();
            assertThat(vb).isEqualTo(b);

            long afterBoundary= (long)(Math.min(1.0D,(c[b])+phi)*maxv);
            double afterDouble = (double)afterBoundary / (double)Long.MAX_VALUE;
            int va = b85.applyAsInt(afterBoundary);
            System.out.println("va:" + va + " after:" + b + " ab:" + afterBoundary + ", reconverted: " + afterDouble);
            System.out.flush();
            assertThat(va).isEqualTo(b+1);

        }
//        assertThat(b85.applyAsInt((long)(c[0]*maxv))-1).isEqualTo(0);
//        assertThat(b85.applyAsInt((long)(c[0]*maxv))+1).isEqualTo(1);

    }

    @Test
    public void showBinomialICDF() {
        IMappedDistFunction b85 = new IMappedDistFunction(new BinomialDistribution(8, 0.5D));
        for (int i = 0; i < 1000; i++) {
            double factor=((double) i / 1000D);
            int v = b85.applyAsInt((long) (factor * (double) Long.MAX_VALUE));
            System.out.println("i:" + i + ",f: " + factor + ", v:" + v);
        }

    }

}