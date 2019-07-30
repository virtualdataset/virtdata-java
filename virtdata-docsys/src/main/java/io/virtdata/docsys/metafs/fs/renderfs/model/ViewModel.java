package io.virtdata.docsys.metafs.fs.renderfs.model;

import io.virtdata.docsys.metafs.fs.renderfs.api.rendered.RenderedContent;
import io.virtdata.docsys.metafs.fs.renderfs.api.versioning.VersionData;
import io.virtdata.docsys.metafs.fs.renderfs.api.versioning.Versioned;
import io.virtdata.docsys.metafs.fs.renderfs.api.versioning.VersionedPath;
import io.virtdata.docsys.metafs.fs.renderfs.model.properties.ListView;
import io.virtdata.docsys.metafs.fs.renderfs.model.properties.PathView;
import io.virtdata.docsys.metafs.fs.renderfs.model.properties.TreeView;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ViewModel implements Versioned {

    private final VersionData versions;
    private Path target;
    private ViewModel inner;
    private RenderedContent<String> rendered;

    public ViewModel(Path sourcePath, Path targetPath) {
        this.target = targetPath;
        this.versions = new VersionData(new VersionedPath(sourcePath));

    }

    public Path getTarget() {
        return target;
    }

    public ActualFsView getFs() {
        return new ActualFsView(this.target);
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

    public void setInner(ViewModel innerRender) {
        this.inner = innerRender;
    }

    public ViewModel getInner() {
        return inner;
    }

    public void setRenderedContent(RenderedContent rendered) {
        this.rendered = rendered;
    }

    public String getRendered() {
        return rendered.get();
    }

    @Override
    public String toString() {
        return "ViewModel{" +
                "target=" + target.toString() +
                ", versions=" + versions +
                '}';
    }

    public TopicViews getTopics() {
        return new TopicViews(target);
    }

    public RenderedContent getRenderedContent() {
        return this.rendered;
    }

    @Override
    public long getVersion() {
        return 0;
    }

    @Override
    public boolean isValid() {
        return versions.isValid();
    }

}
