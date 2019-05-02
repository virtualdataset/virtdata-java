package io.virtdata;

import io.virtdata.api.DataMapper;
import io.virtdata.core.VirtData;
import io.virtdata.util.ModuleInfo;
import org.assertj.core.api.Assertions;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Date;
import java.util.Optional;

public class BasicDataMappersTestIT {

    @BeforeClass
    public static void logModuleInfo() {
        ModuleInfo.logModuleNamesDebug(BasicDataMappersTestIT.class);
    }

    @Test
    public void testGetDataMapper() throws Exception {
        Optional<DataMapper<Object>> dataMapper = VirtData.getOptionalMapper("StaticStringMapper('foo')");
        Assertions.assertThat(dataMapper.isPresent()).isTrue();
        Assertions.assertThat(dataMapper.get().get(5)).isEqualTo("foo");
    }

    @Test
    public void testMultipleChoiceLong() {
        Optional<DataMapper<Object>> add5 = VirtData.getOptionalMapper("long -> Add(5) -> long");
        Assertions.assertThat(add5).isPresent();
        Object o = add5.get().get(5);
        Assertions.assertThat(o.getClass()).isEqualTo(Long.class);
    }

    @Test
    public void testToDateSpaceInstantiator() throws Exception {
        Optional<DataMapper<Date>> dataMapper = VirtData.getOptionalMapper("ToDate(1000)");
        Assertions.assertThat(dataMapper).isNotNull();
        Assertions.assertThat(dataMapper.get()).isNotNull();
        Date d1 = dataMapper.get().get(1);
        Date d2 = dataMapper.get().get(2);
        Assertions.assertThat(d2).isAfter(d1);
    }
    @Test
    public void testToDateSpaceAndCountInstantiator() throws Exception {
        Optional<DataMapper<Date>> dataMapper = VirtData.getOptionalMapper("ToDate(1000,10)");
        Assertions.assertThat(dataMapper).isNotNull();
        Assertions.assertThat(dataMapper.get()).isNotNull();
        Date d1 = dataMapper.get().get(1);
        Date d2 = dataMapper.get().get(2);
        Assertions.assertThat(d2).isAfter(d1);
    }
    @Test
    public void testToDateInstantiator() throws Exception {
        Optional<DataMapper<Date>> dataMapper = VirtData.getOptionalMapper("ToDate()");
        Assertions.assertThat(dataMapper).isNotNull();
        Assertions.assertThat(dataMapper.get()).isNotNull();
        Date d1 = dataMapper.get().get(1);
        Date d2 = dataMapper.get().get(2);
    }

    @Test
    public void testToDateBucketInstantiator() throws Exception {
        Optional<DataMapper<Date>> dataMapper = VirtData.getOptionalMapper("ToDate(1000,10000)");
        Assertions.assertThat(dataMapper).isNotNull();
        Assertions.assertThat(dataMapper.get()).isNotNull();
        Assertions.assertThat(dataMapper.get().get(1).after(new Date(1)));
    }

    @Test
    public void testRandomLineToIntInstantiator() throws Exception {
        Optional<DataMapper<Integer>> dataMapper = VirtData.getOptionalMapper("HashedLineToInt('numbers.txt')");
        Assertions.assertThat(dataMapper).isNotNull();
        Assertions.assertThat(dataMapper.get()).isNotNull();
        Assertions.assertThat(dataMapper.get().get(1)).isNotNull();
    }

}