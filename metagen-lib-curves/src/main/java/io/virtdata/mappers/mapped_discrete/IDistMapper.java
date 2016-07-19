package io.virtdata.mappers.mapped_discrete;

import java.util.function.LongUnaryOperator;

public class IDistMapper implements LongUnaryOperator {
    private IDistMappedResolver.ThreadSafe idist;

    public IDistMapper(String... args) {
        idist = new IDistMappedResolver.ThreadSafe(args);
    }

    @Override
    public long applyAsLong(long operand) {
        long l = idist.get().applyAsLong(operand);
        return l;
    }
}
