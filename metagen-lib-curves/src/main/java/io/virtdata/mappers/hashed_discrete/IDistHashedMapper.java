package io.virtdata.mappers.hashed_discrete;

import io.virtdata.mappers.mapped_discrete.IDistMappedResolver;

import java.util.function.LongUnaryOperator;

public class IDistHashedMapper implements LongUnaryOperator {
    private IDistMappedResolver.ThreadSafe idist;

    public IDistHashedMapper(String... args) {
        idist = new IDistMappedResolver.ThreadSafe(args);
    }

    @Override
    public long applyAsLong(long operand) {
        long l = idist.get().applyAsLong(operand);
        return l;
    }
}
