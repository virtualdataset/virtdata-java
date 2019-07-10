package io.virtdata.docsys.metafs.fs.renderfs.model;

import io.virtdata.docsys.metafs.fs.renderfs.walkers.VirtTreeWalker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class TopicFinder {
    private final static Logger logger = LoggerFactory.getLogger(TopicFinder.class);

    public static List<Topic> getTopics(Path baseTopicPath) {

        Predicate<String> f = s -> s.endsWith(".md") && !s.equals(baseTopicPath.toString());
        FV v = new FV();
        VirtTreeWalker.walk(baseTopicPath.getParent(), v, f);
        List<Topic> topics = v.getTopics();
        logger.info("TOPICS for(" + baseTopicPath + "): " + topics.toString());
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
