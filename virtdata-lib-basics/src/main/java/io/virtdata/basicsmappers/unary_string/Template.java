package io.virtdata.basicsmappers.unary_string;

import io.virtdata.api.ThreadSafeMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.function.LongFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ThreadSafeMapper
public class Template implements LongFunction<String> {
    private static final String EXPR_BEGIN = "[[";
    private static final String EXPR_END = "]]";

    private String[] literals;
    private LongFunction<?>[] funcs;
    private final String rawTemplate;
    private final static ThreadLocal<StringBuilder> sb = ThreadLocal.withInitial(StringBuilder::new);

    public Template(String template, LongFunction<?>... funcs) {
        this.rawTemplate = template;
        this.funcs = funcs;
        this.literals = parseTemplate(template);
    }

    @SuppressWarnings("unchecked")
    private String[] parseTemplate(String rawTemplate) {
        try {
            List<String> literals = new ArrayList<>();
            Pattern p = Pattern.compile("\\{}");
            Matcher m = p.matcher(rawTemplate);
            int pos=0;
            while (m.find()) {
                literals.add(rawTemplate.substring(pos,m.start()));
                pos = m.end();
            }
            literals.add(rawTemplate.substring(pos));
//            literals.addAll(Arrays.asList(rawTemplate.split("\\{}")));
            if (literals.size()!=funcs.length+1) {
                throw new RuntimeException("The number of {} place holders in '" + rawTemplate + "' should equal the number of functions.");
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
            for (int i = 1; i < literals.length; i++) {
                String genString = String.valueOf(funcs[i - 1].apply(value));
                buffer.append(genString);
                buffer.append(literals[i]);
            }
        }
        return buffer.toString();
    }
}
