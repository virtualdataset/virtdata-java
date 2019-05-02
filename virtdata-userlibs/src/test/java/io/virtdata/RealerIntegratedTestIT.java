package io.virtdata;

import io.virtdata.api.DataMapper;
import io.virtdata.core.VirtData;
import io.virtdata.util.ModuleInfo;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class RealerIntegratedTestIT {

    @BeforeClass
    public static void showModuleInfo() {
        ModuleInfo.logModuleNamesDebug(RealerIntegratedTestIT.class);
    }

    @Test
    public void testLastNames() {
        DataMapper mapper = VirtData.getOptionalMapper("LastNames()").orElse(null);
        assertThat(mapper).isNotNull();
        assertThat(mapper.get(0L)).isEqualTo("Miracle");
    }

    @Test
    public void testFirstNames() {
        DataMapper mapper = VirtData.getOptionalMapper("FirstNames()").orElse(null);
        assertThat(mapper).isNotNull();
        assertThat(mapper.get(0L)).isEqualTo("Norman");
    }

    @Test
    public void testFullNames() {
        DataMapper mapper = VirtData.getOptionalMapper("FullNames()").orElse(null);
        assertThat(mapper).isNotNull();
        assertThat(mapper.get(0L)).isEqualTo("Norman Wolf");
    }

}
