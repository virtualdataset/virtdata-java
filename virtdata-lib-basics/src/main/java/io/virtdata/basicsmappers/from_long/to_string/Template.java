package io.virtdata.basicsmappers.from_long.to_string;

import io.virtdata.annotations.Example;
import io.virtdata.annotations.ThreadSafeMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.function.LongFunction;
import java.util.function.LongUnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Creates a template function which will yield a string which fits the template
 * provided, with all occurrences of <pre>{}</pre> substituted pair-wise with the
 * result of the provided functions. The number of <pre>{}</pre> entries in the template
 * must strictly match the number of functions or an error will be thrown.
 *
 * To provide differing values for similarly defined functions in the list, the input
 * value used is automatically incremented by one for each function, starting with
 * the initial input value.
 */
@ThreadSafeMapper
public class Template implements LongFunction<String> {
    private static final String EXPR_BEGIN = "[[";
    private static final String EXPR_END = "]]";
    private final static ThreadLocal<StringBuilder> sb = ThreadLocal.withInitial(StringBuilder::new);
    private final String rawTemplate;
    private LongUnaryOperator iterOp;
    private String[] literals;
    private LongFunction<?>[] funcs;

    @Example({"Template('{}-{}',Add(10),Hash())","concatenate input+10, '-', and a pseudo-random long"})
    public Template(String template, LongFunction<?>... funcs) {
        this.funcs = funcs;
        this.rawTemplate = template;
        this.literals = parseTemplate(template, funcs);
    }

    /**
     * If an operator is provided, it is used to change the function input value in an additional way before each function.
     *
     * @param iterOp   A pre-generation value mapping function
     * @param template A string template containing <pre>{}</pre> anchors
     * @param funcs    A varargs length of LongFunctions of any output type
     */
    public Template(LongUnaryOperator iterOp, String template, LongFunction<?>... funcs) {
        this(template, funcs);
        this.iterOp = iterOp;
    }

    @SuppressWarnings("unchecked")
    private String[] parseTemplate(String template, LongFunction<?>... funcs) {
        try {
            List<String> literals = new ArrayList<>();
            Pattern p = Pattern.compile("\\{}");
            Matcher m = p.matcher(template);
            int pos = 0;
            while (m.find()) {
                literals.add(template.substring(pos, m.start()));
                pos = m.end();
            }
            literals.add(template.substring(pos));
            if (literals.size() != funcs.length + 1) {
                throw new RuntimeException("The number of {} place holders in '" + template + "' should equal the number of functions.");
            }
            return literals.toArray(new String[0]);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String apply(long value) {
        StringBuilder buffer = sb.get();
        buffer.setLength(0);
        buffer.append(literals[0]);
        if (literals.length > 1) {
            for (int i = 0; i < funcs.length; i++) {
                long input = iterOp != null ? iterOp.applyAsLong(value + i) : value + i;
                String genString = String.valueOf(funcs[i].apply(input));
                buffer.append(genString);
                buffer.append(literals[i + 1]);
            }
        }
        return buffer.toString();
    }
}
