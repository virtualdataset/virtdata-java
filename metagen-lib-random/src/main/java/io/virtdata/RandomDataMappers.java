package io.virtdata;

import com.google.auto.service.AutoService;
import io.virtdata.api.DataMapperLibrary;
import io.virtdata.core.FunctionalDataMappingLibrary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>This is a basic data mapping library that contains a variety of functions to build from.</p>
 *
 * <p>This library simply relies on {@link FunctionalDataMappingLibrary}.</p>
 */
@AutoService(DataMapperLibrary.class)
public class RandomDataMappers extends FunctionalDataMappingLibrary {
    private static final Logger logger = LoggerFactory.getLogger(RandomDataMappers.class);

    public final static String dataDir = "data";

    @Override
    public String getLibraryName() {
        return "random";
    }

    @Override
    public List<Package> getSearchPackages() {
        return new ArrayList<Package>() {
            {
                add(io.virtdata.random.RandomFileExtractToString.class.getPackage());
            }
        };

    }
}
