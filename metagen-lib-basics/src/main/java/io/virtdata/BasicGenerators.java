package io.virtdata;

import com.google.auto.service.AutoService;
import io.virtdata.api.GeneratorLibrary;
import io.virtdata.core.BaseGeneratorLibrary;
import io.virtdata.functional.StaticStringGenerator;
import io.virtdata.longs.Add;
import io.virtdata.strings.Suffix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

@SuppressWarnings({"unchecked", "Duplicates"})
@AutoService(GeneratorLibrary.class)
public class BasicGenerators extends BaseGeneratorLibrary {
    private static final Logger logger = LoggerFactory.getLogger(BasicGenerators.class);

    @Override
    public String getLibraryName() {
        return "basics";
    }

    private List<Package> packages = new LinkedList<Package>() {
        {
            add(StaticStringGenerator.class.getPackage());
            add(Add.class.getPackage());
            add(Suffix.class.getPackage());
        }
    };

    @Override
    public List<Package> getSearchPackages() {
        return packages;
    }


}
