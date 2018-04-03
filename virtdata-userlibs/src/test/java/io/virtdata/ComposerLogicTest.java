package io.virtdata;

import io.virtdata.api.DataMapper;
import io.virtdata.basicsmappers.from_long.to_string.MapTemplate;
import io.virtdata.basicsmappers.from_long.to_string.Template;
import io.virtdata.core.VirtData;
import io.virtdata.testing.functions.ARandomPOJO;
import org.testng.annotations.Test;

import java.util.Map;
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

    @Test
    public void testNestedFunction() {
        Template t = new Template("_{}_{}_", String::valueOf, String::valueOf);
        String r = t.apply(5);
        assertThat(r).isEqualTo("_5_6_");
        Optional<DataMapper<String>> m2 = VirtData.getMapper("Template('_{}_',long->NumberNameToString()->java.lang.String)");
        assertThat(m2).isPresent();
        DataMapper<String> dm2 = m2.get();
        Object r3 = dm2.get(42L);

    }

    public void testBrokenTemplate() {
        Optional<DataMapper<String>> m2 = VirtData.getMapper("Template('{\"alt1-{}\",\"alt2-{}\"}',ToLongFunction(Identity()),ToLongFunction(Identity()))");
        assertThat(m2).isPresent();
        DataMapper<String> dm2 = m2.get();
        Object r3 = dm2.get(42L);

    }
    @Test
    public void testMapTemplate() {
        MapTemplate mt = new MapTemplate(l -> (int)l,String::valueOf, String::valueOf);
        assertThat(mt.apply(3)).containsEntry("3","3");
        Optional<DataMapper<Map>> optionalMapper =VirtData.getMapper(
                "MapTemplate(long->Mod(5)->int,NumberNameToString(),NumberNameToString())"
        );
        assertThat(optionalMapper).isPresent();
        DataMapper<Map> mapper = optionalMapper.get();
        Map o = mapper.get(6L);
        assertThat(o).isNotNull();
        assertThat(o.get("six")).isEqualTo("six");
    }

    @Test
    public void testNegativeLongs() {
        Optional<DataMapper<Long>> mo = VirtData.getMapper("HashRange(-2147483648L,2147483647L) -> long");
        assertThat(mo).isPresent();
        DataMapper<Long> longDataMapper = mo.get();
        Long result = longDataMapper.get(5L);
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(1398623797L);

    }
}
