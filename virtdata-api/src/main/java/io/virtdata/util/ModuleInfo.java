package io.virtdata.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.stream.Collectors;

public class ModuleInfo {
    private final static Logger logger = LoggerFactory.getLogger(ModuleInfo.class);

    public static String getModuleInfo(Object o) {
        Set<Module> modules = o.getClass().getModule().getLayer().modules();
        return modules.stream().map(Module::getName).collect(Collectors.joining(","));
    }

    public static void logModuleNamesDebug(Object o) {
        logger.debug("Layer modules for object " + o.toString());
        String moduleName = o.getClass().getModule().getName();
        if (moduleName!=null) {
            logger.debug(" from module '" + o.getClass().getModule().getName() + "':");
            Set<Module> modules = o.getClass().getModule().getLayer().modules();
            modules.stream().map(Module::getName).sorted().forEach(mn -> logger.debug("module:" + mn));
        } else {
            logger.debug("This object is not in a module.");
        }
    }

}
