package io.virtdata.gen.generators;

import io.virtdata.gen.internal.DiscreteDistributionSampler;

public class DiscreteDistributionSamplerTest {

    public void testBinomialDist() {
        DiscreteDistributionSampler binomial = new DiscreteDistributionSampler("binomial", "0.5", "5");
    }


    /*
    @Test
    public void testDiscreteDistributionSamplerInstantiator() throws Exception {
        CurveGenerators curves = new CurveGenerators();
        Optional<Generator<Integer>> generator = curves.getGenerator("DiscreteDistributionSampler:binomial:0.5:5");
        assertThat(generator).isNotNull();
        assertThat(generator.get()).isNotNull();
        assertThat(generator.get().get(1)).isNotNull();
    }
    */

}