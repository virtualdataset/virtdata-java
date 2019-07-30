package io.virtdata.docsys.metafs.fs.renderfs.model.topics;

import io.virtdata.docsys.metafs.fs.renderfs.api.versioning.VersionData;
import io.virtdata.docsys.metafs.fs.renderfs.api.versioning.VersionedDirectory;
import io.virtdata.docsys.metafs.fs.renderfs.renderers.DocSysIdGenerator;

import java.nio.file.Path;
import java.util.Collection;
import java.util.LinkedList;

public abstract class PathTopic implements Topic {

    private final DocSysIdGenerator gen = new DocSysIdGenerator();
    protected final Path path;
    private final VersionData versions;
    protected String name;

    protected LinkedList<Topic> subTopics;
    private final static LinkedList<Topic> NO_SUBTOPICS = new LinkedList<>();
    private int level;

    public PathTopic(Path path) {
        this.path = path;
        versions = new VersionData(new VersionedDirectory(path));
    }

    @Override
    public String getName() {
        try {
            if (name==null) {
                String path = getPath();
                int i = path.lastIndexOf("/");
                return "unnamed:" + getPath().substring(0,i);
            }
            return name;
        } catch (Exception e) {
            return "OOPS";
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return gen.generateId(getName());
    }

    @Override
    public String getPath() {
        String pstr = path.toString();
        int i = pstr.lastIndexOf(".");
        if (i<0) {
            return pstr;
        } else {
            return pstr.substring(0,i);
        }
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public LinkedList<Topic> getSubTopics() {
        return subTopics == null ? NO_SUBTOPICS: subTopics;
    }

    public void addSubTopics(Collection<Topic> topics) {
        if (subTopics==null) {
            subTopics = new LinkedList<>();
        }
        this.subTopics.addAll(topics);
    }

    @Override
    public void addSubTopic(Topic topic) {
        if (subTopics==null) {
            subTopics = new LinkedList<>();
        }
        subTopics.add(topic);
    }

    public String toString() {
        return "[" + getLevel() + "[ " + getName() + " ]] : " + getPath() + " id:" + getId();
    }

    public void setName(String name) {
        this.name = name;
    }

}
