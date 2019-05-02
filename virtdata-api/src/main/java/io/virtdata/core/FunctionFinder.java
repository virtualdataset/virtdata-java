package io.virtdata.core;

import io.virtdata.services.FunctionFinderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

// https://docs.oracle.com/en/java/javase/12/docs/api/java.base/java/lang/module/package-summary.html

/**
 * Finds all sources of ThreadSafeMappers and ThreadLocalMappers using a couple schemes, in the following
 * order:
 *
 * <OL>
 *     <LI>All resolvable DocFuncData services are used directly.</LI>
 *     <LI>All resolvable DocFuncDataFinder services are used directly.</LI>
 * </OL>
 *
 */
public class FunctionFinder {
    private final static Logger logger = LoggerFactory.getLogger(FunctionFinder.class);

    public List<FunctionFinderService.Path> getFunctionNames() {

        List<FunctionFinderService.Path> paths = new ArrayList<>();

        List<FunctionFinderService> finders = ServiceLoader.load(FunctionFinderService.class)
                .stream()
                .map(ServiceLoader.Provider::get)
                .collect(Collectors.toList());

        logger.debug("Found " + finders.size() + " function enumerators in the runtime.");

        for (FunctionFinderService docFuncDataFinder : finders) {
            List<FunctionFinderService.Path> funcNamesForModule = docFuncDataFinder.getFunctionPaths();
            logger.debug("Found " + funcNamesForModule.size() +
                    " functions in library '" + docFuncDataFinder.getClass().getModule().getName());
            paths.addAll(funcNamesForModule);
        }
        return paths;
    }
}