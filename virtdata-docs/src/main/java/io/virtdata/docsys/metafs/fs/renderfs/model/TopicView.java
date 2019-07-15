package io.virtdata.docsys.metafs.fs.renderfs.model;

import io.virtdata.docsys.metafs.fs.renderfs.model.topics.FileTopic;
import io.virtdata.docsys.metafs.fs.renderfs.model.topics.PathTopic;
import io.virtdata.docsys.metafs.fs.renderfs.model.topics.Topic;
import io.virtdata.docsys.metafs.fs.renderfs.model.topics.TopicFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class TopicView {
    private final static Logger logger = LoggerFactory.getLogger(TopicView.class);
    private final Path topicScope;

    public TopicView(Path target) {
        topicScope = target;
    }

    /**
     * Each file is presented as a topic holder, with its first heading
     * taken as the file topic name.
     *
     * @return A list of topics in a hierarchic structure, with the top
     * level aligned to markdown files
     */
    public List<Topic> getFiles() {
        List<Topic> fileTopics = TopicFinder.getFileTopics(this.topicScope);
        return fileTopics;
    }

    /**
     * Each header is presented.
     *
     * @return
     */
    public List<Topic> getHeaders() {
        List<Topic> topics = TopicFinder.getHeaderTopics(this.topicScope);
        return scrunch(topics);

    }

    private List<Topic> scrunch(List<Topic> topics) {
        Path parent = this.topicScope.getParent();
        PathTopic root = new FileTopic(parent);
        String s = root.toString();
        root.addSubTopics(topics);

        LinkedList<Topic> mill = new LinkedList<>();
        mill.add(root);
        ListIterator<Topic> it = mill.listIterator();
        try {

            while (it.hasNext()) {
                Topic next = it.next();
                for (Topic subTopic : next.getSubTopics()) {
                    subTopic.setLevel(next.getLevel() + 1);
                    it.add(subTopic);
                    it.previous();
                }
            }
            logger.info("Completed scrunch");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return root.getSubTopics();
    }

    public List<Topic> getList() {
        List<Topic> topicList = TopicFinder.getTopicList(this.topicScope);
        return topicList;
    }

}
