package io.virtdata.libbasics.shared.distributions;

import io.virtdata.annotations.Categories;
import io.virtdata.annotations.Category;
import io.virtdata.annotations.DeprecatedFunction;
import io.virtdata.annotations.ThreadSafeMapper;
import io.virtdata.libbasics.core.stathelpers.AliasSamplerDoubleInt;
import io.virtdata.libbasics.shared.from_long.to_long.Hash;
import io.virtdata.libbasics.core.stathelpers.EvProbD;
import io.virtdata.util.VirtDataResources;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.LongFunction;

@Categories(Category.general)
@DeprecatedFunction("This has been replaced with WeightedStringFromCSV(...)")
@ThreadSafeMapper
public class WeightedStrings implements LongFunction<String> {

    private final String[] filenames;
    private final String valueColumn;
    private final String weightColumn;
    private final String[] lines;
    private final AliasSamplerDoubleInt sampler;
    private Hash hash;

    /**
     * Create a sampler of strings from the given CSV file. The CSV file must have plain CSV headers
     * as its first line.
     * @param valueColumn The name of the value column to be sampled
     * @param weightColumn The name of the weight column, which must be parsable as a double
     * @param filenames One or more file names which will be read in to the sampler buffer
     */
    public WeightedStrings(String valueColumn, String weightColumn, String... filenames) {
        this.filenames = filenames;
        this.valueColumn = valueColumn;
        this.weightColumn = weightColumn;
        List<EvProbD> events = new ArrayList<>();
        List<String> values = new ArrayList<>();

        if (filenames[0].equals("map")) {
            filenames = Arrays.copyOfRange(filenames,1,filenames.length);
            this.hash=null;
        } else {
            if (filenames[0].equals("hash")) {
                filenames = Arrays.copyOfRange(filenames,1,filenames.length);
            }
            this.hash=new Hash();
        }
        for (String filename: filenames) {
            CSVParser csvdata = VirtDataResources.readFileCSV(filename);
            for (CSVRecord csvdatum : csvdata) {
                String value = csvdatum.get(valueColumn);
                values.add(value);
                String weight = csvdatum.get(weightColumn);
                events.add(new EvProbD(values.size()-1,Double.valueOf(weight)));
            }
        }
        sampler = new AliasSamplerDoubleInt(events);
        lines = values.toArray(new String[0]);
    }

    @Override
    public String apply(long value) {
        if (hash!=null) {
            value = hash.applyAsLong(value);
        }
        double unitValue = (double) value / (double) Long.MAX_VALUE;
        int idx = sampler.applyAsInt(unitValue);
        return lines[idx];
    }
}
