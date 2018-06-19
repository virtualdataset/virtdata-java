package io.virtdata.processors;

import javax.annotation.processing.Filer;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * This enumerator receives stateful updates out of order from the annotation
 * processor and constructs consistent view of document models for listeners.
 */
public class DocsEnumerator {

    private final Types types;
    private final Elements elements;
    private final Filer filer;

    private DocForFunc model;
    private List<Listener> listeners = new ArrayList<>();
    private String anchorPackage;
    private String anchorSimpleName;

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
        model = new DocForFunc();
        model.setPackageName(packageName);
        model.setClassName(simpleClassName);
        model.setClassJavadoc(classJavadoc);
    }

    public void onAnchor(String anchorPackage, String anchorSimpleName) {
        for (Listener listener : listeners) {
            listener.onAnchorModel(anchorPackage,anchorSimpleName);
        }
    }

    public void onApplyTypes(String inType, String outType) {
        model.setInType(inType);
        model.setOutType(outType);
    }

    public void onConstructor(LinkedHashMap<String, String> args, String ctorJavaDoc, List<List<String>> examples) {
        model.addCtor(ctorJavaDoc, args, examples);
    }

    public void flush() {
        this.listeners.forEach(l -> l.onFunctionModel(model));
        model=null;
    }

    public void onComplete() {
        this.listeners.forEach(Listener::onComplete);
    }

    /**
     * These Listeners handle data that has been found by the DocsEnumerator.
     */
    public interface Listener {
        /**
         * The anchor model is simply location data that can locate rendered docs data
         * within a known naming scheme, such that it can be found in a uniform way at
         * runtime by the using module.
         * @param packageName the anchor element's package name
         * @param anchorName the simple class name of the anchor element
         */
        public void onAnchorModel(String packageName, String anchorName);

        /**
         * Handle each logical function model that has been found.
         * @param functionDoc the documentation model for a single mapping function
         */
        public void onFunctionModel(DocForFunc functionDoc);

        /**
         * If any buffering has occurred in the listener, signal the listener to do
         * any completion processing.
         */
        public void onComplete();
    }
}
