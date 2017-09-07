package io.virtdata.ast;

public class FloatArg implements ArgType {

    private final double floatValue;

    public FloatArg(double floatValue) {
        this.floatValue = floatValue;
    }

    @Override
    public String toString() {
        return String.valueOf(floatValue);
    }
}
