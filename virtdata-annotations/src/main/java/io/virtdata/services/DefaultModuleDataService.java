package io.virtdata.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * This class needs to be held in the module for which it will return data,
 * so that local data sources within that module can be resolve relative
 * to the service endpoint itself.
 *
 * In order to support historic path names that users have become familiar with,
 * such as "/data", this data service will use that as a default search path.
 *
 * Since the adoption of JPMS in Virtdata, however, the base path "/MODULE-DATA" is used,
 * since this allows to bypass the unique naming requirements of module elements, while
 * at the same time allowing for this path to be instanced as a per-module namespace.
 *
 * Thus, some files are moved from "/data" to "/MODULE-DATA", and in these cases, both
 * paths must be checked when either of them is specified. In order to steer users towards
 * canonically-named resources, the following conditions must be detected and messaged
 * in logs appropriately:
 *
 * <UL>
 * <LI>Data is found under "/data/..." when "/MODULE-DATA/..." was specified.
 * In this case, the user should be warned about the fact that </LI>
 * <LI>Data was found under "/MODULE-DATA/..." when "/data/..." was specified</LI>
 * </UL>
 *
 * The next change to this loader will be to allow for explicit selection of resource
 * locations, or specific ordering of search paths.
 * <UL>
 * <LI>MODULE-DATA - Only module data will be considered when searching this location.
 * <LI>CLASS-PATH:...</LI>
 * <LI>FILE-SYSTEM:...</LI>
 * <LI>URI:...</LI>
 * </UL>
 */
public class DefaultModuleDataService implements ModuleDataService {
    private final static Logger logger = LoggerFactory.getLogger(DefaultModuleDataService.class);

    @Override
    public InputStream getInputStream(Path resourcePath, Path... searchIn) {


        try {
            Path initialPath = ModuleDataService.DATAPATH.resolve(resourcePath);
            InputStream found = this.getClass().getModule().getResourceAsStream(initialPath.toString());
            if (found!=null) {
                return found;
            }
        } catch (IOException ignored) {
        }

        List<Path> searchedPaths = new ArrayList<>();
//        candidatePaths.add(resourcePath);
        //searchPathPrefixes.stream().map(spp -> spp.resolve(resourcePath)).forEach(candidatePaths::add);

        for (Path searchPathPrefix : searchIn) {
            InputStream stream = null;
            try {
                Path candidatePath = ModuleDataService.DATAPATH.resolve(searchPathPrefix.resolve(resourcePath));
                stream = this.getClass().getModule().getResourceAsStream(candidatePath.toString());
                if (stream != null) {
                    return stream;
                } else {
                    searchedPaths.add(candidatePath);
                    logger.trace("did not find resource in " + candidatePath);
                }
            } catch (IOException ignored) {
            }
        }
        return null;
    }

    public String toString() {
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        StringBuilder sb = new StringBuilder();
        sb.append(" (from:").append(stacktrace[2].getModuleName()).append(")");
        return this.getClass().getModule().getName() + "/" + this.getClass().getSimpleName() + sb.toString();
    }

}
