package io.virtdata;

import com.google.auto.service.AutoService;
import io.virtdata.api.DataMapperLibrary;
import io.virtdata.core.FunctionalDataMappingLibrary;
import io.virtdata.core.ResolvedFunction;
import io.virtdata.random.RandomFileExtractToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * <p>This is a basic data mapping library that contains a variety of functions to build from.</p>
 *
 * <p>This library simply relies on {@link FunctionalDataMappingLibrary}.</p>
 */
@AutoService(DataMapperLibrary.class)
public class RandomDataMappers extends FunctionalDataMappingLibrary {
    public final static String dataDir = "data";
    private static final Logger logger = LoggerFactory.getLogger(RandomDataMappers.class);

    @Override
    public List<ResolvedFunction> resolveFunctions(String specifier) {
        List<ResolvedFunction> resolvedFunctions = super.resolveFunctions(specifier);
        if (resolvedFunctions.size() > 0) {
            logger.warn(
                    "Generator functions in " + getLibraryName() + " are deprecated and will be removed soon." +
                            " Please use alternative functions. (Hint: Change 'Random' to 'Hashed')");
            String newSpec = specifier.replaceAll("Random", "Hashed");
            List<ResolvedFunction> alternate = super.resolveFunctions(newSpec);
            if (alternate.size() > 0) {
                logger.warn("suggested alternative: " + newSpec);
            }
        }
        return resolvedFunctions;
    }

    @Override
    public Optional<ResolvedFunction> resolveFunction(String spec) {
        Optional<ResolvedFunction> resolvedFunction = super.resolveFunction(spec);
        if (resolvedFunction.isPresent()) {
            logger.warn("Generator functions in " + getLibraryName() + " are deprecated and will be removed soon." +
                    " Please use alternative functions. (Hint: Change 'Random' to 'Hashed')");
            String newSpec = spec.replaceAll("Random", "Hashed");
            Optional<ResolvedFunction> alternate = super.resolveFunction(newSpec);
            if (alternate.isPresent()) {
                logger.warn("suggested alternative: " + newSpec);
            }
        }
        return resolvedFunction;
    }

    @Override
    public String getLibraryName() {
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
