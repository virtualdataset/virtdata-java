package io.virtdata.docsys.metafs.fs.layerfs;

import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class LayerFSTest {

    @Test
    public void testLayeringAB() throws IOException {

        LayerFS fs1 = new LayerFS("testlayers")
                .addLayer(Path.of("src/test/resources/testsite1"),"testsite1")
                .addLayer(Path.of("src/test/resources/testsite2"),"testsite2");

        Path basics = fs1.getPath("/basics/section1/topic1.md");
        FileSystem basics1fs = basics.getFileSystem();
        Path advanced = fs1.getPath("/advanced/advanced_topic1.md");
        FileSystem advancedfs = advanced.getFileSystem();

        byte[] bytes = Files.readAllBytes(basics);
        System.out.println(new String(bytes, StandardCharsets.UTF_8));

        List<String> lines = Files.readAllLines(advanced);
        System.out.println(lines);
    }

//    public void testBasicRendering() throws IOException {
//        RenderFS renderFS = new RenderFS(Path.of("src/test/resources/testsite1"));
//        renderFS.addRenderers(new MarkdownRenderer("md","html"));
//        byte[] bytes1 = Files.readAllBytes(renderFS.getTargetPath("/basics/section1/topic1.md"));
//        System.out.println(new String(bytes1, StandardCharsets.UTF_8));
//        byte[] bytes2 = Files.readAllBytes(renderFS.getTargetPath("/basics/section1/topic1.html"));
//        System.out.println(new String(bytes2, StandardCharsets.UTF_8));
//    }

}