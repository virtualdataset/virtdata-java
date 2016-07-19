package io.virtdata.libraryimpl;

import io.virtdata.api.Generator;
import io.virtdata.mappers.continuous.CDistLIbrary;
import org.testng.annotations.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Test
public class CDistLIbraryTest {

    @Test
    public void testNormalDistribution() {
        CDistLIbrary cpd = new CDistLIbrary();
        Optional<Generator<Object>> optionalGenerator = cpd.getGenerator("normal,50,10");
        Generator<Object> gen = optionalGenerator.get();
        Object o = gen.get(1);
        assertThat(o).isNotNull();
        assertThat(o).isInstanceOf(Double.class);

    }

}