package io.virtdata.core;

import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.testng.Assert.*;

@Test
public class CompatibilityFixupsTest {

    @Test
    public void testFixupModifiers() {
        assertThat(CompatibilityFixups.fixup("compute_levy(ASDF)")).isEqualTo("Levy(ASDF,'hash','compute')");
        assertThat(CompatibilityFixups.fixup("interpolate_levy(ASDF)")).isEqualTo("Levy(ASDF,'hash','interpolate')");
        assertThat(CompatibilityFixups.fixup("mapto_levy(ASDF)")).isEqualTo("Levy(ASDF,'map','interpolate')");
        assertThat(CompatibilityFixups.fixup("hashto_levy(ASDF)")).isEqualTo("Levy(ASDF,'hash','interpolate')");
    }

    @Test
    public void testFixupNames() {
        assertThat(CompatibilityFixups.fixup("gamma(foo)")).isEqualTo("Gamma(foo,'hash','interpolate')");
        assertThat(CompatibilityFixups.fixup("mapto_uniform_integer(foo)")).isEqualTo("Uniform(foo,'map','interpolate')");
        assertThat(CompatibilityFixups.fixup("hashto_uniform_real(foo)")).isEqualTo("Uniform(foo,'hash','interpolate')");
    }

}