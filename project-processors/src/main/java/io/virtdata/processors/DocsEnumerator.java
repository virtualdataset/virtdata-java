package io.virtdata.processors;

import javax.annotation.processing.Filer;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class DocsEnumerator {

    private final Types types;
    private final Elements elements;
    private final Filer filer;

    private DocModel model;
    private List<Listener> listeners = new ArrayList<>();

    public DocsEnumerator(Types types, Elements elements, Filer filer) {
        this.types = types;
        this.elements = elements;
        this.filer = filer;
    }

    public void addListener(Listener listener) {
        this.listeners.add(listener);
    }

    public void onClass(String packageName, String simpleClassName, String classJavadoc) {
        if (model!=null) {
            throw new RuntimeException("The DocModel was overwritten. Perhaps you are not calling flush() ?");
        }
        model = new DocModel();
        model.setPackageName(packageName);
        model.setClassName(simpleClassName);
        model.setClassJavadoc(classJavadoc);
    }

    public void onApplyTypes(String inType, String outType) {
        model.setInType(inType);
        model.setOutType(outType);
    }

    public void onInitializer(LinkedHashMap<String, String> args, String ctorDoc) {
        model.addCtor(args,ctorDoc);
    }

    public void flush() {
        this.listeners.forEach(l -> l.onDocModel(model));
        model=null;
    }

    public interface Listener {
        public void onDocModel(DocModel m);
    }
}
