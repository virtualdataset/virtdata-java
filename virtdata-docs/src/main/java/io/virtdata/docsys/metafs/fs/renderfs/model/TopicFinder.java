package io.virtdata.docsys.metafs.fs.renderfs.model;

import io.virtdata.docsys.metafs.fs.renderfs.walkers.VirtTreeWalker;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class TopicFinder {
    public static List<Topic> getTopics(Path baseTopicPath) {

        Predicate<String> f = s -> s.endsWith(".md") && !s.equals(baseTopicPath.toString());
        FV v = new FV();
        VirtTreeWalker.walk(baseTopicPath, v, f);
        List<Topic> topics = v.getTopics();
        return topics;
    }

    private final static class FV implements VirtTreeWalker.PathVisitor {
        private List<Topic> topics = new ArrayList<>();

        @Override
        public void visit(Path p) {
            TopicParser parser = new TopicParser(p);
            topics.add(new Topic(parser.getTopicName(), p));
        }

        public List<Topic> getTopics() {
            return topics;
        }
    }


}
