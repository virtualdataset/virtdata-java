package io.virtdata.libraryimpl;

import io.virtdata.BasicGenerators;
import io.virtdata.api.DataMapper;
import org.testng.annotations.Test;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class BasicGeneratorsTest {


    @Test
    public void testGetLibraryName() throws Exception {
        String libraryName = new BasicGenerators().getLibraryName();
        assertThat(libraryName).isEqualTo("basics");

    }

    @Test
    public void testGetGenerator() throws Exception {
        Optional<DataMapper<Object>> generator = new BasicGenerators().getDataMapper("StaticStringGenerator(foo)");
        assertThat(generator.isPresent()).isTrue();
        assertThat(generator.get().get(5)).isEqualTo("foo");
    }

    @Test
    public void testGetGeneratorNames() throws Exception {
        BasicGenerators basics = new BasicGenerators();
        List<String> gnames = basics.getDataMapperNames();
        assertThat(gnames.size()>5);
        assertThat(gnames.contains("StaticStringGenerator")).isTrue();

    }

    @Test
    public void testMultipleChoiceLong() {
        BasicGenerators basics = new BasicGenerators();
        Optional<DataMapper<Object>> add5 = basics.getDataMapper("Add(5) -> long");
        assertThat(add5).isPresent();
        Object o = add5.get().get(5);
        assertThat(o.getClass()).isEqualTo(Long.class);
    }

    @Test
    public void testMultipleChoiceInt() {
        BasicGenerators basics = new BasicGenerators();
        Optional<DataMapper<Object>> add5 = basics.getDataMapper("Add(5) -> int");
        assertThat(add5).isPresent();
        Object o = add5.get().get(5);
        assertThat(o.getClass()).isEqualTo(Integer.class);
    }

    @Test
    public void testToDateSpaceInstantiator() throws Exception {
        BasicGenerators basics = new BasicGenerators();
        Optional<DataMapper<Date>> generator = basics.getDataMapper("ToDate(1000)");
        assertThat(generator).isNotNull();
        assertThat(generator.get()).isNotNull();
        Date d1 = generator.get().get(1);
        Date d2 = generator.get().get(2);
        assertThat(d2).isAfter(d1);
    }
    @Test
    public void testToDateSpaceAndCountInstantiator() throws Exception {
        BasicGenerators basics = new BasicGenerators();
        Optional<DataMapper<Date>> generator = basics.getDataMapper("ToDate(1000,10)");
        assertThat(generator).isNotNull();
        assertThat(generator.get()).isNotNull();
        Date d1 = generator.get().get(1);
        Date d2 = generator.get().get(2);
        assertThat(d2).isAfter(d1);
    }
    @Test
    public void testToDateInstantiator() throws Exception {
        BasicGenerators basics = new BasicGenerators();
        Optional<DataMapper<Date>> generator = basics.getDataMapper("ToDate");
        assertThat(generator).isNotNull();
        assertThat(generator.get()).isNotNull();
        Date d1 = generator.get().get(1);
        Date d2 = generator.get().get(2);
    }

    @Test
    public void testToDateBucketInstantiator() throws Exception {
        BasicGenerators basics = new BasicGenerators();
        Optional<DataMapper<Date>> generator = basics.getDataMapper("ToDate(1000,10000)");
        assertThat(generator).isNotNull();
        assertThat(generator.get()).isNotNull();
        assertThat(generator.get().get(1).after(new Date(1)));
    }

    @Test
    public void testRandomLineToIntInstantiator() throws Exception {
        BasicGenerators basics = new BasicGenerators();
        Optional<DataMapper<Integer>> generator = basics.getDataMapper("RandomLineToInt(data/numbers.txt)");
        assertThat(generator).isNotNull();
        assertThat(generator.get()).isNotNull();
        assertThat(generator.get().get(1)).isNotNull();
    }

}