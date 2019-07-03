package io.virtdata.docsys.metafs.fs.renderfs.api;

import io.virtdata.docsys.metafs.fs.renderfs.fs.virtualio.VirtualFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.file.AccessMode;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("Duplicates")
/**
 * This is the main interface for allowing filesystem calls to use rendered content.
 */
public interface FileContentRenderer {

    /**
     * @return a pattern that can be used to match path names which serve as the source data of rendered files.
     */
    Pattern getSourcePattern();

    /**
     * @return a pattern that can be used to match path names which are to be dynamically rendered from source file content.
     */
    Pattern getTargetPattern();

    default boolean matchesSource(Path p) {
        return getSourcePattern().matcher(p.toString()).matches();
    }

    default boolean matchesTarget(Path p) {
        Pattern targetPattern = getTargetPattern();
        Matcher matcher = targetPattern.matcher(p.toString());
        return matcher.matches();
    }

    default boolean hasSource(Path p) {
        Path sourcePath = getSourcePath(p);
        try {
            sourcePath.getFileSystem().provider().checkAccess(sourcePath, AccessMode.READ);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    default boolean canRender(Path p) {
        if (isTemplatePath(p)) {
            return false;
        }

        return matchesTarget(p) && hasSource(p);
    }

    boolean isTemplatePath(Path p);

    /**
     * Return the matching source path, but only if the target name matches the target extension.
     *
     * @param targetName The target Path which represents the intended to be rendered
     * @return A source path, or null if the target name does not match for this renderer
     */
    Path getSourcePath(Path targetName);

    Path getRenderedTargetName(Path sourceName);

    private InputStream getInputStream(Path targetName) {
        ByteBuffer buf = getRendered(targetName);
        if (buf == null) {
            return null;
        }
        return new ByteArrayInputStream(buf.array());
    }

    ByteBuffer render(Path source, Path target, ByteBuffer input);

    private ByteBuffer getRendered(Path targetPath) {
        Path sourcePath = getSourcePath(targetPath);
        if (sourcePath != null) {
            try {
                ByteBuffer rawInput = getRawByteBuffer(sourcePath);
                ByteBuffer rendered = render(sourcePath, targetPath, rawInput);
                return rendered;
            } catch (IOException ioe) {
                throw new RuntimeException(ioe);
            } catch (Exception e) {
                throw e;
            }
        }
        return null;
    }


    private ByteBuffer getRawByteBuffer(Path sourcePath) throws IOException {
        InputStream inputStream = sourcePath.getFileSystem().provider().newInputStream(sourcePath);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        inputStream.transferTo(bos);
        return ByteBuffer.wrap(bos.toByteArray()).asReadOnlyBuffer();
    }

    default VirtualFile getVirtualFile(Path target) {
//        ByteBuffer bb = getRendered(target);
//        if (bb==null) { return null; }
        Path delegate = getSourcePath(target);
        return new VirtualFile(delegate,target,() -> getRendered(target));
    }

    List<Path> getVirtualPathsFor(Path path);
}
