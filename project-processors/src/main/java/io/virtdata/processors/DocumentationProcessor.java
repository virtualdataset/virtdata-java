package io.virtdata.processors;

import io.virtdata.annotations.PerThreadMapper;
import io.virtdata.annotations.ThreadSafeMapper;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@SupportedOptions({"title"})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes({"io.virtdata.annotations.ThreadSafeMapper", "io.virtdata.annotations.PerThreadMapper"})
public class DocumentationProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        List<Element> ts = new ArrayList<>();
        ts.addAll(roundEnv.getElementsAnnotatedWith(ThreadSafeMapper.class));
        ts.addAll(roundEnv.getElementsAnnotatedWith(PerThreadMapper.class));

        for (Element t : ts) {
            try {
                Documenter d = new Documenter(t,processingEnv.getFiler());
                d.write();
            } catch (Throwable e) {
                System.out.println("Error while processing element '" + t + "': " + e.toString());
            }
        }



        return false;
    }
}
