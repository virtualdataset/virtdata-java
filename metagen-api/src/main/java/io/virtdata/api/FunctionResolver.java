package io.virtdata.api;

import io.virtdata.core.ResolvedFunction;

public interface FunctionResolver {
    ResolvedFunction getResolvedFunction(String spec);
}
