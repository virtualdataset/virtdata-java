package io.virtdata.docsys.metafs.fs.renderfs.model.topics;

import java.util.LinkedList;

public interface Topic {

    String getName();

    /**
     * Return the self-same generated ref id that will be rendered in HTML.
     * The rendered HTML and the parsed model of the markdown use the same
     * generator scheme.
     * @return A simple text based ref id
     */
    String getId();

    /**
     * @return the logical path associated with this topic, without the extension.
     */
    String getPath();

    /**
     * Get a level associated with this topic. Lower numbers represent broader
     * topics. The is akin to "indentation level".
     * @return The level of nesting for this topic
     */
    int getLevel();
    void setLevel(int i);

    /**
     * Get subtopics that are associated with this topic, or contained by this topic.
     * @return A list of topics
     */
    LinkedList<Topic> getSubTopics();

    /**
     * Add a subtopic to this topic.
     * @param topic a ne subtopic
     */
    void addSubTopic(Topic topic);

    default String getIndent() {
        return " ".repeat(getLevel());
    }
    default String getIndent2() {
        return "  ".repeat(getLevel());
    }

}
