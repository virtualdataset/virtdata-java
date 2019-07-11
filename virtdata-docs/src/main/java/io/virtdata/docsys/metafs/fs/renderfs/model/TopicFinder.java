package io.virtdata.docsys.metafs.fs.renderfs.model;

import com.vladsch.flexmark.ast.Heading;
import io.virtdata.docsys.metafs.fs.renderfs.walkers.VirtTreeWalker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class TopicFinder {
    private final static Logger logger = LoggerFactory.getLogger(TopicFinder.class);

    public static List<Topic> getTopics(Path baseTopicPath) {
        logger.info("GET TOPICS " + baseTopicPath);
        try {
            FV v = new FV();
            TopicFileFilter f = new TopicFileFilter(".mdf");
            Path parent = baseTopicPath.getParent();
            VirtTreeWalker.walk(parent, v, f);
            List<Topic> topics = v.getTopics();
            return topics;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private final static class TopicFileFilter implements DirectoryStream.Filter<Path> {
        private String extension;

        public TopicFileFilter(String extension) {
            this.extension = extension;
        }

        @Override
        public boolean accept(Path entry) throws IOException {
            if (entry.toString().endsWith(extension))
                return true;
            if(entry.getFileSystem().provider().readAttributes(entry, BasicFileAttributes.class).isDirectory())
                return true;
            return false;
        }
    }

    private final static class FV implements VirtTreeWalker.PathVisitor {
        private List<Topic> topics = new ArrayList<>();

        @Override
        public void visit(Path p) {
            logger.info("VISIT TOPICS for(" + p + "): " + topics.size());
            TopicParser parser = new TopicParser(p);
            List<Heading> headings = parser.getTopicNames();
            for (Heading heading : headings) {
                Topic topic = new Topic(heading, p);
                topics.add(topic);
            }
        }

        public List<Topic> getTopics() {
            return topics;
        }
    }


}
