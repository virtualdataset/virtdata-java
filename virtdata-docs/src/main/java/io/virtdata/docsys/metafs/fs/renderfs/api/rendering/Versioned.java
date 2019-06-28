package io.virtdata.docsys.metafs.fs.renderfs.api.rendering;

/**
 * Versioned objects have a long property that can be used as a version.
 * A versioned object is considered valid <em>for a version</em> if
 * and only if it has the same version.
 */
public interface Versioned {

    /**
     * Get the version for this element. An element is considered valid <em>only</em>
     * for other elements of the same version.
     */
    long getVersion();

    /**
     * A versioned object is valid for another versioned object if they have
     * the same version number.
     * @param other Another version object to check validity against
     * @return true, if the versions match
     */
    default boolean isValidFor(Versioned other) {
        return getVersion()==other.getVersion();
    }

    /**
     * A versioned object is only valid for the specific version it has.
     * @param version The version number to check validity against
     * @return true, if the version matches the provided version
     */
    default boolean isValidFor(long version) {
        return getVersion()==version;
    }
}
