package io.virtdata.docsys.metafs.fs.renderfs.api;

import io.virtdata.docsys.metafs.fs.renderfs.api.rendered.RenderedContent;
import io.virtdata.docsys.metafs.fs.renderfs.api.rendering.RenderingScope;
import io.virtdata.docsys.metafs.fs.renderfs.api.rendering.TemplateCompiler;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.AccessMode;
import java.nio.file.Path;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("Duplicates")
public class FileRenderer extends FileContentRenderer {

    private final String sourceExtension;
    private final String targetExtension;
    private final Pattern sourceNamePattern;
    private final Pattern targetNamePattern;
    private final boolean isCaseSensitive;

    private TemplateCompiler compiler;

    /**
     * Create a file renderer from a source extension to a target extension, which will yield the
     * virtual contents of the target file by applying a set of renderers to the source file data.
     *
     * @param fromext  The extension of the source (actual) file, including the dot and extension name.
     * @param toext    The extension of the target (virtual) file, including the dot and extension name.
     * @param cased    Whether or not to do case-sensitive matching against the source and target extensions.
     * @param compiler A lookup function which can create a renderer for a specific path as needed.
     */
    public FileRenderer(String fromext, String toext, boolean cased, TemplateCompiler compiler) {
        this.compiler = compiler;

        if (!fromext.startsWith(".")) {
            throw new InvalidParameterException("You must provide a source extension in '.xyz' form.");
        }
        if (!toext.startsWith(".")) {
            throw new InvalidParameterException("You must provide a target extension in '.xyz' form.");
        }
        this.isCaseSensitive = cased;
        this.sourceExtension = fromext;
        this.sourceNamePattern = toNamePattern(fromext);
        this.targetExtension = toext;
        this.targetNamePattern = toNamePattern(toext);

    }

    private Pattern toNamePattern(String fileExtension) {
        Pattern.compile(fileExtension);
        StringBuilder sb = new StringBuilder("(?<basepath>.+)(?<extension>");
        if (this.isCaseSensitive) {
            sb.append(Pattern.quote(fileExtension));
        } else {
            for (int i = 0; i < fileExtension.length(); i++) {
                String c = fileExtension.substring(i, i + 1);
                if (c.toUpperCase().equals(c.toLowerCase())) {
                    sb.append(Pattern.quote(c));
                } else {
                    sb.append("[").append(c.toLowerCase()).append(c.toUpperCase()).append("]");
                }
            }
            sb.append(")");
        }
        String pattern = sb.toString().replaceAll("\\\\E\\\\Q", "");
        return Pattern.compile(pattern);

    }

    @Override
    public List<Path> getVirtualPathsFor(Path path) {
        ArrayList<Path> vpaths = new ArrayList<>();
        if (this.matchesSource(path) && !isWrapperPath(path)) {
            Path targetPaths = this.getRenderedTargetName(path);
            vpaths.add(targetPaths);
        }
        return vpaths;
    }


    @Override
    public Pattern getSourcePattern() {
        return sourceNamePattern;
    }

    @Override
    public Pattern getTargetPattern() {
        return targetNamePattern;
    }

    @Override
    public boolean isWrapperPath(Path p) {
        String filename = p.getName(p.getNameCount()-1).toString();
        if (filename.startsWith("__.") || filename.startsWith("_.")) {
            return true;
        }
        return false;
    }

    public String getSourceExtension() {
        return sourceExtension;
    }

    public String getTargetExtension() {
        return targetExtension;
    }

    @Override
    public Path getSourcePath(Path targetName) {
        Matcher matcher = targetNamePattern.matcher(targetName.toString());
        if (matcher.matches()) {
            String basepath = matcher.group("basepath");
            String extension = matcher.group("extension");
            if (basepath == null || extension == null) {
                throw new RuntimeException(
                        "Unable to extract named fields 'basepath' or 'extension' from target " +
                                "name '" + targetName + "' with pattern '" + targetNamePattern + "'");
            }
            return targetName.getFileSystem().getPath(basepath + sourceExtension);

        }
        return null;

    }

    @Override
    public Path getRenderedTargetName(Path sourceName) {
        Matcher matcher = sourceNamePattern.matcher(sourceName.toString());
        if (matcher.matches()) {
            String basepath = matcher.group("basepath");
            String extension = matcher.group("extension");
            if (basepath == null || extension == null) {
                throw new RuntimeException(
                        "Unable to extract named fields 'basepath' or 'extension' from source " +
                                "name '" + sourceName + "' with pattern '" + sourceNamePattern + "'");
            }

            return sourceName.getFileSystem().getPath(basepath + targetExtension);
        }
        throw new RuntimeException("Unable to match source name '" + sourceName + "' with pattern '" + sourceNamePattern + "'");

    }

    @Override
    public synchronized RenderedContent render(Path sourcePath, Path targetPath, ByteBuffer byteBuffer) {
        RenderingScope scope = new RenderingScope(sourcePath, targetPath, compiler);
        if (!targetPath.toString().endsWith(".mdf")) {
            for (Path template : getTemplates(sourcePath)) {
                RenderingScope outer = new RenderingScope(template, template, compiler);
                scope = outer.wrap(scope);
            }
        }
        RenderedContent rendered = scope.getRendered();
        return rendered;
    }

    private LinkedList<Path> getTemplates(Path sourcePath) {
        LinkedList<Path> chain = new LinkedList<>();
        sourcePath.normalize();
        Path directoryPath = sourcePath.getParent();
        String[] parts = sourcePath.toString().split("\\.");
        String extension = parts[parts.length - 1];

        try {
            Path localTmpl = directoryPath.resolve("_." + extension);
            localTmpl.getFileSystem().provider().checkAccess(localTmpl, AccessMode.READ);
            chain.addLast(localTmpl);
        } catch (IOException ignored) {
        }

        while (directoryPath != null) {
            try {
                Path localTmpl = directoryPath.resolve("__." + extension);
                directoryPath = directoryPath.getParent();
                localTmpl.getFileSystem().provider().checkAccess(localTmpl, AccessMode.READ);
                chain.addLast(localTmpl);
            } catch (IOException ignored) {
            }
        }
        return chain;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb
                .append(compiler.toString())
                .append(this.sourceExtension)
                .append("→")
                .append(this.targetExtension)
                .append(")");
        return sb.toString();
    }

}
