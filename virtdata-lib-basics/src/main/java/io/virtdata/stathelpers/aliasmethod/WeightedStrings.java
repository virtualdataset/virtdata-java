package io.virtdata.stathelpers.aliasmethod;

import io.virtdata.api.ThreadSafeMapper;
import io.virtdata.stathelpers.EvProbD;
import io.virtdata.util.ResourceFinder;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.util.ArrayList;
import java.util.List;
import java.util.function.LongFunction;

/**
 * Provides sampling of a given field in a CSV file according
 * to discrete probabilities.
 */
@ThreadSafeMapper
public class WeightedStrings implements LongFunction<String> {

    private final String[] filenames;
    private final String valueColumn;
    private final String weightColumn;
    private final String[] lines;
    private final AliasSampler sampler;

    /**
     * Creata a sampler of strings from the given CSV file. The CSV file must have plain CSV headers
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

        for (String filename: filenames) {
            CSVParser csvdata = ResourceFinder.readFileCSV(filename);
            for (CSVRecord csvdatum : csvdata) {
                String value = csvdatum.get(valueColumn);
                values.add(value);
                String weight = csvdatum.get(weightColumn);
                events.add(new EvProbD(values.size()-1,Double.valueOf(weight)));
            }
        }
        sampler = new AliasSampler(events);
        lines = values.toArray(new String[0]);
    }

    @Override
    public String apply(long value) {
        double unitValue = (double) value / (double) Long.MAX_VALUE;
        int idx = sampler.applyAsInt(unitValue);
        return lines[idx];
    }
}
