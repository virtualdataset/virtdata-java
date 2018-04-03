package io.virtdata.basicsmappers.from_long.to_string;

import io.virtdata.annotations.ThreadSafeMapper;

import java.util.function.LongFunction;
import java.util.function.LongUnaryOperator;

@ThreadSafeMapper
public class JoinTemplate extends Template implements LongFunction<String>  {

    /**
     * Creates a string template function which will concatenate the result of the
     * provided functions together with the provided delimiter.
     * @param delimiter the delimiter between values
     * @param funcs functions which provide data to concatenate
     */
    public JoinTemplate(String delimiter, LongFunction<?>... funcs) {
        super(templateFor("",delimiter,"",funcs), funcs);
    }

    /**
     * Creates a string template function which will concatenate the result
     * of the provided function together with the delimiter, but with the
     * prifix prepended and the suffix appended to the final result.
     * @param delimiter the string delimiter between values
     * @param prefix the string to prefix
     * @param suffix the string to suffix
     * @param funcs functions which provide that data to concatenate
     */
    public JoinTemplate(String prefix, String delimiter, String suffix, LongFunction<?>... funcs) {
        super(templateFor(prefix,delimiter,suffix,funcs), funcs);
    }

    public JoinTemplate(LongUnaryOperator iterop, String prefix, String delimiter, String suffix, LongFunction<?>... funcs) {
        super(iterop, templateFor(prefix,delimiter,suffix,funcs), funcs);

    }
    private static String templateFor(String prefix, String delimiter, String suffix, LongFunction<?>... funcs) {
        StringBuilder sb=new StringBuilder();
        sb.append(prefix);
        for (int i = 0; i < funcs.length; i++) {
            sb.append("{}");
            sb.append(delimiter);
        }
        sb.setLength(sb.length()-delimiter.length());
        sb.append(suffix);
        return sb.toString();
    }

}
