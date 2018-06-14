package io.virtdata.processors;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class DocModel {
    private String packageName;
    private String className;
    private String classJavadoc;
    private String inType;
    private String outType;
    private ArrayList<Ctor> ctors = new ArrayList<>();

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassJavadoc() {
        return classJavadoc;
    }

    public void setClassJavadoc(String classJavadoc) {
        this.classJavadoc = classJavadoc;
    }

    public String getInType() {
        return inType;
    }

    public void setInType(String inType) {
        this.inType = inType;
    }

    public String getOutType() {
        return outType;
    }

    public void setOutType(String outType) {
        this.outType = outType;
    }

    public void addCtor(LinkedHashMap<String, String> args, String ctorDoc) {
        Ctor ctor = new Ctor(args, ctorDoc);
        ctors.add(ctor);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        String ctorDelim="";
        for (Ctor ctor : this.ctors) {
            sb.append(ctorDelim);

            String inType= (this.inType==null? "UNSET" : this.inType);
            inType=inType.replace("java.util.","").replace("java.lang.","");

            sb.append(inType).append(" -> ").append(this.className).append("(");

            String delim="";
            for (String paramName : ctor.args.keySet()) {
                sb.append(delim);
                sb.append(ctor.args.get(paramName).replace("java.util.","").replace("java.lang.","")).append(" ").append(paramName);
                delim=",";
            }
            String outType=(this.outType==null? "UNSET" : this.outType);
            outType=outType.replace("java.util.","").replace("java.lang.","");
            sb.append(") -> ").append(outType);
            ctorDelim="\n";
        }

        return sb.toString();
    }

    public static class Ctor {

        private LinkedHashMap<String, String> args;
        private String ctorDoc;

        public Ctor(LinkedHashMap<String, String> args, String ctorDoc) {
            this.args = args;
            this.ctorDoc = ctorDoc;
        }

        public String getCtorDoc() {
            return ctorDoc;
        }

        public LinkedHashMap<String, String> getArgs() {
            return args;
        }
    }
}
