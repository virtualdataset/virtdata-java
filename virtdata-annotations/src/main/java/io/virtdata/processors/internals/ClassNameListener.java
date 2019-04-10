package io.virtdata.processors.internals;

import io.virtdata.autodoctypes.DocForFunc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ClassNameListener implements FuncEnumerator.Listener {

    private final List<String> classNames = new ArrayList<>();

    @Override
    public void onFunctionModel(DocForFunc functionDoc) {
        classNames.add(functionDoc.getPackageName() +"." + functionDoc.getClassName());
    }

    public List<String> getClassNames() {
        return Collections.unmodifiableList(classNames);
    }

}
