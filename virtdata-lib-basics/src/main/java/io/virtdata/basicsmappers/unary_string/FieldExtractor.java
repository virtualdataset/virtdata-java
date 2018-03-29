package io.virtdata.basicsmappers.unary_string;

import io.virtdata.annotations.ThreadSafeMapper;
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

    public FieldExtractor(String fields) {
        this.fields = fields;

        String[] indexSpecs = fields.split(",");
        this.printDelim = indexSpecs[0];
        this.splitDelim = "\\" + indexSpecs[0];
        indexes = new int[indexSpecs.length-1];
        for (int i = 1; i <= indexes.length; i++) {
            indexes[i-1] = Integer.valueOf(indexSpecs[i].trim())-1;
        }
        maxIdx = Arrays.stream(indexes).max().orElse(-1);
    }

    private int[] initIndexes(String fields) {
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
