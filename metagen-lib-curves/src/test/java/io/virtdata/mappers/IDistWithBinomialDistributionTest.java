package io.virtdata.mappers;

import io.virtdata.mappers.mapped_discrete.IDistMappedCoupler;
import io.virtdata.mappers.mapped_discrete.IDistMappedResolver;
import org.apache.commons.math3.distribution.BinomialDistribution;
import org.apache.commons.math3.distribution.IntegerDistribution;
import org.assertj.core.data.Offset;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class IDistWithBinomialDistributionTest {

    @Test
    public void ToBinomialProbabilityTest() {
        IDistMappedResolver tb = new IDistMappedResolver(BinomialDistribution.class.getCanonicalName(),"8","0.5");
        IDistMappedCoupler idc = tb.resolve();
        IntegerDistribution distribution = idc.getDistribution();
        Offset<Double> offset = Offset.offset(0.00001d);

        assertThat(distribution.probability(0)).isCloseTo(0.00390d,offset);
        assertThat(distribution.probability(1)).isCloseTo(0.03125d,offset);
        assertThat(distribution.probability(2)).isCloseTo(0.10937d,offset);
        assertThat(distribution.probability(3)).isCloseTo(0.21875d,offset);
        assertThat(distribution.probability(4)).isCloseTo(0.27343d,offset);
        assertThat(distribution.probability(5)).isCloseTo(0.21875d,offset);
        assertThat(distribution.probability(6)).isCloseTo(0.10937d,offset);
        assertThat(distribution.probability(7)).isCloseTo(0.03125d,offset);
        assertThat(distribution.probability(8)).isCloseTo(0.00390d,offset);
    }

    @Test
    public void ToBinomailDensityTest() {
        IDistMappedResolver tb = new IDistMappedResolver(BinomialDistribution.class,"8","0.5");
        IDistMappedCoupler idc = tb.resolve();

        long half = Long.MAX_VALUE / 2;
        Long expected = idc.applyAsLong(half);
        assertThat(expected).isEqualTo(4L);
        expected = idc.applyAsLong(1L);
        assertThat(expected).isEqualTo(0L);

        // threshold test against CDF
        expected = idc.applyAsLong((long)(0.03515d * (double) Long.MAX_VALUE));
        assertThat(expected).isEqualTo(1);
        expected = idc.applyAsLong((long)(0.03600d * (double) Long.MAX_VALUE));
        assertThat(expected).isEqualTo(2);

    }

}