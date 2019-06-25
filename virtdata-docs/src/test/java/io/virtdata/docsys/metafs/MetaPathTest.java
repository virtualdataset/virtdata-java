package io.virtdata.docsys.metafs;

import org.testng.annotations.Test;

import java.nio.file.FileStore;
import java.nio.file.Path;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@Test
public class MetaPathTest {

    private final TestableMetaFS fs1 = new TestableMetaFS(new Path[0], new FileStore[0], Collections.emptySet(), "fs1");
    private final TestableMetaFS fs2 = new TestableMetaFS(new Path[0], new FileStore[0], Collections.emptySet(), "fs2");

    public void testResolve() {
        Path p1 = fs1.getPath("/an/absolute/one");
        Path p2 = fs1.getPath("relative/two");
        Path r1 = p1.resolve(p2);
        assertThat(r1.toString()).isEqualTo("/an/absolute/one/relative/two");

    }

    public void testNormalize() {
        Path p1 = fs1.getPath("/an/absolute/one/../two");
        Path p2 = p1.normalize();
        assertThat(p2.toString()).isEqualTo("/an/absolute/two");
        Path p3 = fs1.getPath("//a/basic/path");
        assertThat(p3.toString()).isEqualTo("/a/basic/path");
    }

    public void testRelativize() {
        Path p1 = fs1.getPath("/root/one/two/three");
        Path p2 = fs1.getPath("/root/a/b/c");
        Path p3 = p1.relativize(p2);
        assertThat(p3.toString()).isEqualTo("../../../a/b/c");

    }

}