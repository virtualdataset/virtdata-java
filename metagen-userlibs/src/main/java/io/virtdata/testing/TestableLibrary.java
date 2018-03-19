package io.virtdata.testing;

import ch.qos.logback.core.status.NopStatusListener;
import com.google.auto.service.AutoService;
import io.virtdata.api.BasicFunctionalLibrary;
import io.virtdata.api.VirtDataFunctionLibrary;
import io.virtdata.testing.functions.ARandomPOJO;

import java.util.ArrayList;
import java.util.List;

@AutoService(VirtDataFunctionLibrary.class)
public class TestableLibrary extends BasicFunctionalLibrary {
    NopStatusListener nop = new NopStatusListener();

    @Override
    public String getName() {
        return "fortesting";
    }

    @Override
    public List<Package> getSearchPackages() {
        return new ArrayList<Package>() {
            {
                add(ARandomPOJO.class.getPackage());
            }
        };
    }
}
