package io.virtdata.conversions;

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
public class TypeConversions extends FunctionalDataMappingLibrary {
    private static final Logger logger = LoggerFactory.getLogger(TypeConversions.class);

    @Override
    public String getLibraryName() {
        return "conversions";
    }

    @Override
    public List<Package> getSearchPackages() {
        return new ArrayList<Package>() {
            {
                add(io.virtdata.conversions.from_double.ToByte.class.getPackage());
                add(io.virtdata.conversions.from_float.ToByte.class.getPackage());
                add(io.virtdata.conversions.from_int.ToByte.class.getPackage());
                add(io.virtdata.conversions.from_long.ToByte.class.getPackage());
                add(io.virtdata.conversions.from_string.ToByte.class.getPackage());
                add(io.virtdata.conversions.from_short.ToByte.class.getPackage());
            }
        };

    }
}
