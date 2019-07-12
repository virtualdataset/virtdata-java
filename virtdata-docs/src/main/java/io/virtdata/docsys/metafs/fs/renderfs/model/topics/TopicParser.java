package io.virtdata.docsys.metafs.fs.renderfs.model.topics;

import com.vladsch.flexmark.ast.Heading;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Document;
import com.vladsch.flexmark.util.ast.NodeVisitor;
import com.vladsch.flexmark.util.ast.VisitHandler;
import com.vladsch.flexmark.util.ast.Visitor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class TopicParser {

    final static Parser parser = Parser.builder().build();
    private final Path file;
    private List<Heading> headings;

    public TopicParser(Path file) {
        this.file = file;
    }

    private List<Heading> getHeadings() {
        if (this.headings == null) {
            try {
                HeV hev = new HeV();
                VisitHandler<Heading> vh = new VisitHandler<>(Heading.class, hev);
                NodeVisitor nv = new NodeVisitor(vh);

                String s = Files.readString(file);
                Document md = parser.parse(s);
                nv.visit(md);
                this.headings = hev.headinglist;
//            topicName = hev.headings.size() > 0 ? hev.headings.get(0).getText().toString() : "NO HEADING in " + file.toString();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
        return this.headings;

    }

    public List<Topic> getHeaderTopics() {
        List<Heading> headings = getHeadings();
        return headings
                .stream()
                .map(h -> new HeaderTopic(h, file))
                .collect(Collectors.toList());
    }

    public LinkedList<Topic> getNestedHeaderTopics() {
        Deque<Topic> roots = new ArrayDeque<>();
        roots.addLast(new FileTopic(file));

        Deque<Heading> stack = new ArrayDeque<>();
        stack.addLast(new Heading() {{
            setLevel(0);
        }});

        for (Heading heading : getHeadings()) {

            HeaderTopic thistopic = new HeaderTopic(heading, this.file);

            int thislevel = heading.getLevel();
            while (roots.peekLast().getLevel()>=thislevel) {
                roots.removeLast();
            }
            roots.peekLast().addSubTopic(thistopic);
            roots.addLast(thistopic);
        }
        while (roots.peekLast().getLevel()>roots.peekFirst().getLevel()) {
            roots.removeLast();
        }


        if (roots.size()!=1) {
            throw new RuntimeException("Struct not as expected after parser walk.");
        }

        return roots.peek().getSubTopics();

    }

    private final static class HeV implements Visitor<Heading> {
        public List<Heading> headinglist = new ArrayList<>();

        @Override
        public void visit(Heading heading) {
            headinglist.add(heading);
        }
    }

    public String toString() {
        return "TOPICS:" + this.file.toString();
    }

}
