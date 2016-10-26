package io.virtdata.libraryimpl;

import io.virtdata.api.Generator;
import io.virtdata.libimpl.CDistMappedLibrary;
import org.testng.annotations.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Test
public class CDistMappedLibraryTest {

    @Test
    public void testNormalDistribution() {
        CDistMappedLibrary cpd = new CDistMappedLibrary();
        Optional<Generator<Object>> optionalGenerator = cpd.getGenerator("mapto_normal(50,10)");
        Generator<Object> gen = optionalGenerator.get();
        Object o = gen.get(1);
        assertThat(o).isNotNull();
        assertThat(o).isInstanceOf(Double.class);

    }

}