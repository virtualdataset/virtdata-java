# CATEGORY distributions
## Beta

See <a href="https://en.wikipedia.org/wiki/Beta_distribution">Wikipedia: Beta distribution</a>

See <a href="https://commons.apache.org/proper/commons-statistics/commons-statistics-distribution/apidocs/org/apache/commons/statistics/distribution/BetaDistribution.html">Commons JavaDoc: BetaDistribution</a>

- int -> Beta(double: alpha, double: beta, String... mods) -> double
- long -> Beta(double: alpha, double: beta, String... mods) -> double

## Binomial

See <a href="http://en.wikipedia.org/wiki/Binomial_distribution">Wikipedia: Binomial distribution</a>

See <a href="https://commons.apache.org/proper/commons-statistics/commons-statistics-distribution/apidocs/org/apache/commons/statistics/distribution/BinomialDistribution.html">Commons JavaDoc: BinomialDistribution</a>

- int -> Binomial(int: trials, double: p, String... modslist) -> int
- int -> Binomial(int: trials, double: p, String... modslist) -> long
- long -> Binomial(int: trials, double: p, String... modslist) -> int
- long -> Binomial(int: trials, double: p, String... modslist) -> long

## Cauchy

See <a href="http://en.wikipedia.org/wiki/Cauchy_distribution">Wikipedia: Cauchy_distribution</a>

See <a href="https://commons.apache.org/proper/commons-statistics/commons-statistics-distribution/apidocs/org/apache/commons/statistics/distribution/CauchyDistribution.html">Commons Javadoc: CauchyDistribution</a>

- int -> Cauchy(double: median, double: scale, String... mods) -> double
- long -> Cauchy(double: median, double: scale, String... mods) -> double

## ChiSquared

See <a href="https://en.wikipedia.org/wiki/Chi-squared_distribution">Wikipedia: Chi-squared distribution</a>

See <a href="https://commons.apache.org/proper/commons-statistics/commons-statistics-distribution/apidocs/org/apache/commons/statistics/distribution/ChiSquaredDistribution.html">Commons JavaDoc: ChiSquaredDistribution</a>

- int -> ChiSquared(double: degreesOfFreedom, String... mods) -> double
- long -> ChiSquared(double: degreesOfFreedom, String... mods) -> double

## ConstantContinuous

Always yields the same value.

See <a href="https://commons.apache.org/proper/commons-statistics/commons-statistics-distribution/apidocs/org/apache/commons/statistics/distribution/ConstantContinuousDistribution.html">Commons JavaDoc: ConstantContinuousDistribution</a>

- int -> ConstantContinuous(double: value, String... mods) -> double
- long -> ConstantContinuous(double: value, String... mods) -> double

## Enumerated

Creates a probability density given the values and optional weights provided, in "value:weight value:weight ..." form.
The weight can be elided for any value to use the default weight of 1.0d.

See <a href="http://commons.apache.org/proper/commons-math/apidocs/org/apache/commons/math4/distribution/EnumeratedRealDistribution.html">Commons JavaDoc: EnumeratedRealDistribution</a>

- int -> Enumerated(String: data, String... mods) -> double
  - *ex:* `Enumerated('1 2 3 4 5 6')` - *a fair six-sided die roll*
  - *ex:* `Enumerated('1:2.0 2 3 4 5 6')` - *an unfair six-sided die roll, where 1 has probability mass 2.0, and everything else has only 1.0*
- long -> Enumerated(String: data, String... mods) -> double
  - *ex:* `Enumerated('1 2 3 4 5 6')` - *a fair 6-sided die*
  - *ex:* `Enumerated('1:2.0 2 3 4 5:0.5 6:0.5')` - *an unfair fair 6-sided die, where ones are twice as likely, and fives and sixes are half as likely*

## Exponential

See <a href="https://en.wikipedia.org/wiki/Exponential_distribution">Wikipedia: Exponential distribution</a>

See <a href="https://commons.apache.org/proper/commons-statistics/commons-statistics-distribution/apidocs/org/apache/commons/statistics/distribution/ExponentialDistribution.html">Commons JavaDoc: ExponentialDistribution</a>

- int -> Exponential(double: mean, String... mods) -> double
- long -> Exponential(double: mean, String... mods) -> double

## F

See <a href="https://en.wikipedia.org/wiki/F-distribution">Wikipedia: F-distribution</a>

See <a href="https://commons.apache.org/proper/commons-statistics/commons-statistics-distribution/apidocs/org/apache/commons/statistics/distribution/FDistribution.html">Commons JavaDoc: FDistribution</a>

See <a href="http://mathworld.wolfram.com/F-Distribution.html">Mathworld: F-Distribution</a>

- int -> F(double: numeratorDegreesOfFreedom, double: denominatorDegreesOfFreedom, String... mods) -> double
- long -> F(double: numeratorDegreesOfFreedom, double: denominatorDegreesOfFreedom, String... mods) -> double

## Gamma

See <a href="https://en.wikipedia.org/wiki/Gamma_distribution">Wikipedia: Gamma distribution</a>

See <a href="https://commons.apache.org/proper/commons-statistics/commons-statistics-distribution/apidocs/org/apache/commons/statistics/distribution/GammaDistribution.html">Commons JavaDoc: GammaDistribution</a>

- int -> Gamma(double: shape, double: scale, String... mods) -> double
- long -> Gamma(double: shape, double: scale, String... mods) -> double

## Geometric

See <a href="http://en.wikipedia.org/wiki/Geometric_distribution">Wikipedia: Geometric distribution</a>

See <a href="https://commons.apache.org/proper/commons-statistics/commons-statistics-distribution/apidocs/org/apache/commons/statistics/distribution/GeometricDistribution.html">Commons JavaDoc: GeometricDistribution</a>

- int -> Geometric(double: p, String... modslist) -> int
- int -> Geometric(double: p, String... modslist) -> long
- long -> Geometric(double: p, String... modslist) -> int
- long -> Geometric(double: p, String... modslist) -> long

## Gumbel

See <a href="https://en.wikipedia.org/wiki/Gumbel_distribution">Wikipedia: Gumbel distribution</a>

See <a href="https://commons.apache.org/proper/commons-statistics/commons-statistics-distribution/apidocs/org/apache/commons/statistics/distribution/GumbelDistribution.html">Commons JavaDoc: GumbelDistribution</a>

- int -> Gumbel(double: mu, double: beta, String... mods) -> double
- long -> Gumbel(double: mu, double: beta, String... mods) -> double

## Hypergeometric

See <a href="http://en.wikipedia.org/wiki/Hypergeometric_distribution">Wikipedia: Hypergeometric distribution</a>

See <a href="https://commons.apache.org/proper/commons-statistics/commons-statistics-distribution/apidocs/org/apache/commons/statistics/distribution/HypergeometricDistribution.html">Commons JavaDoc: HypergeometricDistribution</a>

- int -> Hypergeometric(int: populationSize, int: numberOfSuccesses, int: sampleSize, String... modslist) -> int
- int -> Hypergeometric(int: populationSize, int: numberOfSuccesses, int: sampleSize, String... modslist) -> long
- long -> Hypergeometric(int: populationSize, int: numberOfSuccesses, int: sampleSize, String... modslist) -> int
- long -> Hypergeometric(int: populationSize, int: numberOfSuccesses, int: sampleSize, String... modslist) -> long

## Laplace

See <a href="https://en.wikipedia.org/wiki/Laplace_distribution">Wikipedia: Laplace distribution</a>

See <a href="https://commons.apache.org/proper/commons-statistics/commons-statistics-distribution/apidocs/org/apache/commons/statistics/distribution/LaplaceDistribution.html">Commons JavaDoc: LaplaceDistribution</a>

- int -> Laplace(double: mu, double: beta, String... mods) -> double
- long -> Laplace(double: mu, double: beta, String... mods) -> double

## Levy

See <a href="https://en.wikipedia.org/wiki/L%C3%A9vy_distribution">Wikipedia: Lévy distribution</a>

See <a href="https://commons.apache.org/proper/commons-statistics/commons-statistics-distribution/apidocs/org/apache/commons/statistics/distribution/LevyDistribution.html">Commons JavaDoc: LevyDistribution</a>

- int -> Levy(double: mu, double: c, String... mods) -> double
- long -> Levy(double: mu, double: c, String... mods) -> double

## LogNormal

See <a href="https://en.wikipedia.org/wiki/Log-normal_distribution">Wikipedia: Log-normal distribution</a>

See <a href="https://commons.apache.org/proper/commons-statistics/commons-statistics-distribution/apidocs/org/apache/commons/statistics/distribution/LogNormalDistribution.html">Commons JavaDoc: LogNormalDistribution</a>

- int -> LogNormal(double: scale, double: shape, String... mods) -> double
- long -> LogNormal(double: scale, double: shape, String... mods) -> double

## Logistic

See <a href="https://en.wikipedia.org/wiki/Logistic_distribution">Wikipedia: Logistic distribution</a>

See <a href="https://commons.apache.org/proper/commons-statistics/commons-statistics-distribution/apidocs/org/apache/commons/statistics/distribution/LogisticDistribution.html">Commons JavaDoc: LogisticDistribution</a>

- int -> Logistic(double: mu, double: scale, String... mods) -> double
- long -> Logistic(double: mu, double: scale, String... mods) -> double

## Nakagami

See <a href="https://en.wikipedia.org/wiki/Nakagami_distribution">Wikipedia: Nakagami distribution</a>

See <a href="https://commons.apache.org/proper/commons-statistics/commons-statistics-distribution/apidocs/org/apache/commons/statistics/distribution/NakagamiDistribution.html">Commons JavaDoc: NakagamiDistribution</a>

- int -> Nakagami(double: mu, double: omega, String... mods) -> double
- long -> Nakagami(double: mu, double: omega, String... mods) -> double

## Normal

See <a href="https://en.wikipedia.org/wiki/Normal_distribution">Wikipedia: Normal distribution</a>

See <a href="https://commons.apache.org/proper/commons-statistics/commons-statistics-distribution/apidocs/org/apache/commons/statistics/distribution/NormalDistribution.html">Commons JavaDoc: NormalDistribution</a>

- int -> Normal(double: mean, double: sd, String... mods) -> double
- long -> Normal(double: mean, double: sd, String... mods) -> double

## Pareto

See <a href="https://en.wikipedia.org/wiki/Pareto_distribution">Wikipedia: Pareto distribution</a>

See <a href="https://commons.apache.org/proper/commons-statistics/commons-statistics-distribution/apidocs/org/apache/commons/statistics/distribution/ParetoDistribution.html">Commons JavaDoc: ParetoDistribution</a>

- int -> Pareto(double: scale, double: shape, String... mods) -> double
- long -> Pareto(double: scale, double: shape, String... mods) -> double

## Pascal

See <a href="https://commons.apache.org/proper/commons-statistics/commons-statistics-distribution/apidocs/org/apache/commons/statistics/distribution/PascalDistribution.html">Commons JavaDoc: PascalDistribution</a>

See <a href="https://en.wikipedia.org/wiki/Negative_binomial_distribution">Wikipedia: Negative binomial distribution</a>

- int -> Pascal(int: r, double: p, String... modslist) -> int
- int -> Pascal(int: r, double: p, String... modslist) -> long
- long -> Pascal(int: r, double: p, String... modslist) -> int
- long -> Pascal(int: r, double: p, String... modslist) -> long

## Poisson

See <a href="http://en.wikipedia.org/wiki/Poisson_distribution">Wikipedia: Poisson distribution</a>

See <a href="https://commons.apache.org/proper/commons-statistics/commons-statistics-distribution/apidocs/org/apache/commons/statistics/distribution/PoissonDistribution.html">Commons JavaDoc: PoissonDistribution</a>

- int -> Poisson(double: p, String... modslist) -> int
- int -> Poisson(double: p, String... modslist) -> long
- long -> Poisson(double: p, String... modslist) -> int
- long -> Poisson(double: p, String... modslist) -> long

## T

See <a href="https://en.wikipedia.org/wiki/Student's_t-distribution">Wikipedia: Student's t-distribution</a>

See <a href="https://commons.apache.org/proper/commons-statistics/commons-statistics-distribution/apidocs/org/apache/commons/statistics/distribution/TDistribution.html">Commons JavaDoc: TDistribution</a>

- int -> T(double: degreesOfFreedom, String... mods) -> double
- long -> T(double: degreesOfFreedom, String... mods) -> double

## Triangular

See <a href="https://en.wikipedia.org/wiki/Triangular_distribution">Wikipedia: Triangular distribution</a>

See <a href="https://commons.apache.org/proper/commons-statistics/commons-statistics-distribution/apidocs/org/apache/commons/statistics/distribution/TriangularDistribution.html">Commons JavaDoc: TriangularDistribution</a>

- int -> Triangular(double: a, double: c, double: b, String... mods) -> double
- long -> Triangular(double: a, double: c, double: b, String... mods) -> double

## Uniform

See <a href="https://en.wikipedia.org/wiki/Uniform_distribution_(continuous)">Wikipedia: Uniform distribution (continuous)</a>

See <a href="https://commons.apache.org/proper/commons-statistics/commons-statistics-distribution/apidocs/org/apache/commons/statistics/distribution/UniformContinuousDistribution.html">Commons JavaDoc: UniformContinuousDistribution</a>

- int -> Uniform(double: lower, double: upper, String... mods) -> double
- long -> Uniform(double: lower, double: upper, String... mods) -> double
- int -> Uniform(int: lower, int: upper, String... modslist) -> int
- int -> Uniform(int: lower, int: upper, String... modslist) -> long
- long -> Uniform(int: lower, int: upper, String... modslist) -> int
- long -> Uniform(int: lower, int: upper, String... modslist) -> long

## Weibull

See <a href="https://en.wikipedia.org/wiki/Weibull_distribution">Wikipedia: Weibull distribution</a>

See <a href="http://mathworld.wolfram.com/WeibullDistribution.html">Wolfram Mathworld: Weibull Distribution</a>

See <a href="https://commons.apache.org/proper/commons-statistics/commons-statistics-distribution/apidocs/org/apache/commons/statistics/distribution/WeibullDistribution.html">Commons Javadoc: WeibullDistribution</a>

- int -> Weibull(double: alpha, double: beta, String... mods) -> double
- long -> Weibull(double: alpha, double: beta, String... mods) -> double

## Zipf

See <a href="https://en.wikipedia.org/wiki/Zipf's_law">Wikipedia: Zipf's Law</a>

See <a href="https://commons.apache.org/proper/commons-statistics/commons-statistics-distribution/apidocs/org/apache/commons/statistics/distribution/ZipfDistribution.html">Commons JavaDoc: ZipfDistribution</a>

- int -> Zipf(int: numberOfElements, double: exponent, String... modslist) -> int
- int -> Zipf(int: numberOfElements, double: exponent, String... modslist) -> long
- long -> Zipf(int: numberOfElements, double: exponent, String... modslist) -> int
- long -> Zipf(int: numberOfElements, double: exponent, String... modslist) -> long

