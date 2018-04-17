package io.virtdata.stathelpers.aliasmethod;

import io.virtdata.stathelpers.ElemProbD;

import java.util.Collection;
import java.util.function.LongFunction;

public class AliasSampler<T> implements LongFunction<T> {
    AliasSamplerDoubleInt coreSampler;
    T[] elements;

    public AliasSampler(Collection<ElemProbD<T>> elementProbabilities) {
        elementProbabilities.forEach(ep -> {

        });

    }


    @Override
    public T apply(long value) {
        return null;
    }
}
