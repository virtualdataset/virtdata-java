package io.virtdata.docsys.metafs.fs.renderfs.model.topics;

import io.virtdata.docsys.metafs.fs.renderfs.walkers.VirtTreeWalker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

public class FileTopicVisitor implements VirtTreeWalker.PathVisitor {
    private final static Logger logger = LoggerFactory.getLogger(FileTopicVisitor.class);
    private final PathTopic root;

    public FileTopicVisitor(Path p) {
        root = new FileTopic(p);
        logger.trace("VISIT FILE TOPICS for(" + p + ")");
    }

    public List<Topic> getFileTopics() {
        return root.getSubTopics();
    }

    @Override
    public void visit(Path p) {
        TopicParser parser = new TopicParser(p);
        LinkedList<Topic> topicsNestedByHeader = parser.getNestedHeaderTopics();
        FileTopic fileTopic = new FileTopic(p);
        if (topicsNestedByHeader.size()>0) {
            Topic first = topicsNestedByHeader.removeFirst();
            fileTopic.addSubTopics(first.getSubTopics());
            fileTopic.setName(first.getName());
        }
        fileTopic.addSubTopics(topicsNestedByHeader);
        root.addSubTopic(fileTopic);
    }
}
