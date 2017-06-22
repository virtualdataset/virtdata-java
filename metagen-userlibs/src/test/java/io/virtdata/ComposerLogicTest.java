package io.virtdata;

import io.virtdata.api.DataMapper;
import io.virtdata.libraryimpl.ComposerLibrary;
import io.virtdata.testing.functions.ARandomPOJO;
import org.testng.annotations.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Test
public class ComposerLogicTest {

    @Test
    public void testSignatureMapping() {
        ComposerLibrary cl = new ComposerLibrary();
        Optional<DataMapper<Object>> dataMapper = cl.getDataMapper(
                "compose HashRange(1000000000,9999999999); ToString() -> String"
        );
        assertThat(dataMapper).isNotNull();
        assertThat(dataMapper).isPresent();
        Object v = dataMapper.get().get(5L);
        assertThat(v).isNotNull();
    }

    @Test
    public void  testIntegratedComposer() {
        ComposerLibrary cl = new ComposerLibrary();
        Optional<DataMapper<Object>> dataMapper = cl.getDataMapper(
                "compose binomial(8,0.5); ToDate -> Date"
        );
        assertThat(dataMapper).isNotNull();
        assertThat(dataMapper).isPresent();
        assertThat(dataMapper.get().get(1)).isNotNull();
    }

    @Test
    public void testComplexComposition() {
        ComposerLibrary cl = new ComposerLibrary();
        Optional<DataMapper<Object>> dataMapper = cl.getDataMapper(
                "compose Hash; mapto_normal(50,10.0); Add(50); ToString; Suffix(avgdays) -> String"
        );
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

    @Test
    public void testResourceLoader() {
        ComposerLibrary cl = new ComposerLibrary();
        Optional<DataMapper<Object>> dataMapper = cl.getDataMapper("compose ModuloLineToString(data/variable_words.txt) -> String");
        assertThat(dataMapper).isPresent();
        assertThat(dataMapper.get().get(1)).isEqualToComparingFieldByField("completion_count");
        dataMapper = cl.getDataMapper("compose ModuloLineToString(variable_words.txt) -> String");
        assertThat(dataMapper).isPresent();
        assertThat(dataMapper.get().get(1)).isEqualToComparingFieldByField("completion_count");
    }

    @Test
    public void testPOJOTypeSpecializer() {
        ComposerLibrary cl = new ComposerLibrary();
        Optional<DataMapper<Object>> dataMapper = cl.getDataMapper("compose LongToLongPOJO -> ARandomPOJO");
        assertThat(dataMapper).isPresent();
        assertThat(dataMapper.get().get(1)).isOfAnyClassIn(ARandomPOJO.class);
    }

}
