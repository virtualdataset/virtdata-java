package io.virtdata.long_double;

import org.assertj.core.data.Offset;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Test
public class ResampleLongToDoubleFuncTest {

    @Test
    public void testCoarseLongToDoubleLinearInterpolation() {
        ResampleLongToDoubleFunc resampler = new ResampleLongToDoubleFunc(
                "LongRangeToDouble()", 0.0, (double) Long.MAX_VALUE, 10
        );

        double v;
        v = resampler.applyAsDouble(0);
        assertThat(v).isCloseTo(0.0D, Offset.offset(0.01D));

        v = resampler.applyAsDouble(Long.MAX_VALUE);
        assertThat(v).isCloseTo((double) Long.MAX_VALUE, Offset.offset(0.01D));

        v = resampler.applyAsDouble((long)((0.23456D)* (double)Long.MAX_VALUE));
        assertThat(v).isCloseTo(((0.23456D)* (double)Long.MAX_VALUE), Offset.offset(0.01D));

    }

}