package io.virtdata.core;

import io.virtdata.api.VirtDataFunctionLibrary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface VirtDataLibrary {

    final static Logger logger = LoggerFactory.getLogger(VirtDataLibrary.class);

    VirtDataFunctionLibrary getFunctionLibrary();
    String getLibname();

    BindingsTemplate getBindingsTemplate(String... namesAndSpecs);

}
