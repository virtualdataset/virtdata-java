package io.virtdata.docsys.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class DocServerApp {
    private final static Logger logger  = LoggerFactory.getLogger(DocServerApp.class);

    public static void main(String[] args) {
        if (args.length == 1 && args[0].equals("help")) {
            showHelp();
        }

        if (args.length>0) {
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
            }else {
                throw new RuntimeException("unknown subcommand: " +subcmd);
            }
        } else {
            runServer(args);
        }
    }

    private static void runServer(String[] serverArgs) {
        Path contentRoot = Paths.get("docs").toAbsolutePath().normalize();
        Path docsysDocs = Paths.get("virtdata-docs/docs").toAbsolutePath().normalize();
        logger.info("Starting server with content roots:" + contentRoot.toString() + "," + docsysDocs.toString());
        DocServer server = new DocServer().addPaths(contentRoot, docsysDocs);
        server.run();
    }

    private static void showHelp(String... helpArgs) {
        System.out.println(
                "Usage:\n" +
                        " topics\n" +
                        " search\n" +
                        " help\n" +
                        " server\n"
        );
    }

    private static void search(String[] searchArgs) {
    }

    private static void listTopics() {

    }
}
