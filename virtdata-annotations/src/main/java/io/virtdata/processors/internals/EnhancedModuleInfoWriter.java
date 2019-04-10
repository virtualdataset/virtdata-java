package io.virtdata.processors.internals;

import io.virtdata.autodoctypes.DocForFunc;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.nio.charset.StandardCharsets.UTF_8;

public class EnhancedModuleInfoWriter implements FuncEnumerator.Listener {

    private final String suffix;
    private final Set<String> classes = new HashSet<>();
    private Filer filer;
    private Messager messenger;

    public EnhancedModuleInfoWriter(Filer filer, Messager messenger, String suffix) {
        this.filer = filer;
        this.messenger = messenger;
        this.suffix = suffix;
    }

    private String getEntries() {
        StringBuilder sb = new StringBuilder();
        sb.append("  provides javax.annotation.processing.Processor with\n");
        for (String clazz : classes) {
            sb.append("   ").append(clazz).append(",\n");
        }
        sb.setLength(sb.length()-1);
        sb.append(";\n");
        return sb.toString();
    }

    private String readTemplate() throws IOException {
        HashSet<String> serviceClasses = new HashSet<String>();
        FileObject moduleInfoFile = filer.getResource(StandardLocation.SOURCE_PATH, "", "module-info.java");
        Reader reader = moduleInfoFile.openReader(false);
        StringWriter writer = new StringWriter();
        reader.transferTo(writer);
        String existing = writer.getBuffer().toString();
        messenger.printMessage(Diagnostic.Kind.MANDATORY_WARNING, "existing module-info:\n" + existing);
        messenger.printMessage(Diagnostic.Kind.MANDATORY_WARNING, "entries to add:\n" + getEntries());
        return existing;
    }

    /**
     * Writes the set of service class names to a service file.
     *
     * @param output   not {@code null}. Not closed after use.
     * @param services a not {@code null Collection} of service class names.
     * @throws IOException
     */
    static void writeServiceFile(Collection<String> services, OutputStream output)
            throws IOException {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output, UTF_8));
        for (String service : services) {
            writer.write(service);
            writer.newLine();
        }
        writer.flush();
    }

    @Override
    public void onFunctionModel(DocForFunc functionDoc) {
        this.classes.add(functionDoc.getPackageName() + "." + functionDoc.getClassName());
    }

    public void finalFlush(List<String> classNames) {
        this.getEntries();
    }


}

