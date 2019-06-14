package io.virtdata.docsys.metafs.fs.renderfs.api;

import io.virtdata.docsys.metafs.fs.renderfs.model.TargetPathView;

import java.nio.ByteBuffer;
import java.security.InvalidParameterException;
import java.util.function.Supplier;

public class RenderableChain implements Renderable {
    private RenderableEntry[] entries;

    public RenderableChain(Supplier<ByteBuffer> initialBufferSource, TemplateCompiler... compilers) {
        this.entries = new RenderableEntry[compilers.length];
        if (compilers.length==0) {
            throw new InvalidParameterException("There must be at least 1 compiler for a rendered chain.");
        }
        entries[0]=new RenderableEntry(initialBufferSource, compilers[0]);
        if (entries.length>1) {
            for (int i = 1; i < compilers.length; i++) {
                entries[i] = new RenderableEntry(entries[i-1],compilers[i]);
            }
        }
    }

    @Override
    public long getVersion() {
        return entries[entries.length - 1].getVersion();
    }

    @Override
    public ByteBuffer apply(TargetPathView targetPathView) {
        return entries[entries.length -1].apply(targetPathView);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Chain");
        sb.append("[").append(entries==null ? "-init-" : entries.length).append("]:");
        for (RenderableEntry entry : entries) {
            sb.append((entry==null ? "NULL" : entry.toString()));
            sb.append(",");
        }
        sb.setLength(sb.length()-1);
        return sb.toString();
    }

}
