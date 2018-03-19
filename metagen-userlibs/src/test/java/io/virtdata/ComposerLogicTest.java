package io.virtdata;

import io.virtdata.api.DataMapper;
import io.virtdata.core.VirtData;
import io.virtdata.testing.functions.ARandomPOJO;
import org.testng.annotations.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Test
public class ComposerLogicTest {


    @Test
    public void testSignatureMapping() {
        Optional<DataMapper<Object>> dataMapper = VirtData.getMapper(
                "compose HashRange(1000000000,9999999999L); ToString() -> String"
        );
        assertThat(dataMapper).isNotNull();
        assertThat(dataMapper).isPresent();
        Object v = dataMapper.get().get(5L);
        assertThat(v).isNotNull();
    }

    @Test
    public void  testIntegratedComposer() {
        Optional<DataMapper<Object>> dataMapper = VirtData.getMapper(
                "binomial(8,0.5); ToDate() -> java.util.Date"
        );
        assertThat(dataMapper).isNotNull();
        assertThat(dataMapper).isPresent();
        assertThat(dataMapper.get().get(1)).isNotNull();
    }

    @Test
    public void testComplexComposition() {
        Optional<DataMapper<Object>> dataMapper = VirtData.getMapper(
                "Hash(); mapto_normal(50,10.0); Add(50); ToString(); Suffix('avgdays') -> String"
        );
        assertThat(dataMapper).isNotNull();
        assertThat(dataMapper.isPresent()).isTrue();
        assertThat(dataMapper.get().get(1)).isNotNull();
//        for (int i = 0; i < 1000; i++) {
//            System.out.println(dataMapper.getInverseCumulativeDensity().getInverseCumulativeDensity(i));
//        }
    }

    @Test
    public void testComposerOnly() {
        Optional<DataMapper<Object>> dataMapper = VirtData.getMapper("Add(5)");
        assertThat(dataMapper.isPresent()).isTrue();
    }

    @Test
    public void testResourceLoader() {
        Optional<DataMapper<Object>> dataMapper = VirtData.getMapper(" ModuloLineToString('data/variable_words.txt') -> String");
        assertThat(dataMapper).isPresent();
        assertThat(dataMapper.get().get(1)).isEqualToComparingFieldByField("completion_count");
        dataMapper = VirtData.getMapper("compose ModuloLineToString('variable_words.txt') -> String");
        assertThat(dataMapper).isPresent();
        assertThat(dataMapper.get().get(1)).isEqualToComparingFieldByField("completion_count");
    }

    @Test
    public void testPOJOTypeSpecializer() {
        Optional<DataMapper<Object>> dataMapper = VirtData.getMapper("compose LongToLongPOJO() -> io.virtdata.testing.functions.ARandomPOJO");
        assertThat(dataMapper).isPresent();
        assertThat(dataMapper.get().get(1)).isOfAnyClassIn(ARandomPOJO.class);
    }

}
