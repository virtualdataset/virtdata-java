package io.virtdata.datamappers.lib;

import com.google.auto.service.AutoService;
import io.virtdata.api.BasicFunctionalLibrary;
import io.virtdata.api.VirtDataFunctionLibrary;
import io.virtdata.datamappers.functions.LastNames;

import java.util.ArrayList;
import java.util.List;

@AutoService(VirtDataFunctionLibrary.class)
public class RealerFunctionLibrary extends BasicFunctionalLibrary implements VirtDataFunctionLibrary {

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
