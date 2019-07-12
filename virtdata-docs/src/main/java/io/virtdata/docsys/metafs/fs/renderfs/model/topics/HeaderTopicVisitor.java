package io.virtdata.docsys.metafs.fs.renderfs.model.topics;

import io.virtdata.docsys.metafs.fs.renderfs.walkers.VirtTreeWalker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class HeaderTopicVisitor implements VirtTreeWalker.PathVisitor {
    private final static Logger logger = LoggerFactory.getLogger(HeaderTopicVisitor.class);
    private List<Topic> topics = new ArrayList<>();

    @Override
    public void visit(Path p) {
        logger.info("VISIT TOPICS for(" + p + "): " + topics.size());
        TopicParser parser = new TopicParser(p);
        topics.addAll(parser.getHeaderTopics());
    }

    public List<Topic> getTopics() {
        return topics;
    }
}
