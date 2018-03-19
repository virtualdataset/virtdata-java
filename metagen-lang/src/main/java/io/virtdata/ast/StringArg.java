package io.virtdata.ast;

public class StringArg implements ArgType {
    private final String text;

    public StringArg(String text) {
        this.text = text;
    }

    public String getStringValue() {
        return text;
    }

    @Override
    public String toString() {
        return "'"+text+"'";
    }
}
