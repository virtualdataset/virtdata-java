package io.basics.virtdata.api.specs;

import io.basics.virtdata.api.ValueType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpecData implements Specifier {

    public final static String RTYPE_DELIM = "->";
    protected final static Pattern funcNamePattern =
            Pattern.compile("(?<func>[a-zA-Z][a-zA-Z0-9-_.]*)");
    protected final static Pattern argPattern =
            Pattern.compile("(\\s*(?<arg>[^,)]+)?\\s*(\\)|,)*)");
    protected final static Pattern argsPattern =
            Pattern.compile("(?<args>(\\(.*?)\\))?");
    protected final static Pattern resultTypePattern =
            Pattern.compile("( *" + RTYPE_DELIM + " *(?<rtype>\\w+))?");
    private final static Pattern specPattern =
            Pattern.compile(funcNamePattern.pattern() + argsPattern.pattern() + resultTypePattern.pattern());
    private String[] funcArgs;
    private ValueType resultType;

    private SpecData(String func, String[] args, ValueType resultType) {
        this.funcArgs = new String[args.length + 1];
        this.funcArgs[0] = func;
        System.arraycopy(args, 0, funcArgs, 1, args.length);
        this.resultType = resultType;
    }

    public static SpecData forSpec(String spec) {
        Optional<SpecData> specData = forOptionalSpec(spec);
        return specData.orElseThrow(() -> new RuntimeException("Unable to parse " + spec));
    }

    public static Optional<SpecData> forOptionalSpec(String spec) {
        Matcher matcher = specPattern.matcher(spec);
        if (!matcher.matches()) {
            return Optional.empty();
        }
        String funcname = matcher.group("func");
        String funcargs = matcher.group("args");
        List<String> args = new ArrayList<>();
        if (funcargs != null) {
            if (funcargs.startsWith("(") && funcargs.endsWith(")")) {
                funcargs = funcargs.substring(1, funcargs.length() - 1);
            }
            Matcher matchargs = argPattern.matcher(funcargs);
            while (matchargs.find()) {
                if (matchargs.group("arg") == null) {
                    continue;
                }
                args.add(matchargs.group("arg"));
            }
        }
        String rtype = matcher.group("rtype");
        ValueType valueType = (rtype == null) ? null : ValueType.valueOfClassName(rtype);
        SpecData newspec = new SpecData(funcname, args.toArray(new String[0]), valueType);
        return Optional.of(newspec);
    }

    public SpecData forResultType(ValueType resultType) {
        return new SpecData(getFuncName(), getArgs(), resultType);
    }

    public String getFuncName() {
        return funcArgs[0];
    }

    public String[] getArgs() {
        return Arrays.copyOfRange(funcArgs, 1, funcArgs.length);
    }

    public Optional<ValueType> getResultType() {
        return Optional.ofNullable(resultType);
    }

    public String[] getFuncAndArgs() {
        return Arrays.copyOf(funcArgs, funcArgs.length);
    }

    public String getCanonicalSpec() {
        StringBuilder sb = new StringBuilder(100);
        sb.append(getFuncName());
        if (funcArgs.length > 0) {
            sb.append("(");
            String comma = "";
            for (String funcArg : this.getArgs()) {
                sb.append(comma).append(funcArg);
                comma = ",";
            }
            sb.append(")");
        }
        if (this.resultType != null) {
            sb.append("->").append(this.resultType.getSimpleName());
        }
        return sb.toString();
    }

    public String toString() {
        return getCanonicalSpec();
    }
//    public static Pattern camelCaseMatcher = Pattern.compile("(?<=[a-z])(?=[A-Z])|(?<=[A-Z])(?=[A-Z][a-z])");
//    public static Pattern underscoreMatcher = Pattern.compile("([a-zA-Z0-9]+)");

}
