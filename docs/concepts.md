Metagen Concepts
================

Metagen is a library for the flexible management and expressive use of procedural generation libraries. It is a reincarnation of a previous project. This version of the idea starts by focusing directly on usage aspects and extension points rather than the big idea.

### Procedural Generation

Procedural generation is a general class of methods for taking a set of inputs and modifying them in a predictable way to generate content which appears random but is actually deterministic. For example, some games use procedural generation to take a single value known as the "seed" to generate an apparently rich and interesting world.

### Apparently Random RNGs

Sequences of values produced by RNGs (more properly called PRNGs) are not actually random, even though they may pass certain tests for randomness. In practice, the combination of these two properties is quite valuable for testing and data synthesis. Having a stream of data that is measurably random by some meaningful standard, but which is configurable and reusable allows for test to be replayed, for example.

### Apparently Random Samples

Just as RNGs can appear random when the are not truly, statistical distributions which rely on them can also appear random. Uniform random number generators over the unit interval [0,1.0) are a common input to virtual sampling methods. This means that if you can configure the RNG stream that you feed into your virtual sampling methods, you can simulate a repeatable sequence from a known distribution.

### Generator Function

The generator function is the core building block of metagen. Generator functions are the functional logic that powers all procedural generation. The core data sequences that come from the RNG and statistical layers may not be purely functional, but it makes sense for the higher-level generator functions to be. This simply means that a generator function will always provide the same result given the same input. Generator functions all take a long value as their input, and produce a result based on their parameterized type.

### Generator Library

Generator functions are packaged into libraries which can be loaded by the metagen-user component of the project. Each library has a name, a function resolver, and a set of functions that can be instantiated via the function resolver.

### Function Resolver

Each library must implement its own function resolver. This is because each library may have a different way of naming, finding, creating or managing function generator instances. For the user, the description of a generator is simply a string. What the generator library does with it is implementation-specific. This means that some generator libraries may simply have constructor signatures as function specifiers, and others may go as far as implementing their own DSL. The basic contract for a function resolver is that you pass it a string describing what you want, and it provides a generator function in return.

#### Bindings Template

It is often useful to have a template that describes a set of generator functions that can be reused across many threads or other application scopes. A bindings template is a way to capture the requested generator functions for re-use, with actual scope instantiation of the generator functions controlled by the usage point. For example, in a JEE app, you may have a bindings template in the application scope, and a set of actual bindings within each request (thread scope).
