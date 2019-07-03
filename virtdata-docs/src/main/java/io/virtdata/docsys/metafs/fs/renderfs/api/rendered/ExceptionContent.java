package io.virtdata.docsys.metafs.fs.renderfs.api.rendered;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

public class ExceptionContent implements RenderedContent {

    private final long version;
    private final Exception e;
    private final Object[] details;

    public ExceptionContent(Exception e, long version, Object... details) {

        this.e = e;
        this.version = version;
        this.details = details;
    }

    @Override
    public long getVersion() {
        return version;
    }

    @Override
    public String get() {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PrintWriter printStream = new PrintWriter(bos);
        for (Object detail : this.details) {
            printStream.write(detail.toString());
        }
        printStream.write(e.getMessage());
        printStream.flush();
        String out = new String(bos.toByteArray(), StandardCharsets.UTF_8);
        return out;
    }

    public String toString() {
        return get();
    }
}
