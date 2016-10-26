package io.virtdata;

import io.virtdata.api.DataMapper;
import io.virtdata.libraryimpl.ComposerLibrary;
import org.testng.annotations.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Test
public class composerLogicTest {

    @Test
    public void  testIntegratedComposer() {
        ComposerLibrary cl = new ComposerLibrary();
        Optional<DataMapper<Object>> generator = cl.getDataMapper("compose binomial(8,0.5); ToDate -> Date");
        assertThat(generator).isNotNull();
        assertThat(generator.isPresent()).isTrue();
        assertThat(generator.get().get(1)).isNotNull();
    }

    @Test
    public void testComplexComposition() {
        ComposerLibrary cl = new ComposerLibrary();
        Optional<DataMapper<Object>> generator = cl.getDataMapper("compose Hash; mapto_normal(50,10.0); Add(50); ToString; Suffix(avgdays) -> String");
        assertThat(generator).isNotNull();
        assertThat(generator.isPresent()).isTrue();
        assertThat(generator.get().get(1)).isNotNull();
//        for (int i = 0; i < 1000; i++) {
//            System.out.println(generator.get().get(i));
//        }
    }

    @Test
    public void testComposerOnly() {
        ComposerLibrary cl = new ComposerLibrary();
        Optional<DataMapper<Object>> generator = cl.getDataMapper("Add(5)");
        assertThat(generator.isPresent()).isFalse();
    }

}
