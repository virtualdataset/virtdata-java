package io.virtdata.processors.internals;

import javax.annotation.processing.Filer;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Optional;

public class ProcessorFileUtils {

    public static void writeGeneratedFileOrThrow(Filer filer, String moduleAndPkg, String relativeName, String fileContents) {
        try {
            FileObject toWrite = filer.createResource(StandardLocation.CLASS_OUTPUT, moduleAndPkg, relativeName);
            Writer moduleWriter = toWrite.openWriter();
            moduleWriter.write(fileContents);
            moduleWriter.close();
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }

    }

    public static Optional<StringBuffer> readOptionalSourceFile(Filer filer, String moduleAndPkg, String relativeName) {
        try {
            FileObject resource = filer.getResource(
                    StandardLocation.SOURCE_PATH,
                    moduleAndPkg,
                    relativeName
            );
            Reader reader = resource.openReader(false);
            StringWriter extantInfo = new StringWriter();
            reader.transferTo(extantInfo);
            return Optional.of(extantInfo.getBuffer());
        } catch (IOException e) {
            return Optional.empty();
        }
    }

}
