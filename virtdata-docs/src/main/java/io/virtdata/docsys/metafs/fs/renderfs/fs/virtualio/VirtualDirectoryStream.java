package io.virtdata.docsys.metafs.fs.renderfs.fs.virtualio;

import io.virtdata.docsys.metafs.core.AugmentingIterator;
import io.virtdata.docsys.metafs.fs.renderfs.api.Renderers;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;

public class VirtualDirectoryStream implements DirectoryStream<Path> {

    private final DirectoryStream<Path> wrappedStream;
    private final NameMappingFunc func;
    private final Path of;

    public VirtualDirectoryStream(Path of, DirectoryStream<Path> wrappedStream, Renderers renderers) {
        this.of = of;
        this.wrappedStream = wrappedStream;
        this.func = new NameMappingFunc(renderers);
    }

    @Override
    public Iterator<Path> iterator() {
        AugmentingIterator<Path> pathAugmentingIterator = new AugmentingIterator<>(wrappedStream.iterator(), func);
        List<Path> paths = new ArrayList<>();
        pathAugmentingIterator.forEachRemaining(paths::add);
        Collections.sort(paths);
        return paths.iterator();
    }

    @Override
    public void close() throws IOException {
        wrappedStream.close();
    }

    private class NameMappingFunc implements Function<Path,List<Path>> {
        private Renderers renderers;

        public NameMappingFunc(Renderers renderers) {
            this.renderers = renderers;
        }

        /**
         * This method does a recursive evaluation on a list in place.
         * The purpose of this is to expand out all source to target
         * mappings across multiple renderers.
         * @param path The top level path which is considered a source path
         * @return A list of all possible virtual paths that could be rendered from this path.
         */
        @Override
        public List<Path> apply(Path path) {
            LinkedList<Path> pathlist =
                    new LinkedList<>(renderers.getVirtualPathsFor(path));

            ListIterator<Path> cursor = pathlist.listIterator();
            while (cursor.hasNext()) {
                Path next = cursor.next();
                List<Path> adding = renderers.getVirtualPathsFor(next);
                adding.forEach(e -> {
                    cursor.add(e);
                    cursor.previous();
                });
            }

            if (pathlist.size()==0) {
                return null;
            }
            return pathlist;
        }
    }
}
