package io.virtdata.libbasics.shared.from_long.to_string;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.Test;

import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

public class DirectoryLinesTest {
    private final static Logger logger = LoggerFactory.getLogger(DirectoryLinesTest.class);

    @Test
    public void testResourceDirectory() {
        logger.info("CWD For test " + DirectoryLinesTest.class.getCanonicalName() + ": '"
                + Path.of(".").toFile().getAbsoluteFile().toString()+ "'");

        Path path = Path.of("./src/test/resources/static-do-not-change");
        if (!path.toFile().exists()) {
            path = Path.of("./virtdata-lib-basics/src/test/resources/static-do-not-change");
        }
        if (!path.toFile().exists()) {
            throw new RuntimeException("Unable to find test resources from CWD: " + Path.of(".").toFile().getAbsolutePath());
        }


        DirectoryLines directoryLines = new DirectoryLines(path.toString(), ".+txt");

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