package io.virtdata.basicsmappers.unary_string;

import io.virtdata.api.ThreadSafeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.function.Function;

/**
 * Extracts out a set of fields from a delimited string, returning
 * a string with the same delimiter containing only the specified fields.
 */
@ThreadSafeMapper
public class FieldExtractor implements Function<String,String> {

    private final static Logger logger = LoggerFactory.getLogger(FieldExtractor.class);

    private final String fields;
    private final String splitDelim;
    private final Object printDelim;
    private final int maxIdx;
    private int[] indexes;
    private ThreadLocal<StringBuilder> tlsb = ThreadLocal.withInitial(StringBuilder::new);

    public FieldExtractor(String delim, String fields) {
        if (delim.length()>1) {
            throw new RuntimeException("Single character delimiters are required.");
        }
        this.printDelim = delim;
        this.splitDelim = "\\" + delim;
        this.fields = fields;
        indexes = initIndexes(fields);
        maxIdx = Arrays.stream(indexes).max().orElse(-1);
    }

    private int[] initIndexes(String fields) {
        String[] indexSpecs = fields.split(",");
        int[] indexes = new int[indexSpecs.length];
        for (int i = 0; i < indexes.length; i++) {
            indexes[i] = Integer.valueOf(indexSpecs[i].trim())-1;
        }
        return indexes;
    }

    @Override
    public String apply(String s) {
        String[] words = s.split(splitDelim);
        if (words.length<maxIdx) {
            logger.warn("Short read on line, indexes: " + Arrays.toString(indexes) + ", line:" + s + ", returning 'ERROR-UNDERRUN'");
            return "ERROR-UNDERRUN in FieldExtractor";
        }
        StringBuilder sb = tlsb.get();
        sb.setLength(0);
        for(int index: indexes) {
            sb.append(words[index]).append(printDelim);
        }
        sb.setLength(sb.length()-1);
        return sb.toString();
    }
}
