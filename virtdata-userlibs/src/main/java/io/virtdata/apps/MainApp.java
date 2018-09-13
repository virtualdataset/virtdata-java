package io.virtdata.apps;

import io.virtdata.apps.docsapp.DocsApp;
import io.virtdata.apps.valuesapp.ValuesCheckerApp;

import java.util.Arrays;

/**
 * This just routes the user to the correct sub-app depending on the leading verb, stripping it off in the process.
 */
public class MainApp {

    private final static String APP_TESTMAPPER = "testmapper";
    private final static String APP_GENDOCS = "gendocs";

    public static void main(String[] args) {
        if (args.length==0) {
            System.out.println("Usage: app (" + APP_TESTMAPPER + "|" + APP_GENDOCS + ")");
            System.exit(0);
        }

        String appSelection = args[0];
        String[] appArgs= new String[0];
        if (args.length>1) {
            appArgs = Arrays.copyOfRange(args, 1, args.length);
        }

        if (appSelection.toLowerCase().equals(APP_TESTMAPPER)) {
            ValuesCheckerApp.main(appArgs);
        } else if (appSelection.toLowerCase().equals(APP_GENDOCS)) {
            DocsApp.main(appArgs);
        } else {
            System.err.println("Error in command line. The first argument must be " + APP_GENDOCS + " or " + APP_TESTMAPPER);
        }
    }
}
