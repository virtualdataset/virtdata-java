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
        Optional<DataMapper<Object>> dataMapper = cl.getDataMapper("compose binomial(8,0.5); ToDate -> Date");
        assertThat(dataMapper).isNotNull();
        assertThat(dataMapper.isPresent()).isTrue();
        assertThat(dataMapper.get().get(1)).isNotNull();
    }

    @Test
    public void testComplexComposition() {
        ComposerLibrary cl = new ComposerLibrary();
        Optional<DataMapper<Object>> dataMapper = cl.getDataMapper("compose Hash; mapto_normal(50,10.0); Add(50); ToString; Suffix(avgdays) -> String");
        assertThat(dataMapper).isNotNull();
        assertThat(dataMapper.isPresent()).isTrue();
        assertThat(dataMapper.get().get(1)).isNotNull();
//        for (int i = 0; i < 1000; i++) {
//            System.out.println(dataMapper.get().get(i));
//        }
    }

    @Test
    public void testComposerOnly() {
        ComposerLibrary cl = new ComposerLibrary();
        Optional<DataMapper<Object>> dataMapper = cl.getDataMapper("Add(5)");
        assertThat(dataMapper.isPresent()).isFalse();
    }

}
