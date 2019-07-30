package io.virtdata.docsys.metafs.fs.renderfs.renderers;

import com.vladsch.flexmark.ast.AnchorRefTarget;
import com.vladsch.flexmark.ast.util.AnchorRefTargetBlockVisitor;
import com.vladsch.flexmark.html.renderer.*;
import com.vladsch.flexmark.util.ast.Document;

public class DocSysIdGenerator extends HeaderIdGenerator {

    @Override
    public void generateIds(Document document) {

        new AnchorRefTargetBlockVisitor() {
            @Override
            protected void visit(AnchorRefTarget node) {
                if (node.getAnchorRefId().isEmpty()) {
                    String text = node.getAnchorRefText();
                    String refId = null;

                    refId = generateId(text);

                    if (refId != null) {
                        node.setAnchorRefId(refId);
                    }
                }
            }
        }.visit(document);
    }

    public String generateId(CharSequence text) {
        String s = text.toString();
        s = s.replaceAll("[^a-zA-Z0-9]+", " ");
        s = s.trim();
        s = s.toLowerCase();
        s = s.replaceAll(" +", "-");
        return s;
    }


    public final static class DocSysIdGeneratorFactory implements HeaderIdGeneratorFactory, HtmlIdGeneratorFactory {

        @Override
        public HtmlIdGenerator create(LinkResolverContext context) {
            return new DocSysIdGenerator();
        }

        @Override
        public HtmlIdGenerator create() {
            return new DocSysIdGenerator();
        }
    }
}
