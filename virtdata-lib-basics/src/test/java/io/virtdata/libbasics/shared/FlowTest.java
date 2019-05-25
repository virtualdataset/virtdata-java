package io.virtdata.libbasics.shared;

import io.virtdata.libbasics.shared.from_double.to_double.Max;
import io.virtdata.libbasics.shared.from_long.to_long.Add;
import io.virtdata.libbasics.shared.unary_int.Mul;
import io.virtdata.libbasics.shared.unary_string.StringFlow;
import io.virtdata.libbasics.shared.unary_string.Prefix;
import io.virtdata.libbasics.shared.unary_string.Suffix;
import org.assertj.core.data.Offset;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FlowTest {

    @Test
    public void testLongFlow() {
        io.virtdata.libbasics.shared.from_long.to_long.Flow lf =
                new io.virtdata.libbasics.shared.from_long.to_long.Flow(
                        new Add(3L),
                        new Add(4L)
                );
        assertThat(lf.applyAsLong(3L)).isEqualTo(10L);

    }

    @Test
    public void testIntegerFlow() {
        Mul imul3 = new Mul(3);
        io.virtdata.libbasics.shared.unary_int.Flow ifl =
                new io.virtdata.libbasics.shared.unary_int.Flow(imul3, imul3);
        assertThat(ifl.applyAsInt(2)).isEqualTo(18);
    }

    @Test
    public void testDoubleFlow() {
        Max dmax12 = new Max(12D);
        Max dmax100 = new Max(100D);
        io.virtdata.libbasics.shared.from_double.to_double.Flow dmax =
                new io.virtdata.libbasics.shared.from_double.to_double.Flow(dmax12,dmax100);
        assertThat(dmax.applyAsDouble(13D)).isCloseTo(100D, Offset.offset(0.0001D));

    }

    @Test
    public void testStringFlow() {
        Prefix pf = new Prefix("->");
        Suffix sf = new Suffix ("<-");
        StringFlow flow = new StringFlow(pf,sf);
        assertThat(flow.apply("woot")).isEqualTo("->woot<-");
    }
}