package io.virtdata.basicsmappers.from_long.to_string;

import io.virtdata.api.ThreadSafeMapper;
import io.virtdata.basicsmappers.from_long.to_double.HashedDoubleRange;

import java.util.ArrayList;
import java.util.List;
import java.util.function.LongFunction;

@ThreadSafeMapper
public class WeightedStrings implements LongFunction<String> {

    private final String valuesAndWeights;
    private double[] unitWeights; // Positional weights after parsing and unit weight normalization
    private double[] cumulativeWeights;
    private HashedDoubleRange unitRange = new HashedDoubleRange(0.0D, 1.0D);
    private String[] values;

    public WeightedStrings(String valuesAndWeights) {
        this.valuesAndWeights = valuesAndWeights;
        parseWeights();
    }

    private void parseWeights() {
        String[] pairs = valuesAndWeights.split(";");
        if (pairs.length == 0) {
            throw new RuntimeException("No pairs were found. They must be separated by ';'");
        }

        values = new String[pairs.length];
        List<Double> parsedWeights = new ArrayList<>();
        for (int i = 0; i < pairs.length; i++) {
            String[] pair = pairs[i].split(":", 2);
            if (pair.length != 2) {
                throw new RuntimeException("Each pair must be in value:weight form");
            }
            values[i] = pair[0];
            parsedWeights.add(Double.valueOf(pair[1]));
        }
        double total = parsedWeights.stream().mapToDouble(f -> f).sum();
        unitWeights = parsedWeights.stream().mapToDouble(f -> f / total).toArray();
        cumulativeWeights = new double[unitWeights.length];
        double cumulative = 0.0D;
        for (int i = 0; i < unitWeights.length; i++) {
            cumulative += unitWeights[i];
            cumulativeWeights[i] = cumulative;
        }
    }

    @Override
    public String apply(long value) {
        double sampledUnit = unitRange.applyAsDouble(value);
        for (int i = 0; i < cumulativeWeights.length; i++) {
            if (sampledUnit < cumulativeWeights[i]) {
                return values[i];
            }
        }
        throw new RuntimeException(
                "sampled value '" + sampledUnit + "' was not below final cumulative weight: "
                        + cumulativeWeights[cumulativeWeights.length - 1]);
    }
}
