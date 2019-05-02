package io.virtdata.libbasics;

import io.virtdata.services.DefaultModuleDataService;

import java.io.InputStream;
import java.nio.file.Path;

public class BasicsModuleDataService extends DefaultModuleDataService {
    @Override
    public InputStream getInputStream(Path resourcePath, Path... searchPathPrefixes) {
        return super.getInputStream(resourcePath, searchPathPrefixes);
    }
}
