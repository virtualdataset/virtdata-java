package io.virtdata.libbasics.shared.stateful;

import io.virtdata.annotations.ThreadSafeMapper;
import io.virtdata.api.config.ConfigAware;
import io.virtdata.api.config.ConfigModel;
import io.virtdata.api.config.MutableConfigModel;

import java.util.Map;
import java.util.function.Function;

@ThreadSafeMapper
public class LoadElement implements Function<Object,Object>, ConfigAware {

    private final String varname;
    private final Object defaultValue;

    private Map<String,?> vars;

    public LoadElement(String varname) {
        this(varname, null);
    }
    public LoadElement(String varname, Object defaultValue) {
        this.varname = varname;
        this.defaultValue = defaultValue;
    }

    @Override
    public Object apply(Object o) {
        if (vars==null) {
            throw new RuntimeException("An injected element of type Map is required");
        }

        Object object = vars.get(varname);
        return (object!=null) ? object : defaultValue;
    }

    @Override
    public void applyConfig(Map<String, ?> element) {
        Map<String,?> vars = (Map<String, ?>) element.get("vars");
        if (vars!=null) {
            this.vars = vars;
        }
    }

    @Override
    public ConfigModel getConfigModel() {
        return new MutableConfigModel().add("vars",Map.class);
    }
}
