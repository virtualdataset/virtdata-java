package io.virtdata;

import io.virtdata.stathelpers.aliasmethod.WeightedStrings;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Test
public class IntegratedAliasMethodTests {

    @Test
    public void testCensusData() {
        WeightedStrings surnames = new WeightedStrings("Name", "prop100k", "data/surnames");
        String n = surnames.apply(2343);
        assertThat(n).isEqualTo("Vanderploeg");
    }
}