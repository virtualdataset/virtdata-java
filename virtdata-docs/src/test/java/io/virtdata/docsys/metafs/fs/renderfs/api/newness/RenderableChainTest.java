package io.virtdata.docsys.metafs.fs.renderfs.api.newness;

import io.virtdata.docsys.metafs.fs.renderfs.api.*;
import io.virtdata.docsys.metafs.fs.renderfs.model.TargetPathView;
import org.testng.annotations.Test;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;

@Test
public class RenderableChainTest {

    public void testBasicRenderer() {
        Supplier<ByteBuffer> rawtemplate = getSupplier("MyTest ##content## MyTest");
        TemplateCompiler compiler = getTemplateCompiler("##content##", "compiled");

        RenderableEntry r1 = new RenderableEntry(rawtemplate,compiler);
        Path path = Path.of("/tmp/nothing");
        ByteBuffer result = r1.apply(new TargetPathView(path, 23));
        String resultString = new String(result.array(), StandardCharsets.UTF_8);
        assertThat(resultString).isEqualTo("MyTest compiled MyTest");
    }

    public void testChainedRenderer() {
        Supplier<ByteBuffer> rawtemplate = getSupplier("MyTest ##content## MyTest");
        TemplateCompiler compiler = getTemplateCompiler("##content##", "compiled");
        TemplateCompiler compiler2 = getTemplateCompiler("compiled","COMPILED");

        Renderable r1 = new RenderableChain(rawtemplate, compiler, compiler2);

        Path path = Path.of("/tmp/nothing");
        ByteBuffer bb1 = r1.apply(new TargetPathView(path, 23));
        String resultString = new String(bb1.array(), StandardCharsets.UTF_8);
        assertThat(resultString).isEqualTo("MyTest COMPILED MyTest");
        ByteBuffer bb2 = r1.apply(new TargetPathView(path, 23));

        assertThat(bb2==bb1);
        ByteBuffer bb3 = r1.apply(new TargetPathView(path, 24));
        assertThat(bb3!=bb1);


    }

    private Supplier<ByteBuffer> getSupplier(String content) {
        return new Supplier<ByteBuffer>() {
            @Override
            public ByteBuffer get() {
                return ByteBuffer.wrap(content.getBytes(StandardCharsets.UTF_8));
            }
        };
    }

    private TemplateCompiler getTemplateCompiler(final String toReplace, final String replacement) {
        return new TemplateCompiler() {
            @Override
            public Renderer apply(ByteBuffer byteBuffer) {
                return new Renderer() {
                    @Override
                    public ByteBuffer apply(TargetPathView targetPathView) {
                        String string = new String(byteBuffer.array(), StandardCharsets.UTF_8);
                        String result = string.replaceAll(toReplace, replacement);
                        return ByteBuffer.wrap(result.getBytes(StandardCharsets.UTF_8));
                    }
                };
            }
        };
    }

}