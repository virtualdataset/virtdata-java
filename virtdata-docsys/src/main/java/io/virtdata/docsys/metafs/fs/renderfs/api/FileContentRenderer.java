package io.virtdata.docsys.metafs.fs.renderfs.api;

import io.virtdata.docsys.metafs.fs.renderfs.api.rendered.RenderedContent;
import io.virtdata.docsys.metafs.fs.renderfs.fs.virtualio.VirtualFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.file.AccessMode;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("Duplicates")
/**
 * This is the main interface for allowing filesystem calls to use rendered content.
 */
public abstract class FileContentRenderer {
    private final static Logger logger = LoggerFactory.getLogger(FileContentRenderer.class);

    /**
     * @return a pattern that can be used to match path names which serve as the source data of rendered files.
     */
    abstract Pattern getSourcePattern();

    /**
     * @return a pattern that can be used to match path names which are to be dynamically rendered from source file content.
     */
    abstract Pattern getTargetPattern();

    public boolean matchesSource(Path p) {
        Pattern sourcePattern = getSourcePattern();
        Matcher matcher = sourcePattern.matcher(p.toString());
        boolean matches = matcher.matches();
        return matches;
    }

    public boolean matchesTarget(Path p) {
        Pattern targetPattern = getTargetPattern();
        Matcher matcher = targetPattern.matcher(p.toString());
        return matcher.matches();
    }

    public boolean hasSource(Path p) {
        Path sourcePath = getSourcePath(p);
        try {
            sourcePath.getFileSystem().provider().checkAccess(sourcePath, AccessMode.READ);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean canRender(Path p) {
        if (isWrapperPath(p)) {
            return false;
        }

        boolean matchesTarget = matchesTarget(p);
        boolean hasSource = hasSource(p);
        boolean canRender = matchesTarget && hasSource;
        if (canRender) {
            logger.trace("CANRENDER " + p + " (" + this + ")");
        }
        return canRender;
    }

    abstract boolean isWrapperPath(Path p);

    /**
     * Return the matching source path, but only if the target name matches the target extension.
     *
     * @param targetName The target Path which represents the intended to be rendered
     * @return A source path, or null if the target name does not match for this renderer
     */
    abstract Path getSourcePath(Path targetName);

    abstract Path getRenderedTargetName(Path sourceName);

    abstract RenderedContent render(Path source, Path target, Supplier<ByteBuffer> input);

    private RenderedContent getRendered(Path targetPath) {
        Path sourcePath = getSourcePath(targetPath);
        if (sourcePath != null) {
            try {
                ByteBufferSupplier inputSupplier = new ByteBufferSupplier(sourcePath);
                RenderedContent rendered = render(sourcePath, targetPath, inputSupplier);
                return rendered;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    private final class ByteBufferSupplier implements Supplier<ByteBuffer> {
        private Path sourcePath;
        public ByteBufferSupplier(Path sourcePath) {
            this.sourcePath = sourcePath;
        }
        @Override
        public ByteBuffer get() {
            try {

            InputStream inputStream = sourcePath.getFileSystem().provider().newInputStream(sourcePath);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            inputStream.transferTo(bos);
            return ByteBuffer.wrap(bos.toByteArray()).asReadOnlyBuffer();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }


    public VirtualFile getVirtualFile(Path target) {
//        ByteBuffer bb = getRendered(target);
//        if (bb==null) { return null; }
        Path delegate = getSourcePath(target);
        return new VirtualFile(delegate,target,getRendered(target));
    }

    abstract List<Path> getVirtualPathsFor(Path path);
}
