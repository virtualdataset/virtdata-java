package io.virtdata.core;

import java.util.Arrays;

public class SpecReader {
    public static String[] split(String spec) {
        String[] split = spec.split("[,:]");
        return split;
    }
    public static String first(String spec) {
        String s = split(spec)[0];
        return s;
    }
    public static String[] afterFirst(String spec) {
        String[] split = split(spec);
        return Arrays.copyOfRange(split,1,split.length);
    }
}
