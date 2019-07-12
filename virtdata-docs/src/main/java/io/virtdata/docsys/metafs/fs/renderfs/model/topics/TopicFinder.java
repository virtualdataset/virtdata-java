package io.virtdata.docsys.metafs.fs.renderfs.model.topics;

import io.virtdata.docsys.metafs.fs.renderfs.walkers.VirtTreeWalker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;

public class TopicFinder {
    private final static Logger logger = LoggerFactory.getLogger(TopicFinder.class);

    public static List<Topic> getTopicList(Path baseTopicPath) {
//        VirtTreeWalker.TLPATHS.get().clear();

        logger.info("GET TOPIC LISTS " + baseTopicPath);
        try {
            HeaderTopicVisitor v = new HeaderTopicVisitor();
            TopicFileFilter f = new TopicFileFilter(".mdf", baseTopicPath);
            Path parent = baseTopicPath.getParent();
            VirtTreeWalker.walk(parent, v, f);
            List<Topic> topics = v.getTopics();
            return topics;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Topic> getHeaderTopics(Path baseTopicPath) {
        logger.info("GET TOPIC TREES" + baseTopicPath);
        try {
            TopicTreeVisitor v = new TopicTreeVisitor(baseTopicPath);
            TopicFileFilter f = new TopicFileFilter(".mdf", baseTopicPath);
            Path parent = baseTopicPath.getParent();
            VirtTreeWalker.walk(parent, v, f);
            return v.getTopicTrees();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Topic> getFileTopics(Path topicScope) {
        logger.info("GET FILE TOPICS " + topicScope);
        try {
            FileTopicVisitor v = new FileTopicVisitor(topicScope);
            TopicFileFilter f = new TopicFileFilter(".mdf", topicScope);
            Path parent = topicScope.getParent();
            VirtTreeWalker.walk(parent, v, f);
            List<Topic> topics = v.getFileTopics();
            return topics;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private final static class TopicFileFilter implements DirectoryStream.Filter<Path> {
        private final Path callerPath;
        private final String logicalPathName;
        private String extension;

        public TopicFileFilter(String extension, Path callerPath) {
            this.extension = extension;
            this.callerPath = callerPath;
            int i = this.callerPath.toString().lastIndexOf(".");
            this.logicalPathName = callerPath.toString().substring(0,i);
        }

        @Override
        public boolean accept(Path entry) throws IOException {
            if (entry.equals(callerPath)) {
                return false;
            }
            if (entry.toString().startsWith(logicalPathName)) {
                return false;
            }

            if (entry.toString().endsWith(extension)) {
                return true;
            }
            if (entry.getFileSystem().provider()
                    .readAttributes(entry, BasicFileAttributes.class).isDirectory()) {
                return true;
            }
            return false;
        }
    }


}
