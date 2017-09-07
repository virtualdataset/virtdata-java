package io.virtdata.libraryimpl;

import io.virtdata.basicsmappers.BasicDataMappers;
import io.virtdata.api.DataMapper;
import org.testng.annotations.Test;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class BasicDataMappersTest {


    @Test
    public void testGetLibraryName() throws Exception {
        String libraryName = new BasicDataMappers().getLibraryName();
        assertThat(libraryName).isEqualTo("basics");

    }

    @Test
    public void testGetDataMapper() throws Exception {
        Optional<DataMapper<Object>> dataMapper = new BasicDataMappers().getDataMapper("StaticStringMapper(foo)");
        assertThat(dataMapper.isPresent()).isTrue();
        assertThat(dataMapper.get().get(5)).isEqualTo("foo");
    }

    // TODO: Fix this when convergence around a guava version is possible, if ever
    @Test(enabled=false)
    public void testGetDataMapperNames() throws Exception {
        BasicDataMappers basics = new BasicDataMappers();
        List<String> gnames = basics.getDataMapperNames();
        assertThat(gnames.size()>5);
        assertThat(gnames.contains("StaticStringMapper")).isTrue();

    }

    @Test
    public void testMultipleChoiceLong() {
        BasicDataMappers basics = new BasicDataMappers();
        Optional<DataMapper<Object>> add5 = basics.getDataMapper("Add(5) -> long");
        assertThat(add5).isPresent();
        Object o = add5.get().get(5);
        assertThat(o.getClass()).isEqualTo(Long.class);
    }

    @Test
    public void testMultipleChoiceInt() {
        BasicDataMappers basics = new BasicDataMappers();
        Optional<DataMapper<Object>> add5 = basics.getDataMapper("Add(5) -> int");
        assertThat(add5).isPresent();
        Object o = add5.get().get(5);
        assertThat(o.getClass()).isEqualTo(Integer.class);
    }

    @Test
    public void testToDateSpaceInstantiator() throws Exception {
        BasicDataMappers basics = new BasicDataMappers();
        Optional<DataMapper<Date>> dataMapper = basics.getDataMapper("ToDate(1000)");
        assertThat(dataMapper).isNotNull();
        assertThat(dataMapper.get()).isNotNull();
        Date d1 = dataMapper.get().get(1);
        Date d2 = dataMapper.get().get(2);
        assertThat(d2).isAfter(d1);
    }
    @Test
    public void testToDateSpaceAndCountInstantiator() throws Exception {
        BasicDataMappers basics = new BasicDataMappers();
        Optional<DataMapper<Date>> dataMapper = basics.getDataMapper("ToDate(1000,10)");
        assertThat(dataMapper).isNotNull();
        assertThat(dataMapper.get()).isNotNull();
        Date d1 = dataMapper.get().get(1);
        Date d2 = dataMapper.get().get(2);
        assertThat(d2).isAfter(d1);
    }
    @Test
    public void testToDateInstantiator() throws Exception {
        BasicDataMappers basics = new BasicDataMappers();
        Optional<DataMapper<Date>> dataMapper = basics.getDataMapper("ToDate");
        assertThat(dataMapper).isNotNull();
        assertThat(dataMapper.get()).isNotNull();
        Date d1 = dataMapper.get().get(1);
        Date d2 = dataMapper.get().get(2);
    }

    @Test
    public void testToDateBucketInstantiator() throws Exception {
        BasicDataMappers basics = new BasicDataMappers();
        Optional<DataMapper<Date>> dataMapper = basics.getDataMapper("ToDate(1000,10000)");
        assertThat(dataMapper).isNotNull();
        assertThat(dataMapper.get()).isNotNull();
        assertThat(dataMapper.get().get(1).after(new Date(1)));
    }

    @Test
    public void testRandomLineToIntInstantiator() throws Exception {
        BasicDataMappers basics = new BasicDataMappers();
        Optional<DataMapper<Integer>> dataMapper = basics.getDataMapper("HashedLineToInt(data/numbers.txt)");
        assertThat(dataMapper).isNotNull();
        assertThat(dataMapper.get()).isNotNull();
        assertThat(dataMapper.get().get(1)).isNotNull();
    }

}