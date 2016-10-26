package io.virtdata;

import com.google.auto.service.AutoService;
import io.virtdata.api.DataMapperLibrary;
import io.virtdata.core.FunctionalDataMappingLibrary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>This is a basic generator library that contains a variety of functions to build from.</p>
 *
 * <p>This library simply relies on {@link FunctionalDataMappingLibrary}.</p>
 */
@AutoService(DataMapperLibrary.class)
public class BasicGenerators extends FunctionalDataMappingLibrary {
    private static final Logger logger = LoggerFactory.getLogger(BasicGenerators.class);

    @Override
    public String getLibraryName() {
        return "basics";
    }

    @Override
    public List<Package> getSearchPackages() {
        return new ArrayList<Package>() {
            {
                add(io.virtdata.strings.Suffix.class.getPackage());
                add(io.virtdata.longs.Add.class.getPackage());
                add(io.virtdata.ints.Add.class.getPackage());
                add(io.virtdata.functional.DivideToLong.class.getPackage());
            }
        };

    }
}
