package io.virtdata.continuous.long_double;

import io.virtdata.annotations.ThreadSafeMapper;
import org.apache.commons.math4.distribution.EnumeratedRealDistribution;

/**
 * {@inheritDoc}
 *
 * @see io.virtdata.continuous.long_double.LongToDoubleContinuousCurve
 */
@ThreadSafeMapper
public class Enumerated extends LongToDoubleContinuousCurve {
    public Enumerated(String data, String... mods) {
        super(new EnumeratedRealDistribution(parseWeights(data)[0],parseWeights(data)[1]), mods);
    }

    private static double[][] parseWeights(String input) {
        String[] entries = input.split("[;, ]");
        double[][] elements = new double[2][entries.length];
        for (int i = 0; i < entries.length; i++) {
            String[] parts = entries[i].split(":");
            elements[1][i]=1.0d;
            switch(parts.length) {
                case 2:
                    elements[1][i] = Double.parseDouble(parts[1]);
                case 1:
                    elements[0][i] = Double.parseDouble(parts[0]);
                    break;
                default:
                    throw new RuntimeException("Unable to parse entry or weight from '" + entries[i] + "'");
            }
        }
        return elements;
    }
}
