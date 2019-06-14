package io.virtdata.docsys.metafs.fs.renderfs.api;

import io.virtdata.docsys.metafs.fs.renderfs.model.TargetPathView;

import java.nio.ByteBuffer;
import java.util.function.Supplier;

public class RenderableEntry implements Renderable {

    private final Supplier<ByteBuffer> bufferSource;
    private final RenderableEntry upstream;
    private long renderedVersion;
    private ByteBuffer input;
    private TemplateCompiler compiler;
    private Renderer renderer;
    private ByteBuffer output;

    /**
     * Create a renderable entry which receives its template image from a direct
     * byte buffer source. When this entry is accessed, its validity (version) will
     * be checked against the version of the viewed path. If it needs to be re-rendered,
     * then the template image will be re-read from the buffer source.
     *
     * @param bufferSource a direct source of byte buffer data
     * @param compiler     the template compiler for this entry
     */
    public RenderableEntry(Supplier<ByteBuffer> bufferSource, TemplateCompiler compiler) {
        this.compiler = compiler;
        this.bufferSource = bufferSource;
        this.upstream = null;
    }

    /**
     * Create a renderable entry which receives its template image from another renderable
     * entry. When this entry's version is invalid with respect to the upstream entry's
     * version, then this entry will be recomputed on access by first asking for an updated
     * template image from upstream.
     *
     * @param upstream The upstream renderable entry
     * @param compiler The template compiler for this entry
     */
    public RenderableEntry(RenderableEntry upstream, TemplateCompiler compiler) {
        this.upstream = upstream;
        this.compiler = compiler;
        this.bufferSource = null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getVersion() {
        return renderedVersion;
    }

    /**
     * Create a rendered document image of some type (it might not always be html, for example)
     * by doing the following:
     * <ol>
     * <li>Read the template image unless it is already valid.</li>
     * <li>Construct a new renderer from the template image.</li>
     * <li>Construct a new document image with the renderer.</li>
     * <li>Update the version.</li>
     * <li>Return the document image to the reader.</li>
     * </ol>
     *
     * These steps are only taken if needed. When the version matches the previously read
     * version of this element, then only the last step is taken.
     *
     * @param targetPathView
     * @return
     */
    @Override
    public ByteBuffer apply(TargetPathView targetPathView) {

        if (output != null && isValidFor(targetPathView)) {
            return output;
        }
        // Get the raw template image
        input = (bufferSource != null) ? bufferSource.get() : upstream.apply(targetPathView);

        // Create a compiled template of some type
        renderer = compiler.apply(input);

        // Apply the view context to the compiled template
        output = renderer.apply(targetPathView);

        // Update the cache info
        renderedVersion = targetPathView.getVersion();
        return output;
    }

    @Override
    public String toString() {
        return this.compiler.getClass().getSimpleName() + ":" +
                "input=" +
                ((input == null) ? "NULL" : input.capacity()) +
                " output=" +
                ((output == null) ? "NULL" : output.capacity());
    }
}
