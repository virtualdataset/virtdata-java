package io.basics.virtdata.api.specs;

import io.basics.virtdata.api.ValueType;

import java.util.Optional;

public interface Specifier {
    public Optional<ValueType> getResultType();
    public String getCanonicalSpec();
}
