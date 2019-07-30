package io.virtdata.docsys.metafs.fs.renderfs.api.versioning;

/**
 * <p>Versions on elements are merely observable ways of asking for
 * when they were made. Version comparisons between
 * elements are used to determine if one element is possibly out of
 * sync with other elements.</p>
 *
 * <p>Elements can have multiple parts internally which each have their own version.
 * In this case, each element is tracked independently, and the version of
 * the whole set of internal parts is taken as the oldest or most conservative
 * value. The concept of <em>part</em> is abstract here. It is merely a way of
 * allowing a logical element to derive its intrinsic version directly from
 * the provided parts when possible.</p>
 *
 * <p>Elements can have an dependency on other elements which, when compared against,
 * determine whether the element is stale, or invalid after the dependent elements
 * have changed.</p>
 *
 * <p>A versioned element is taken to be valid or <em>not stale</em> if and only if
 * the highest version of dependencies is equal to or less than the lowest value
 * of all versioned parts.</p>
 *
 * <p>The value of a version in this implementation is assumed to be epoch milliseconds.</p>
 *
 * <p>Getting version data is a deferred call via closure in every case. The caller
 * has to determine whether to provide a lambda to a static value, or to delegate
 * to a way to read a current value when the version is needed. This allows for an interesting
 * and simplifying usage pattern for delegating version reads:</p>
 *
 * <p>At the time an element is created from its dependencies, the versions of those
 * dependencies at that time are read to determine the vintage of the new element.
 * This is a dynamic read of a property which is stored statically in the element
 * instance.</p>
 *
 *
 *
 */
public interface Versioned {

    /**
     * The version of an element can depend on more than one
     * property internally. Informally, the version of an element is
     * the the lowest value of all the version properties of that element.
     * This is a conservative back reference to the oldest contributing
     * piece of data for that element, such that invalidation occurs based
     * on the possibility that versions are not in sync, rather than any
     * specific proof that they aren't.
     *
     * @return The version of the oldest part of a versioned element.
     */
    long getVersion();

    /**
     *
     * @return
     */
//    List<Versioned> getVersionDependencies();

//    /**
//     * A version dependency is a Versioned element that this Versioned element
//     * depends on for validity.
//     *
//     * @param dep Another Versioned element which establishes valid versions for this one.
//     */
//    void addVersionDependency(Versioned... dep);

    /**
     * If all the versioned parts of this elements are equal or higher version than
     * all of the dependencies, then this element is valid. Said differently, if the
     * highest version of all dependencies is equal to or lower than the lowest version
     * of all the parts, then this element is valid.
     * @return true, if this element is valid with respect to dependencies.
     */
    boolean isValid();

//    boolean isValidFor(long version);
//    boolean isValidFor(Versioned other);

}
