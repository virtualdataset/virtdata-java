package io.virtdata.valuesapp;

public interface PostProcessor extends AutoCloseable {
    void process(Object[] values);
}
