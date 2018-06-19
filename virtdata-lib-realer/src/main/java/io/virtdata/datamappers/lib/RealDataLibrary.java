package io.virtdata.datamappers.lib;

import com.google.auto.service.AutoService;
import io.virtdata.annotations.DocManifestAnchor;
import io.virtdata.api.BasicFunctionalLibrary;
import io.virtdata.api.VirtDataFunctionLibrary;
import io.virtdata.datamappers.functions.LastNames;

import java.util.ArrayList;
import java.util.List;

/**
 * This library provides high-level data generation functions which
 * can be used out of the box with little customization needed.
 * In some cases, the data is seeded from actual data.
 */
@AutoService(VirtDataFunctionLibrary.class)
@DocManifestAnchor(name = "realer")
public class RealDataLibrary extends BasicFunctionalLibrary implements VirtDataFunctionLibrary {

    @Override
    public String getName() {
        return "realer";
    }

    @Override
    public List<Package> getSearchPackages() {
        return new ArrayList<Package>() {
            {
                add(LastNames.class.getPackage());
            }
        };
    }
}
