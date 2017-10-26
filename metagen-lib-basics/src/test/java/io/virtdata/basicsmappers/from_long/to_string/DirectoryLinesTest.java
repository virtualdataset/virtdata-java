package io.virtdata.basicsmappers.from_long.to_string;

import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Test
public class DirectoryLinesTest {

    @Test
    public void testResourceDirectory() {
        DirectoryLines directoryLines = new DirectoryLines("./src/test/resources/static-do-not-change", ".+txt");
        String s;
        assertThat(directoryLines.apply(0)).isEqualTo("data1.txt-line1");
        assertThat(directoryLines.apply(0)).isEqualTo("data1.txt-line2");
        assertThat(directoryLines.apply(0)).isEqualTo("data1.txt-line3");
        assertThat(directoryLines.apply(0)).isEqualTo("data1.txt-line4");
        assertThat(directoryLines.apply(0)).isEqualTo("data1.txt-line5");
        assertThat(directoryLines.apply(0)).isEqualTo("data2.txt-line1");
        assertThat(directoryLines.apply(0)).isEqualTo("data2.txt-line2");
        assertThat(directoryLines.apply(0)).isEqualTo("data2.txt-line3");
        assertThat(directoryLines.apply(0)).isEqualTo("data2.txt-line4");
        assertThat(directoryLines.apply(0)).isEqualTo("data2.txt-line5");
        assertThat(directoryLines.apply(0)).isEqualTo("data1.txt-line1");
    }

}