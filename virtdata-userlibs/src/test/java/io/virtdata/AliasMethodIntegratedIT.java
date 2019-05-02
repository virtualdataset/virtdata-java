package io.virtdata;


import io.virtdata.stathelpers.aliasmethod.WeightedStrings;
import io.virtdata.util.ModuleInfo;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AliasMethodIntegratedIT {

    @BeforeClass
    public static void showModuleInfo() {
        ModuleInfo.logModuleNamesDebug(AliasMethodIntegratedIT.class);
    }

    @Test
    public void testCensusData() {
        WeightedStrings surnames = new WeightedStrings("Name", "prop100k", "surnames");
        String n = surnames.apply(2343);
        assertThat(n).isEqualTo("Conaway");
    }
}