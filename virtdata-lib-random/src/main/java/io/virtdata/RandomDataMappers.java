package io.virtdata;

import com.google.auto.service.AutoService;
import io.virtdata.annotations.DocManifestAnchor;
import io.virtdata.api.BasicFunctionalLibrary;
import io.virtdata.api.VirtDataFunctionLibrary;
import io.virtdata.core.ResolvedFunction;
import io.virtdata.random.RandomFileExtractToString;
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
@DocManifestAnchor(name = "random")
public class RandomDataMappers extends BasicFunctionalLibrary {
    public final static String dataDir = "data";
    private static final Logger logger = LoggerFactory.getLogger(RandomDataMappers.class);

    @Override
    public List<ResolvedFunction> resolveFunctions(Class<?> returnType, Class<?> inputType, String functionName, Object... parameters) {
        List<ResolvedFunction> resolvedFunctions = super.resolveFunctions(returnType, inputType, functionName, parameters);
        if (resolvedFunctions.size() > 0) {
            logger.warn(
                    "Generator functions in " + getName() + " are deprecated and will be removed soon." +
                            " Please use alternative functions. (Hint: Change 'Random' to 'Hashed')");
            String newSpec = functionName.replaceAll("Random", "Hashed");
            List<ResolvedFunction> alternate = super.resolveFunctions(returnType, inputType, newSpec, parameters);
            if (alternate.size() > 0) {
                logger.warn("suggested alternative: " + newSpec);
            }
        }
        return resolvedFunctions;
    }

    @Override
    public String getName() {
        return "random";
    }

    @Override
    public List<Package> getSearchPackages() {
        return new ArrayList<Package>() {
            {
                add(RandomFileExtractToString.class.getPackage());
            }
        };

    }
}
