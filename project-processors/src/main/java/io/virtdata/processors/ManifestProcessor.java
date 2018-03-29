package io.virtdata.processors;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import java.util.Set;

@SupportedOptions({"param1","param2"})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes({"io.virtdata.annotations.ThreadSafeMapper"})
public class ManifestProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        System.out.println("annotations:" + annotations+ "\nenv:"+roundEnv);
        return false;
    }
}
