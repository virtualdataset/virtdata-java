package io.virtdata.api;

public interface Generator<R> {
    R get(long input);
}
