package io.virtdata.basicsmappers.unary_string;

import io.virtdata.api.DataMapper;
import io.virtdata.api.ThreadSafeMapper;
import io.virtdata.core.VirtData;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.LongFunction;

@ThreadSafeMapper
public class FuncTemplate implements LongFunction<String> {
    private static final String EXPR_BEGIN = "[[";
    private static final String EXPR_END = "]]";

    private String[] literals;
    private DataMapper<String>[] funcs;
    private final String rawTemplate;
    private final static ThreadLocal<StringBuilder> sb = ThreadLocal.withInitial(StringBuilder::new);

    public FuncTemplate(String rawTemplate) {
        this.rawTemplate = rawTemplate;
        this.parseTemplate(rawTemplate);
    }


    @SuppressWarnings("unchecked")
    private void parseTemplate(String rawTemplate) {
        try {
            int pos = 0;
            List<String> lits = new ArrayList<>();
            List<DataMapper<String>> funcs = new ArrayList<>();
            while (pos < rawTemplate.length()) {
                int startat = rawTemplate.indexOf(EXPR_BEGIN, pos);
                int endat = rawTemplate.indexOf(EXPR_END, pos);
                if (startat >= 0 && endat >= startat) {

                    String pre = rawTemplate.substring(pos, startat);
                    lits.add(pre);

                    String expr = rawTemplate.substring(startat + 2, endat);
                    Optional<DataMapper<String>> func = VirtData.getMapper(expr);
                    funcs.add(func.orElseThrow(() -> new RuntimeException("Unable to resolve function: " + expr)));

                    pos = endat + 2;
                } else if (startat >= 0 || endat >= 0) {
                    throw new RuntimeException("invalid (( and )) positions while parsing '" + rawTemplate + "' from position " + pos);
                } else {
                    String remainder = rawTemplate.substring(pos, rawTemplate.length());
                    lits.add(remainder);
                    pos = rawTemplate.length();
                }
            }
            if (lits.size() <= funcs.size()) {
                lits.add("");
            }
            this.literals = lits.toArray(new String[0]);
            this.funcs = funcs.toArray(new DataMapper[0]);
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
                String genString = funcs[i - 1].get(value);
                buffer.append(genString);
                buffer.append(literals[i]);
            }
        }
        return buffer.toString();
    }
}
