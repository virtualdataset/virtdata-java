package io.virtdata.libraryimpl;

import io.virtdata.BasicGenerators;
import io.virtdata.api.Generator;
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
        Optional<Generator<Object>> generator = new BasicGenerators().getGenerator("StaticStringGenerator:foo");
        assertThat(generator.isPresent()).isTrue();
        assertThat(generator.get().get(5)).isEqualTo("foo");
    }

    @Test
    public void testGetGeneratorNames() throws Exception {
        BasicGenerators basics = new BasicGenerators();
        List<String> gnames = basics.getGeneratorNames();
        assertThat(gnames.size()>5);
        assertThat(gnames.contains("StaticStringGenerator")).isTrue();

    }

    @Test
    public void testToDateSpaceInstantiator() throws Exception {
        BasicGenerators basics = new BasicGenerators();
        Optional<Generator<Date>> generator = basics.getGenerator("ToDate:1000");
        assertThat(generator).isNotNull();
        assertThat(generator.get()).isNotNull();
        Date d1 = generator.get().get(1);
        Date d2 = generator.get().get(2);
        assertThat(d2).isAfter(d1);
    }
    @Test
    public void testToDateSpaceAndCountInstantiator() throws Exception {
        BasicGenerators basics = new BasicGenerators();
        Optional<Generator<Date>> generator = basics.getGenerator("ToDate:1000:10");
        assertThat(generator).isNotNull();
        assertThat(generator.get()).isNotNull();
        Date d1 = generator.get().get(1);
        Date d2 = generator.get().get(2);
        assertThat(d2).isAfter(d1);
    }
    @Test
    public void testToDateInstantiator() throws Exception {
        BasicGenerators basics = new BasicGenerators();
        Optional<Generator<Date>> generator = basics.getGenerator("ToDate");
        assertThat(generator).isNotNull();
        assertThat(generator.get()).isNotNull();
        Date d1 = generator.get().get(1);
        Date d2 = generator.get().get(2);
    }

    @Test
    public void testToDateBucketInstantiator() throws Exception {
        BasicGenerators basics = new BasicGenerators();
        Optional<Generator<Date>> generator = basics.getGenerator("ToDate:1000:10000");
        assertThat(generator).isNotNull();
        assertThat(generator.get()).isNotNull();
        assertThat(generator.get().get(1).after(new Date(1)));
    }

    @Test
    public void testRandomLineToIntInstantiator() throws Exception {
        BasicGenerators basics = new BasicGenerators();
        Optional<Generator<Integer>> generator = basics.getGenerator("RandomLineToInt:data/numbers.txt");
        assertThat(generator).isNotNull();
        assertThat(generator.get()).isNotNull();
        assertThat(generator.get().get(1)).isNotNull();
    }
    @Test
    public void testThreadNumToIntegerInstantiator() throws Exception {
        BasicGenerators basics = new BasicGenerators();
        Optional<Generator<Integer>> generator = basics.getGenerator("ThreadNumToInteger");
        assertThat(generator).isNotNull();
        assertThat(generator.get()).isNotNull();
        assertThat(generator.get().get(1)).isNotNull();
    }
    @Test
    public void testRandomLineTosStringInstantiator() throws Exception {
        BasicGenerators basics = new BasicGenerators();
        Optional<Generator<Integer>> generator = basics.getGenerator("RandomLineToString:data/numbers.txt");
        assertThat(generator).isNotNull();
        assertThat(generator.get()).isNotNull();
        assertThat(generator.get().get(1)).isNotNull();
    }

}