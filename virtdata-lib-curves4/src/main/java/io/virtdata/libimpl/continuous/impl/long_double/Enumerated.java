package io.virtdata.libimpl.continuous.impl.long_double;

import io.virtdata.annotations.ThreadSafeMapper;
import org.apache.commons.math4.distribution.EnumeratedRealDistribution;

import java.util.Arrays;

@ThreadSafeMapper
public class Enumerated extends LongToDoubleContinuousCurve {
    public Enumerated(String data, String... mods) {
        super(new EnumeratedRealDistribution(parseWeights(data)[0],parseWeights(data)[1]), mods);
    }

    private static double[][] parseWeights(String input) {
        String[] entries = input.split("[;, ]");
        double[][] elements = new double[entries.length][2];
        for (int i = 0; i < elements.length; i++) {
            String[] parts = entries[i].split(":");
            elements[i][1]=1.0d;
            switch(parts.length) {
                case 2:
                    elements[i][1] = Double.parseDouble(parts[1]);
                case 1:
                    elements[i][0] = Double.parseDouble(parts[0]);
                    break;
                default:
                    throw new RuntimeException("Unable to parse entry or weight from '" + entries[i] + "'");
            }
        }
        return elements;
    }
}
