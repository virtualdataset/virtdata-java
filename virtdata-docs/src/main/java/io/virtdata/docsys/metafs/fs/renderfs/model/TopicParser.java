package io.virtdata.docsys.metafs.fs.renderfs.model;

import com.vladsch.flexmark.ast.Heading;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class TopicParser {

    private final Path file;
    final static Parser parser = Parser.builder().build();
    private String topicName;

    public TopicParser(Path file) {
        this.file = file;
        try {
            HeV hev = new HeV();
            VisitHandler<Heading> vh = new VisitHandler<>(Heading.class,hev);
            NodeVisitor nv = new NodeVisitor(vh);

            String s = Files.readString(file);
            Document md = parser.parse(s);
            nv.visit(md);
            topicName = hev.headings.size() > 0 ? hev.headings.get(0).getText().toString() : "NO HEADING";
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getTopicName() {
        return topicName;
    }

    private final static class HeV implements Visitor<Heading> {
        public List<Heading> headings = new ArrayList<>();
        @Override
        public void visit(Heading node) {
            headings.add(node);
        }
    }

}
