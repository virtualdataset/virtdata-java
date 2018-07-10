package io.virtdata.libraryimpl;

import io.virtdata.api.DataMapper;
import io.virtdata.core.VirtData;
import org.testng.annotations.Test;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class BasicDataMappersTest {


    @Test
    public void testGetDataMapper() throws Exception {
        Optional<DataMapper<Object>> dataMapper = VirtData.getMapper("StaticStringMapper('foo')");
        assertThat(dataMapper.isPresent()).isTrue();
        assertThat(dataMapper.get().get(5)).isEqualTo("foo");
    }

    @Test
    public void testMultipleChoiceLong() {
        Optional<DataMapper<Object>> add5 = VirtData.getMapper("long -> Add(5) -> long");
        assertThat(add5).isPresent();
        Object o = add5.get().get(5);
        assertThat(o.getClass()).isEqualTo(Long.class);
    }

    @Test
    public void testToDateSpaceInstantiator() throws Exception {
        Optional<DataMapper<Date>> dataMapper = VirtData.getMapper("ToDate(1000)");
        assertThat(dataMapper).isNotNull();
        assertThat(dataMapper.get()).isNotNull();
        Date d1 = dataMapper.get().get(1);
        Date d2 = dataMapper.get().get(2);
        assertThat(d2).isAfter(d1);
    }
    @Test
    public void testToDateSpaceAndCountInstantiator() throws Exception {
        Optional<DataMapper<Date>> dataMapper = VirtData.getMapper("ToDate(1000,10)");
        assertThat(dataMapper).isNotNull();
        assertThat(dataMapper.get()).isNotNull();
        Date d1 = dataMapper.get().get(1);
        Date d2 = dataMapper.get().get(2);
        assertThat(d2).isAfter(d1);
    }
    @Test
    public void testToDateInstantiator() throws Exception {
        Optional<DataMapper<Date>> dataMapper = VirtData.getMapper("ToDate()");
        assertThat(dataMapper).isNotNull();
        assertThat(dataMapper.get()).isNotNull();
        Date d1 = dataMapper.get().get(1);
        Date d2 = dataMapper.get().get(2);
    }

    @Test
    public void testToDateBucketInstantiator() throws Exception {
        Optional<DataMapper<Date>> dataMapper = VirtData.getMapper("ToDate(1000,10000)");
        assertThat(dataMapper).isNotNull();
        assertThat(dataMapper.get()).isNotNull();
        assertThat(dataMapper.get().get(1).after(new Date(1)));
    }

    @Test
    public void testRandomLineToIntInstantiator() throws Exception {
        Optional<DataMapper<Integer>> dataMapper = VirtData.getMapper("HashedLineToInt('data/numbers.txt')");
        assertThat(dataMapper).isNotNull();
        assertThat(dataMapper.get()).isNotNull();
        assertThat(dataMapper.get().get(1)).isNotNull();
    }

//    @Test
//    public void testArgConversions() {
//        try {
//            Object[] args = new Object[]{ new Integer(5)};
//            Constructor<Mod> ctors = Mod.class.getDeclaredConstructor(new Class[]{Long.TYPE});
//            System.out.println(ctors.toString());
//            Mod mod = ctors.newInstance(args);
//            assertThat(mod).isNotNull();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }

}