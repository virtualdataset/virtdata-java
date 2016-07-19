package io.virtdata.mappers.mapped_continuous;

import java.util.function.LongToDoubleFunction;

public class CDistMapper implements LongToDoubleFunction {
    private CDistMappedResolver.ThreadSafe cdist;

    public CDistMapper(String... args) {
        cdist = new CDistMappedResolver.ThreadSafe(args);
    }

    @Override
    public double applyAsDouble(long value) {
        double d = cdist.get().applyAsDouble(value);
        return d;
    }
}
