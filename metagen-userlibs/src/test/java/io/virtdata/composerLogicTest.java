package io.virtdata;

import io.virtdata.api.Generator;
import io.virtdata.libraryimpl.ComposerLibrary;
import org.testng.annotations.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Test
public class composerLogicTest {

    @Test
    public void  testIntegratedComposer() {
        ComposerLibrary cl = new ComposerLibrary();
        Optional<Generator<Object>> generator = cl.getGenerator("compose binomial:8,0.5 ToDate");
        assertThat(generator).isNotNull();
        assertThat(generator.isPresent()).isTrue();
        assertThat(generator.get().get(1)).isNotNull();
    }

    @Test
    public void testComplexComposition() {
        ComposerLibrary cl = new ComposerLibrary();
        Optional<Generator<Object>> generator = cl.getGenerator("compose Murmur3Hash normal:50,10.0 AddLong:50 ToString Suffix:_stuff");
        assertThat(generator).isNotNull();
        assertThat(generator.isPresent()).isTrue();
        assertThat(generator.get().get(1)).isNotNull();
        for (int i = 0; i < 1000; i++) {
            System.out.println(generator.get().get(i));
        }

    }

}
