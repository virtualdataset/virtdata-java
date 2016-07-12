package com.metawiring.gen.internal;

import org.apache.commons.math3.distribution.*;
import org.apache.commons.math3.random.RandomGenerator;

import java.security.InvalidParameterException;
import java.util.Arrays;

/**
 * Map a population and a samplerFunction together to configure a new samplerFunction instance.
 * For each samplerFunction, the idea is to approximate what most users might do for the population.
 * This is not meant to replace all methods of configuring distributions. It is simply the minimal
 * support for users who want a simply configuration. More advanced options may follow.
 */
public class SizedDistributionMapper {

    public static IntegerDistribution mapIntegerDistribution(
            Class<? extends IntegerDistribution> distClass,
            int minValue, int maxValue,
            RandomGenerator rng,
            String[] distParams) {


        switch (distClass.getSimpleName()) {

            case "UniformIntegerDistribution":
                return new UniformIntegerDistribution(rng, minValue, maxValue);
            case "BinomialDistribution":
                return new BinomialDistribution(rng, maxValue, 0.5d);

//            case "PascalDistribution":
//                 break;
//            case "EnumeratedIntegerDistribution":
//                break;
            case "GeometricDistribution":
                double p = distParams.length >= 1 ? Double.valueOf(distParams[0]) : 0.5d;
                return new GeometricDistribution(rng,p);
//            case "HypergeometericDistribution":
//                break;
//            case "ZipfDistribution":
//                break;
//            case "PoissonDistribution":
//                break;
            default:
                throw new RuntimeException("samplerFunction " + distClass.getSimpleName() + " was not a supported samplerFunction");

        }

    }

    public static Class<? extends IntegerDistribution> mapIntegerDistributionClass(String distributionName) {
        switch (distributionName) {
            case "uniform":
            case "random":
                return UniformIntegerDistribution.class;
            case "binomial":
                return BinomialDistribution.class;
            case "geometric":
                return GeometricDistribution.class;
            case "hypergeometric":
                return HypergeometricDistribution.class;
            case "zipf":
                return ZipfDistribution.class;
            case "poisson":
                return PoissonDistribution.class;
            case "enumerated":
                return EnumeratedIntegerDistribution.class;
            case "normal":
            case "gaussian":
//                return NormalDistribution.class;
                throw new RuntimeException("You probably want to use a discrete samplerFunction for this, like binomial."
                        + " Continuous distributions are not supported yet. When they are, they will only be for field values.");
            default:
                throw new RuntimeException("Discrete/Integer distribution name " + distributionName + " was not recognized");

        }
    }

    public static Class<? extends RealDistribution> mapRealDistributionClass(String distributionName) {
        switch (distributionName) {
            case "normal":
            case "gaussian":
                return NormalDistribution.class;
            case "t":
                return TDistribution.class;
            case "cauchy":
                return CauchyDistribution.class;
            case "uniform":
                return UniformRealDistribution.class;
            case "chisquared":
                return ChiSquaredDistribution.class;
            case "enumerated":
                return EnumeratedRealDistribution.class;
            case "lognormal":
                return LogNormalDistribution.class;
            case "weibull":
                return WeibullDistribution.class;
            case "levy":
                return LevyDistribution.class;
            case "pareto":
                return ParetoDistribution.class;
            case "gamma":
                return GammaDistribution.class;
            case "beta":
                return BetaDistribution.class;
            case "exponential":
                return ExponentialDistribution.class;
            case "f":
                return FDistribution.class;
            case "triangular":
                return TriangularDistribution.class;
            default:
                throw new RuntimeException("Continuous/Real distribution name " + distributionName + " was not recognized. It's parameter mappings may not be implemented yet.");
        }

    }

    public static RealDistribution mapRealDistribution(RandomGeneratorAdapter randomMapper, String[] def) {
        if (def.length<1) {
            throw new InvalidParameterException("def must have at least 1 parameter, the distribution name. This one has zero.");
        }
        Class<? extends RealDistribution> clazz = mapRealDistributionClass(def[0]);
        String distName = def[0];
        Class<? extends RealDistribution> distClass = mapRealDistributionClass(distName);
        def = Arrays.copyOfRange(def,1,def.length-1);

        RealDistribution realDistribution = null;
        switch (distClass.getSimpleName()) {
            case "ParetoDistribution":
                double scale = def.length >=1 ? Double.valueOf(def[0]): 1.0d;
                double shape = def.length >= 2 ? Double.valueOf(def[1]): 1.0d;
                double invCumAccuracy = def.length >= 3 ? Double.valueOf(def[2]): 1e-9;
                realDistribution = new ParetoDistribution(randomMapper,scale,shape,invCumAccuracy);
                break;
            case "LevyDistribution":
                double mu_location = def.length >= 1 ? Double.valueOf(def[0]): 1.0d;
                double c_scale = def.length >= 2 ? Double.valueOf(def[1]): 10.0d;
                realDistribution = new LevyDistribution(randomMapper,mu_location,c_scale);
                break;
            default:
                throw new RuntimeException("samplerFunction " + distClass.getSimpleName() + " was not a supported samplerFunction. It's parameter mappings may not be implemented yet.");
        }
        return realDistribution;

    }

    public static IntegerDistribution mapIntegerDistribution(RandomGeneratorAdapter rng, String[] def) {
        if (def.length<1) {
            throw new InvalidParameterException("def must have at least 1 parameter, the distribution name. This one has zero.");
        }

        Class<? extends IntegerDistribution> clazz = mapIntegerDistributionClass(def[0]);
        String distName = def[0];
        Class<? extends IntegerDistribution> distClass = mapIntegerDistributionClass(distName);
        def = Arrays.copyOfRange(def,1,def.length-1);

        IntegerDistribution integerDistribution=null;
        switch (distClass.getSimpleName()) {
            case "GeometricDistribution":
                double p = def.length >= 1 ? Double.valueOf(def[0]) : 0.5d;
                integerDistribution = new GeometricDistribution(rng,p);
                break;
            default:
                throw new RuntimeException("samplerFunction " + distClass.getSimpleName() + " was not a supported samplerFunction. It's parameter mappings may not be implemented yet.");
        }

        return integerDistribution;

    }
}
