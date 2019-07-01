package io.virtdata.docsys.metafs.fs.renderfs.api.rendered;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

public class ExceptionContent implements RenderedContent {

    private final long version;
    private final Exception e;

    public ExceptionContent(Exception e, long version) {
        this.e = e;
        this.version = version;
    }

    @Override
    public long getVersion() {
        return version;
    }

    @Override
    public String get() {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PrintWriter printStream = new PrintWriter(bos);
        printStream.write(e.getMessage());
//        printStream.write("\n");
//        e.printStackTrace(printStream);
        printStream.flush();
        String out = new String(bos.toByteArray(), StandardCharsets.UTF_8);
        return out;
    }

    public String toString() {
        return get();
    }
}
