package io.virtdata.conversions;

import com.google.auto.service.AutoService;
import io.virtdata.annotations.MappingLibrary;
import io.virtdata.api.BasicFunctionalLibrary;
import io.virtdata.api.VirtDataFunctionLibrary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>This is a basic data mapping library that contains a variety of functions to build from.</p>
 *
 * <p>This library simply relies on {@link VirtDataFunctionLibrary}.</p>
 */
@AutoService(VirtDataFunctionLibrary.class)
@MappingLibrary(name = "conversions")
public class TypeConversions extends BasicFunctionalLibrary {

    private static final Logger logger = LoggerFactory.getLogger(TypeConversions.class);

    @Override
    public String getName() {
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
