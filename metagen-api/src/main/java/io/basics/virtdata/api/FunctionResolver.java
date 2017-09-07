package io.basics.virtdata.api;

import io.basics.virtdata.core.ResolvedFunction;

public interface FunctionResolver {
    ResolvedFunction getResolvedFunction(String spec);
}
