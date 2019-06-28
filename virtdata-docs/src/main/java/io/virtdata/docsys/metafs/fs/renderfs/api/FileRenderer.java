package io.virtdata.docsys.metafs.fs.renderfs.api;

import io.virtdata.docsys.metafs.fs.renderfs.api.rendered.RenderedContent;
import io.virtdata.docsys.metafs.fs.renderfs.api.rendering.RenderingScope;
import io.virtdata.docsys.metafs.fs.renderfs.api.rendering.TemplateCompiler;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.AccessMode;
import java.nio.file.Path;
import java.security.InvalidParameterException;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("Duplicates")
public class FileRenderer implements FileContentRenderer {

    private final String sourceExtension;
    private final String targetExtension;
    private final Pattern sourceNamePattern;
    private final Pattern targetNamePattern;
    private final boolean isCaseSensitive;

    private final ConcurrentHashMap<String, Renderable> renderables = new ConcurrentHashMap<>();
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
    public Pattern getSourcePattern() {
        return sourceNamePattern;
    }

    @Override
    public Pattern getTargetPattern() {
        return targetNamePattern;
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
    public String getTargetSuffix() {
        return this.targetExtension;
    }

    @Override
    public synchronized ByteBuffer render(Path sourcePath, Path targetPath, ByteBuffer byteBuffer) {
        RenderingScope scope = new RenderingScope(sourcePath, targetPath, compiler);
        LinkedList<Path> templates = getTemplates(sourcePath);
        for (Path template : templates) {
            RenderingScope outer = new RenderingScope(template, targetPath, compiler);
            scope = scope.addParent(outer);
        }
        RenderedContent rendered = scope.getRendered();

        byte[] bytes = rendered.get().getBytes(StandardCharsets.UTF_8);
        return ByteBuffer.wrap(bytes).asReadOnlyBuffer();
//
//        LinkedList<Path> renderLayers = getTemplates(sourcePath);
//
//        Supplier<ByteBuffer> compositeTemplateProvider = new CompositeTemplate(renderLayers);
//
//        Renderable renderable = renderables.get(targetPath.toString());
//        if (renderable == null) {
//            if (compilers.length==1) {
//                renderable = new RenderableEntry(() -> RendererIO.readString(sourcePath), compilers[0]);
//            } else {
//                renderable = new RenderableChain(() -> RendererIO.readBuffer(sourcePath), compilers);
//            }
//            renderables.put(targetPath.toString(), renderable);
//        }
//        String rendered = renderable.apply(new ViewModel(targetPath, lastModified));
//        return ByteBuffer.wrap(rendered.getBytes(StandardCharsets.UTF_8)).asReadOnlyBuffer();
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
            chain.addFirst(localTmpl);
        } catch (IOException ignored) {
        }

        while (directoryPath != null) {
            try {
                Path localTmpl = directoryPath.resolve("__." + extension);
                localTmpl.getFileSystem().provider().checkAccess(localTmpl, AccessMode.READ);
                chain.addFirst(localTmpl);
                directoryPath = directoryPath.getParent();
            } catch (IOException ignored) {
                break;
            }
        }
        return chain;
    }
//
//
//    private LinkedList<Path> getRenderLayers(Path sourcePath) {
//        sourcePath = sourcePath.normalize();
//        LinkedList<Path> renderchain = new LinkedList<>();
//        renderchain.add(sourcePath);
//        Path directoryPath = sourcePath.getParent();
//
//        while (directoryPath != null) {
//            try {
//                Path candidate = directoryPath.resolve("_template" + sourceExtension);
//                candidate.getFileSystem().provider().checkAccess(candidate, AccessMode.READ);
//                renderchain.addFirst(candidate);
//                directoryPath = directoryPath.getParent();
//            } catch (IOException e) {
//                break;
//            }
//        }
//        return renderchain;
//    }

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
