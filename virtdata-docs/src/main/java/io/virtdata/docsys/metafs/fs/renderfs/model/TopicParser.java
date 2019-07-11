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
    private final List<Heading> headings;
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
            headings = hev.headings;
//            topicName = hev.headings.size() > 0 ? hev.headings.get(0).getText().toString() : "NO HEADING in " + file.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Heading> getTopicNames() {
        return headings;
    }

    private final static class HeV implements Visitor<Heading> {
        public List<Heading> headings = new ArrayList<>();
        @Override
        public void visit(Heading node) {
            headings.add(node);
        }
    }

}
