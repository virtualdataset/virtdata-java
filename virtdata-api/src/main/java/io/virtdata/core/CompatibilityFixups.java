package io.virtdata.core;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * $_=~s/\blog_normal\(/LogNormal\(/g;
 * $_=~s/\bnormal\(/Normal\(/g;
 * $_=~s/\blevy\(/Levy\(/g;
 * $_=~s/\bnakagami\(/Nakagami\(/g;
 * $_=~s/\btriangular\(/Triangular\(/g;
 * $_=~s/\bexponential\(/Exponential\(/g;
 * $_=~s/\blogistic\(/Logistic\(/g;
 * $_=~s/\blaplace\(/Laplace\(/g;
 * $_=~s/\bcauchy\(/Cauchy\(/g;
 * $_=~s/\bf\(/F\(/g;
 * $_=~s/\bt\(/T\(/g;
 * $_=~s/\bweibull\(/Weibull\(/g;
 * $_=~s/\bchi_squared\(/ChiSquared\(/g;
 * $_=~s/\bgumbel\(/Gumbel\(/g;
 * $_=~s/\bbeta\(/Beta\(/g;
 * $_=~s/\bpareto\(/Pareto\(/g;
 * $_=~s/\bgamma\(/Gamma\(/g;
 * $_=~s/\buniform_real\(/UniformReal\(/g;
 * $_=~s/\bhypergeometric\(/Hypergeometric\(/g;
 * $_=~s/\buniform_integer\(/UniformInteger\(/g;
 * $_=~s/\bgeometric\(/Geometric\(/g;
 * $_=~s/\bpoisson\(/Poisson\(/g;
 * $_=~s/\bzipf\(/Zipf\(/g;
 * $_=~s/\bbinomial\(/Binomial\(/g;
 * $_=~s/\bpascal\(/Pascal\(/g;
 */
public class CompatibilityFixups {


    private final static Map<String,String> funcs = new HashMap<String,String>() {{
        put("log_normal","LogNormal");
        put("normal", "Normal");
        put("levy", "Levy");
        put("nakagami","Nakagami");
        put("exponential","Exponential");
        put("logistic","Logistic");
        put("laplace","Laplace");
        put("cauchy","Cauchy");
        put("f","F");
        put("t","T");
        put("weibull","Weibull");
        put("chi_squared","ChiSquared");
        put("gumbel","Gumbel");
        put("beta","Beta");
        put("pareto","Pareto");
        put("gamma","Gamma");
        put("uniform_real","Uniform");
        put("uniform_integer","Uniform");
        put("hypergeometric","Hypergeometric");
        put("geometric","Geometric");
        put("poisson","Poisson");
        put("zipf","Zipf");
        put("binomial","Binomial");
        put("pascal","Pascal");
    }};
    private static final String MAPTO = "mapto_";
    private static final String HASHTO = "hashto_";
    private static final String COMPUTE = "compute_";
    private static final String INTERPOLATE = "interpolate_";

    private final static Pattern oldcurve = Pattern.compile("(?<name>\\b[\\w_]+)(?<lparen>\\()(?<args>.+?)(?<rparen>\\))");

    private final static CompatibilityFixups instance = new CompatibilityFixups();

    public static String fixup(String spec) {
        return instance.fix(spec);
    }

    public String fix(String spec) {

        // Fixup curve ctors. These are not HOF, so local matching will work fine. However, they could occur multiple
        // times within an HOF, so multiple replace is necessary.
        Matcher matcher = oldcurve.matcher(spec);
        StringBuilder out = new StringBuilder(spec.length());
        int start = 0;

        while (matcher.find()) {
            out.append(spec.substring(start, matcher.regionStart()));
            String replacement = fixCurveCall(matcher.group("name"), matcher.group("args"));
            out.append(replacement);
            start = matcher.regionEnd();
        }
        out.append(spec.substring(start));

        return out.toString();
    }

    private String fixCurveCall(String name, String args) {
        boolean map = false;
        boolean compute = false;
        if (name.contains(MAPTO)) {
            name = name.replaceAll(MAPTO,"");
            map=true;
        }
        if (name.contains(HASHTO)) {
            name = name.replaceAll(HASHTO,"");
            map=false;
        }
        if (name.contains(COMPUTE)) {
            name = name.replaceAll(COMPUTE,"");
            compute=true;
        }
        if (name.contains(INTERPOLATE)) {
            name = name.replaceAll(INTERPOLATE,"");
            compute=false;
        }

        String nameReplacement = funcs.get(name);
        if (nameReplacement!=null) {
            name=nameReplacement;
            args=map?args+",'map'":args+",'hash'";
            args=compute?args+",'compute'":args+",'interpolate'";
        }
        return name+"("+args+")";

    }
}
