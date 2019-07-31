package io.virtdata.docsys.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class DocServerApp {
    public final static String APPNAME_DOCSERVER = "docserver";
    private final static Logger logger = LoggerFactory.getLogger(DocServerApp.class);

    public static void main(String[] args) {
        if (args.length > 0) {
            String subcmd = args[0].toLowerCase();
            args = Arrays.copyOfRange(args, 1, args.length);
            if (subcmd.equals("topics")) {
                listTopics();
            } else if (subcmd.equals("search")) {
                search(args);
            } else if (subcmd.equals("help")) {
                showHelp(args);
            } else if (subcmd.equals("server")) {
                runServer(args);
            } else {
                throw new RuntimeException("unknown subcommand: " + subcmd);
            }
        } else {
            runServer(args);
        }
    }

    private static void runServer(String[] serverArgs) {
        DocServer server = new DocServer();
        for (int i = 0; i < serverArgs.length; i++) {
            String arg = serverArgs[i];
            if (arg.matches("http//.*")) {
                server.withURL(arg);
            } else if (Files.exists(Path.of(arg))) {
                server.addPaths(Path.of(arg));
            } else if (arg.matches("\\d+")) {
                server.withPort(Integer.parseInt(arg));
            }
        }
//
        server.run();
    }

    private static void showHelp(String... helpArgs) {
        System.out.println(
                "Usage: " + APPNAME_DOCSERVER + " " +
                        " [url] " +
                        " [path]... " + "\n" +
                        "\n" +
                        "If [url] is provided, then the scheme, address and port are all taken from it.\n" +
                        "Any additional paths are served from the filesystem, in addition to the internal ones.\n"
        );
    }

    private static void search(String[] searchArgs) {
    }

    private static void listTopics() {

    }
}
