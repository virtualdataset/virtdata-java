package io.virtdata;

import io.virtdata.libbasics.shared.distributions.WeightedStringsFromCSV;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Test
public class IntegratedAliasMethodTests {

    @Test
    public void testCensusData() {
        WeightedStringsFromCSV surnames = new WeightedStringsFromCSV("Name", "prop100k", "data/surnames");
        String n = surnames.apply(2343);
        assertThat(n).isEqualTo("Conaway");
    }
}