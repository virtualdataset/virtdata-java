package io.virtdata.apps;

import io.virtdata.apps.docsapp.AutoDocsApp;
import io.virtdata.apps.valuesapp.ValuesCheckerApp;
import io.virtdata.docsys.core.DocServer;

import java.nio.file.Path;
import java.util.Arrays;

/**
 * This just routes the user to the correct sub-app depending on the leading verb, stripping it off in the process.
 */
public class MainApp {

    private final static String APP_TESTMAPPER = "testmapper";
    private final static String APP_GENDOCS = "gendocs";
    private final static String APP_DOCSERVER = "docserver";

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: app (" + APP_TESTMAPPER + "|" + APP_GENDOCS + "|" + APP_DOCSERVER + ")");
            System.exit(0);
        }

        String appSelection = args[0];
        String[] appArgs = new String[0];
        if (args.length > 1) {
            appArgs = Arrays.copyOfRange(args, 1, args.length);
        }

        if (appSelection.toLowerCase().equals(APP_TESTMAPPER)) {
            ValuesCheckerApp.main(appArgs);
        } else if (appSelection.toLowerCase().equals(APP_GENDOCS)) {
            AutoDocsApp.main(appArgs);
        } else if (appSelection.toLowerCase().equals(APP_DOCSERVER)) {
            DocServer docs = new DocServer().addPaths(
                    Path.of("docs"),
                    Path.of("virtdata-docsys/docs")
            ).addWebObject(VirtDataService.class);

            docs.run();
        } else {
            System.err.println("Error in command line. The first argument must be " + APP_GENDOCS + " or " + APP_TESTMAPPER);
        }
    }
}
