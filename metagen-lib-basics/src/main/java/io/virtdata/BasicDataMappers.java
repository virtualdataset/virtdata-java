package io.virtdata;

import com.google.auto.service.AutoService;
import io.virtdata.api.DataMapperLibrary;
import io.virtdata.core.FunctionalDataMappingLibrary;
import io.virtdata.long_byte.ModuloToByte;
import io.virtdata.long_bytes.HashedToByteBuffer;
import io.virtdata.long_collections.HashedLineToStringMap;
import io.virtdata.long_short.ModuloToShort;
import io.virtdata.long_string.HashedFileExtractToString;
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
public class BasicDataMappers extends FunctionalDataMappingLibrary {
    private static final Logger logger = LoggerFactory.getLogger(BasicDataMappers.class);

    public final static String dataDir = "data";

    @Override
    public String getLibraryName() {
        return "basics";
    }

    @Override
    public List<Package> getSearchPackages() {
        return new ArrayList<Package>() {
            {
                add(io.virtdata.strings.Suffix.class.getPackage());
                add(io.virtdata.long_long.Add.class.getPackage());
                add(io.virtdata.long_int.Add.class.getPackage());
                add(io.virtdata.int_int.Add.class.getPackage());
                add(io.virtdata.functional.DivideToLong.class.getPackage());
                add(io.virtdata.long_timeuuid.ToEpochTimeUUID.class.getPackage());
                add(HashedFileExtractToString.class.getPackage());
                add(HashedLineToStringMap.class.getPackage());
                add(HashedToByteBuffer.class.getPackage());
                add(ModuloToShort.class.getPackage());
                add(ModuloToByte.class.getPackage());
            }
        };

    }
}
