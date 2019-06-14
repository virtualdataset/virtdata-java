package io.virtdata.docsys.metafs.fs.renderfs.api;

import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.function.BinaryOperator;
import java.util.function.Supplier;

public class CompositeTemplate implements Supplier<ByteBuffer> {
    private final static Mustache.Compiler layerCompositor = Mustache.compiler()
            .withFormatter(new LayerCompositorFormatter());

    private BinaryOperator<ByteBuffer> combiner = (byteBuffer, byteBuffer2) -> {
        Template compiled = layerCompositor.compile(new String(byteBuffer.array(), StandardCharsets.UTF_8));
        String result = compiled.execute(byteBuffer2);
        return ByteBuffer.wrap(result.getBytes(StandardCharsets.UTF_8));
    };

    private LinkedList<Path> renderLayers;


    public CompositeTemplate(LinkedList<Path> renderLayers) {
        this.renderLayers = renderLayers;
    }

    @Override
    public ByteBuffer get() {

        ByteBuffer combinedLayers = renderLayers.stream().map(this::getByteBuffer).reduce(combiner)
                .orElseThrow();
        return combinedLayers;
    }

    private static class LayerCompositorFormatter implements Mustache.Formatter {
        @Override
        public String format(Object value) {
            if (value instanceof ByteBuffer) {
                return new String(((ByteBuffer)value).array(), StandardCharsets.UTF_8);
            } else {
                return "unrecognized object type in layer compositor: (" +
                        value.getClass().getSimpleName() +
                        ")" +
                        String.valueOf(value);
            }
        }
    }

    private ByteBuffer getByteBuffer(Path sourcePath) {
        InputStream inputStream = null;
        try {
            inputStream = sourcePath.getFileSystem().provider().newInputStream(sourcePath);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            inputStream.transferTo(bos);
            return ByteBuffer.wrap(bos.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
