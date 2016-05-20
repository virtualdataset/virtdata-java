package com.metawiring.gen.libraryimpl;

import com.metawiring.gen.metagenapi.Generator;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class BasicGeneratorsTest {

    @Test
    public void testGetLibraryName() throws Exception {
        String libraryName = new BasicGenerators().getLibraryName();
        assertThat(libraryName).isEqualTo("basics");

    }

    @Test
    public void testGetGenerator() throws Exception {
        Optional<Generator<String>> generator = new BasicGenerators().getGenerator("StaticStringGenerator:foo");
        assertThat(generator.isPresent()).isTrue();
        assertThat(generator.get().get(5)).isEqualTo("foo");
    }

    @Test
    public void testGetGeneratorNames() throws Exception {
        BasicGenerators basics = new BasicGenerators();
        List<String> gnames = basics.getGeneratorNames();
        assertThat(gnames.size()>5);
        assertThat(gnames.contains("StaticStringGenerator")).isTrue();

    }

}