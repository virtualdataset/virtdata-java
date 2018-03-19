package io.virtdata.ast;

import java.util.ArrayList;
import java.util.List;

public class MetagenAST {
    private List<MetagenFlow> flows = new ArrayList<>();

    public void addFlow(MetagenFlow flow) {
        this.flows.add(flow);
    }

    public List<MetagenFlow> getFlows() {
        return flows;
    }
}
