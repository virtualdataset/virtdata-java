package io.basics.virtdata.valuesapp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ValuesCheckerApp {

    private final static Logger logger = LoggerFactory.getLogger(ValuesCheckerApp.class);

    public static void main(String[] args) {
        if (args.length<6) {
            System.out.println("ARGS: 'specifier' threads bufsize start end isolated");
            System.out.println("example: 'timeuuid()' 100 1000 0 10000 true");
            System.exit(2);
        }
        String spec = args[0];
        int threads = Integer.valueOf(args[1]);
        int bufsize = Integer.valueOf(args[2]);
        long start = Long.valueOf(args[3]);
        long end = Long.valueOf(args[4]);

        boolean isolated = true;
        if (args.length==6) {
            isolated=args[5].toLowerCase().equals("isolated") || args[5].toLowerCase().equals("true");
        }

        ValuesCheckerCoordinator checker = new ValuesCheckerCoordinator(spec, threads, bufsize, start, end, isolated);

        if (!isolated) {
            logger.warn("You are testing functions which are not intended to be thread-safe in a non-threadsafe way.");
            logger.warn("This is only advisable if you are doing development against the core libraries.");
            logger.warn("Results may vary.");
        }

        RunData runData;
        try {
            runData = checker.call();
            System.out.println(runData.toString());
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(2);
        }

    }

}
