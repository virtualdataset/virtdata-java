package io.virtdata.parser;

public class VirtDataParseException extends RuntimeException {
    public VirtDataParseException(String reason, NumberFormatException nfe) {
        super(reason,nfe);
    }
}
