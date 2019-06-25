package io.virtdata.docsys.metafs.fs.renderfs.model;

import com.samskivert.mustache.Mustache;
import io.virtdata.docsys.metafs.fs.renderfs.api.MarkdownStringer;
import io.virtdata.docsys.metafs.fs.renderfs.api.Versioned;
import io.virtdata.docsys.metafs.fs.renderfs.model.properties.ListView;
import io.virtdata.docsys.metafs.fs.renderfs.model.properties.PathView;
import io.virtdata.docsys.metafs.fs.renderfs.model.properties.TreeView;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TargetPathView implements Versioned, MarkdownStringer {
    private TargetPathView parent;
    private Path path;
    private long version;

    public TargetPathView(Path path, long version) {
        this.path = path;
        this.version = version;
    }

    public ActualFsView getFs() {
        return new ActualFsView(this.path, this.version);
    }

    public TargetPathView setParent(TargetPathView parent) {
        this.parent = parent;
        return this;
    }

    public TargetPathView getParent() {
        return parent;
    }

    public List<Path> getBreadcrumbs() {
        ArrayList<Path> paths = new ArrayList<>();
        path.iterator().forEachRemaining(paths::add);
        return paths;
    }


    public PathView getPath() {
        return new PathView(path);
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

    public Mustache.Lambda getMarkdown() {
        return new MarkdownLambda(this);
    }


    @Override
    public long getVersion() {
        return version;
    }

    @Override
    public String toString() {
        return "TargetPathView{" +
                "path=" + path +
                ", version=" + version +
                '}';
    }

    @Override
    public String asMarkdown() {
        return "```\n" + toString() + "\n```\n";
    }
}
