package io.virtdata;

import io.virtdata.api.Generator;
import io.virtdata.libraryimpl.ComposerLibrary;
import org.testng.annotations.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Test
public class TestIntegration {

    @Test
    public void  testIntegratedComposer() {
        ComposerLibrary cl = new ComposerLibrary();
        Optional<Generator<Object>> generator = cl.getGenerator("compose Binomial:8,0.5 ToDate");
        assertThat(generator).isNotNull();
        assertThat(generator.isPresent()).isTrue();
        assertThat(generator.get().get(1)).isNotNull();
    }

}
