package io.virtdata;

import io.virtdata.testing.ConcurrentValuesChecker;

public class CheckConcurrency {

    public static void main(String[] args) {
        if (args.length!=5) {
            System.out.print("ARGS: 'specifier' threads bufsize start end");
            System.exit(2);
        }
        String spec = args[0];
        int threads = Integer.valueOf(args[1]);
        int bufsize = Integer.valueOf(args[2]);
        long start = Long.valueOf(args[3]);
        long end = Long.valueOf(args[4]);

        ConcurrentValuesChecker checker = new ConcurrentValuesChecker(spec, threads, bufsize, start, end);

        try {
            Boolean result = checker.call();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
