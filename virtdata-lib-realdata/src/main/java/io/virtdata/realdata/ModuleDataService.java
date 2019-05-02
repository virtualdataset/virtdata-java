package io.virtdata.realdata;

import io.virtdata.services.DefaultModuleDataService;

import java.io.InputStream;
import java.nio.file.Path;

public class ModuleDataService extends DefaultModuleDataService {

    @Override
    public InputStream getInputStream(Path resourcePath, Path... searchPathPrefixes) {
        return super.getInputStream(resourcePath, searchPathPrefixes);
    }
}
