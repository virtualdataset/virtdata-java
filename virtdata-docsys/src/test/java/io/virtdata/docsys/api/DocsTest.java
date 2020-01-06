package io.virtdata.docsys.api;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class DocsTest {

    @Test
    public void testMergedInfoRetainsVisibility() {

        DocNameSpace a = new Docs().namespace("a").addPath(Path.of("/tmp/a"))
                .setEnabledByDefault(false).getLastNameSpace();
        DocNameSpace b = new Docs().namespace("b").addPath(Path.of("/tmp/b"))
                .setEnabledByDefault(true).getLastNameSpace();

        Docs merged = new Docs();
        merged.merge(a);
        merged.merge(b);

        Map<String, DocNameSpace> entries = new HashMap<>();
        merged.forEach(d -> entries.put(d.getNameSpace(),d));

        assertThat(entries.get("a").isEnabledByDefault()).isFalse();
        assertThat(entries.get("b").isEnabledByDefault()).isTrue();
    }


}