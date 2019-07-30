package io.virtdata.docsys.metafs.fs.renderfs.model.topics;

import io.virtdata.docsys.metafs.fs.renderfs.walkers.VirtTreeWalker;

import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class TopicTreeVisitor implements VirtTreeWalker.PathVisitor {

    private final List<Topic> topicRoots = new ArrayList<>();
    private final Deque<Topic> roots = new ArrayDeque<>();
    private final Deque<String> categories = new ArrayDeque<>();

    public TopicTreeVisitor(Path baseTopicPath) {
        roots.addLast(new FileTopic(baseTopicPath));
    }

    @Override
    public void visit(Path p) {
        TopicParser tp = new TopicParser(p);
        List<Topic> topicTrees = tp.getNestedHeaderTopics();
        for (Topic topicTree : topicTrees) {
            this.roots.peekLast().addSubTopic(topicTree);
        }
    }

    @Override
    public void preVisitDir(Path path) {
        categories.add(path.getName(path.getNameCount()-1).toString());
    }

    @Override
    public void postVisitDir(Path path) {
        categories.removeLast();

    }

    public List<Topic> getTopicTrees() {
        return roots.getLast().getSubTopics();
    }
}
