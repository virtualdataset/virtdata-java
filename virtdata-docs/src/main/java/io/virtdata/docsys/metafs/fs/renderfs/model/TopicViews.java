package io.virtdata.docsys.metafs.fs.renderfs.model;

import io.virtdata.docsys.metafs.fs.renderfs.api.RendererIO;
import io.virtdata.docsys.metafs.fs.renderfs.api.versioning.VersionedDirectory;
import io.virtdata.docsys.metafs.fs.renderfs.api.versioning.VersionedObjectCache;
import io.virtdata.docsys.metafs.fs.renderfs.model.topics.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class TopicViews {

    private final static Logger logger = LoggerFactory.getLogger(TopicViews.class);
    private final VersionedObjectCache cache = VersionedObjectCache.INSTANCE;
    private final Path topicScope;

    public TopicViews(Path target) {
        topicScope = target;
    }

    /**
     * Get an iterable view of all topics that are found by walking
     * all markdown fragments and parsing out all headers. These
     * headers are returned as a topic tree rooted at the top level
     * header or headers in each file, with the header levels compacted
     * for uniform nesting levels in rendering.
     * @return A list of root header topics with sub topics added
     */
    public HeaderTopicsView getHeaders() {
        HeaderTopicsView viewOfHeaderTopics = cache.getOrCreate(
                HeaderTopicsView.class,
                this.topicScope.toString(),
                this::getHeaderTopics);
        return viewOfHeaderTopics;
    }

    /**
     * Get a flat view of all topics. This is an iterable of topics which
     * are not nested in any way. The heading level is preserved as the topics
     * were originally seen.
     * @return A flat list of topics as they were found in markdown fragments
     */
    public FlatTopicsView getFlat() {
        FlatTopicsView flatTopicsView = cache.getOrCreate(
                FlatTopicsView.class,
                this.topicScope.toString(),
                this::getFlatTopicsView);
        return flatTopicsView;
    }

    /**
     * Get a list of topics found by walking all markdown fragments and extracting
     * headers. The first heading of each file is promoted to a root topic. If there
     * are subtopics nested under the first topic, then they are promoted up one
     * structural level. Each topic in the returned list represents a single file,
     * with all the internal topics attached below. The topic levels are compacted
     * for uniform nesting levels in rendering.
     * @return a list of topics by file, with internal topics attached to each
     */
    public FileTopicsView getFiles() {
        FileTopicsView fileTopicsView = cache.getOrCreate(
                FileTopicsView.class,
                this.topicScope.toString(),
                this::getFileTopicsView);
            return fileTopicsView;
    }

    private FileTopicsView getFileTopicsView() {
        List<Topic> fileTopics = TopicFinder.getFileTopics(this.topicScope);
        fileTopics = scrunch(fileTopics);
        return new FileTopicsView(fileTopics, new VersionedDirectory(this.topicScope));
    }


    private FlatTopicsView getFlatTopicsView() {
        List<Topic> topicList = TopicFinder.getTopicList(this.topicScope);
        return new FlatTopicsView(topicList, new VersionedDirectory(this.topicScope));
    }


    private HeaderTopicsView getHeaderTopics() {
        List<Topic> topics = TopicFinder.getHeaderTopics(this.topicScope);
        List<Topic> scrunched = scrunch(topics);
        return new HeaderTopicsView(scrunched, new VersionedDirectory(this.topicScope));
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


}
