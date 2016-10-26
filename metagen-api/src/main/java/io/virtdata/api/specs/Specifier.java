package io.virtdata.api.specs;

import io.virtdata.api.ValueType;

import java.util.Optional;

public interface Specifier {
    public Optional<ValueType> getResultType();
    public String getCanonicalSpec();
}
