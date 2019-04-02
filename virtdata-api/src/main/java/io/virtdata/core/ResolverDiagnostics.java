package io.virtdata.core;

import io.virtdata.api.DataMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class ResolverDiagnostics {

    private final static Logger logger = LoggerFactory.getLogger(ResolverDiagnostics.class);

    private ResolvedFunction resolvedFunction;
    private final StringBuilder log = new StringBuilder();
    private Throwable error;

    public ResolverDiagnostics() {
    }

    public <T> Optional<DataMapper<T>> getOptionalMapper() {
        return Optional.ofNullable(resolvedFunction).map(ResolvedFunction::getFunctionObject).map(DataMapperFunctionMapper::map);

    }

    public Optional<ResolvedFunction> getResolvedFunction() {
        return Optional.ofNullable(getResolvedFunctionOrThrow());
    }

    public ResolvedFunction getResolvedFunctionOrThrow() {
        if (error!=null) {
            throw new RuntimeException(error.getMessage(),error);
        }
        return resolvedFunction;
    }

    public ResolverDiagnostics error(Exception e) {
        this.error = e;
        log.append("ERROR encountered while resolving function:\n");
        log.append(e.toString()).append("\n");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(byteArrayOutputStream));
        e.printStackTrace(writer);
        String stacktrace = byteArrayOutputStream.toString(StandardCharsets.UTF_8);

        log.append("stack trace:\n");
        log.append(stacktrace);
        return this;
    }

    public ResolverDiagnostics setResolvedFunction(ResolvedFunction resolvedFunction) {
        this.resolvedFunction = resolvedFunction;
        return this;
    }

    public ResolverDiagnostics trace(String s) {
        logger.trace(s);
        log.append(s).append("\n");
        return this;
    }

    public String toString() {
        return log.toString();
    }

}
