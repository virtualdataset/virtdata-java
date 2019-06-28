package io.virtdata.docsys.metafs.fs.renderfs.model.properties;

import io.virtdata.docsys.metafs.fs.renderfs.api.MarkdownStringer;
import io.virtdata.docsys.metafs.fs.renderfs.model.ViewModel;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

public class TreeView implements MarkdownStringer {

    private final ViewModel viewModel;

    public TreeView(ViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public String asMarkdown() {
        return "implement me";
    }

    private static class MarkdownVisitor implements FileVisitor<Path> {

        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
            return null;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            return null;
        }

        @Override
        public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
            return null;
        }

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
            return null;
        }
    }
}
