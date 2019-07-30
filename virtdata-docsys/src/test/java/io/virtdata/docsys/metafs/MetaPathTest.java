package io.virtdata.docsys.metafs;

import org.junit.Test;

import java.nio.file.FileStore;
import java.nio.file.Path;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

public class MetaPathTest {

    private final TestableMetaFS fs1 = new TestableMetaFS(new Path[0], new FileStore[0], Collections.emptySet(), "fs1");
    private final TestableMetaFS fs2 = new TestableMetaFS(new Path[0], new FileStore[0], Collections.emptySet(), "fs2");

    @Test
    public void testResolve() {
        Path p1 = fs1.getPath("/an/absolute/one");
        Path p2 = fs1.getPath("relative/two");
        Path r1 = p1.resolve(p2);
        assertThat(r1.toString()).isEqualTo("/an/absolute/one/relative/two");

    }

    @Test
    public void testNormalize() {
        Path p1 = fs1.getPath("/an/absolute/one/../two");
        Path p2 = p1.normalize();
        assertThat(p2.toString()).isEqualTo("/an/absolute/two");
        Path p3 = fs1.getPath("//a/basic/path");
        assertThat(p3.toString()).isEqualTo("/a/basic/path");
    }

    @Test
    public void testRelativize() {
        Path p1 = fs1.getPath("/root/one/two/three");
        Path p2 = fs1.getPath("/root/a/b/c");
        Path p3 = p1.relativize(p2);
        assertThat(p3.toString()).isEqualTo("../../../a/b/c");

    }

    @Test
    public void testComparator() {
        Path p1 = fs1.getPath("a", "B");
        Path p2 = fs1.getPath("b", "C");
        int i = p1.compareTo(p2);
        assertThat(i).isEqualTo(-1);
    }

    @Test
    public void testEndsWithPath() {
        Path p1 = fs1.getPath("a","b","c");
        Path p2 = fs1.getPath("b","c");
        boolean actual = p1.endsWith(p2);
        assertThat(actual).isTrue();
        boolean actual1 = p2.endsWith(p1);
        assertThat(actual1).isFalse();
    }

}