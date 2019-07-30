package io.virtdata.docsys.metafs.fs.renderfs.model;

import io.virtdata.docsys.metafs.fs.renderfs.model.properties.ListView;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ActualFsView {
    private final Path path;

    public ActualFsView(Path path) {
        this.path = path;
    }

    public ListView<Path> getPaths() {
        List<Path> paths = new ArrayList<>();
        Path dirPath = path.getParent();
        try {
            DirectoryStream<Path> dir =
                    dirPath.getFileSystem().provider()
                            .newDirectoryStream(dirPath, AcceptAllFiles);
            dir.iterator().forEachRemaining(paths::add);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new ListView<>(paths);
    }

    private final static DirectoryStream.Filter<Path> AcceptAllFiles = new DirectoryStream.Filter<Path>() {
        @Override
        public boolean accept(Path entry) throws IOException {
            return true;
        }
    };




}
