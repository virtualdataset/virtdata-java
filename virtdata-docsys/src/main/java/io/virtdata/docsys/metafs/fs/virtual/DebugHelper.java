package io.virtdata.docsys.metafs.fs.virtual;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DebugHelper {
    public static String matchesCallerTree(StackTraceElement[] st, String... methodRegexps) {
        Pattern[] patterns = new Pattern[methodRegexps.length];
        for (int i = 0; i < methodRegexps.length; i++) {
            patterns[i]=Pattern.compile(methodRegexps[i]);
        }
        for (StackTraceElement stackTraceElement : st) {
            String fqm = stackTraceElement.getClassName() + "." + stackTraceElement.getMethodName();
            for (int i = 0; i < patterns.length; i++) {
                Matcher matcher = patterns[i].matcher(fqm);
                if (matcher.matches()) {
                    return matcher.group();
                }
            }
        }
        return null;
    }
}
