package io.virtdata.docsys.metafs.fs.renderfs.model;

import io.virtdata.docsys.metafs.fs.renderfs.api.MarkdownStringer;
import io.virtdata.docsys.metafs.fs.renderfs.api.RendererIO;
import io.virtdata.docsys.metafs.fs.renderfs.api.rendered.RenderedContent;
import io.virtdata.docsys.metafs.fs.renderfs.api.rendering.Versioned;
import io.virtdata.docsys.metafs.fs.renderfs.model.properties.ListView;
import io.virtdata.docsys.metafs.fs.renderfs.model.properties.PathView;
import io.virtdata.docsys.metafs.fs.renderfs.model.properties.TreeView;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ViewModel implements Versioned, MarkdownStringer {

    private Path target;
    private long version;
    private ViewModel inner;
    private RenderedContent rendered;

    public ViewModel(Path sourcePath, Path targetPath) {
        this.version = RendererIO.mtimeFor(sourcePath);
        this.target = targetPath;
    }

    public Path getTarget() {
        return target;
    }

    public ActualFsView getFs() {
        return new ActualFsView(this.target, this.version);
    }

    public List<Path> getBreadcrumbs() {
        ArrayList<Path> paths = new ArrayList<>();
        target.iterator().forEachRemaining(paths::add);
        return paths;
    }


    public PathView getPath() {
        return new PathView(target);
    }

    public ListView<Path> getPaths() {
        List<Path> nontemplatefiles = getFs().getPaths().stream().filter(
                p -> {
                    if (p.getFileName().toString().matches(".*?\\._.*+")) {
                        return false;
                    }
                    return true;
                }
        ).collect(Collectors.toList());
        return new ListView<>(nontemplatefiles);
    }

    public TreeView getFileTree() {
        return new TreeView(this);
    }

//    public Mustache.Lambda getMarkdown() {
//        return new MarkdownLambda(this);
//    }


    @Override
    public long getVersion() {
        return version;
    }

    @Override
    public String asMarkdown() {
        return "```\n" + toString() + "\n```\n";
    }

    public void setInner(ViewModel innerRender) {
        this.inner = innerRender;
    }

    public ViewModel getInner() {
        return inner;
    }

    public void setRendered(RenderedContent rendered) {
        this.rendered = rendered;
    }

    public RenderedContent getRendered() {
        return rendered;
    }

    @Override
    public String toString() {
        return "ViewModel{" +
                "target=" + target.toString() +
                ", version=" + version +
                '}';
    }

    public List<Topic> getTopics() {
        return TopicFinder.getTopics(target);

    }

}
