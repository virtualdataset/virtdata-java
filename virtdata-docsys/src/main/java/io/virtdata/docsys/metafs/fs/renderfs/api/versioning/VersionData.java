package io.virtdata.docsys.metafs.fs.renderfs.api.versioning;

import java.util.ArrayList;
import java.util.List;
import java.util.function.LongSupplier;

/**
 * VersionsData provides a default implementation of the Versioned interface.
 *
 */
public class VersionData implements Versioned {

    private final LongSupplier[] partVersions;
    private final LongSupplier[] dependencyVersions;
    private final List<Versioned> dependencies = new ArrayList<>();

    public VersionData() {
        this.partVersions = new LongSupplier[]{() -> Long.MIN_VALUE};
        this.dependencyVersions = new LongSupplier[]{};
    }

    /**
     * Initialize versions with multiple parts, each having their own way
     * of reading a version value.
     * @param versionSources version suppliers for each part of this whole element
     */
    public VersionData(LongSupplier... versionSources) {
        this.partVersions = versionSources;
        this.dependencyVersions = new LongSupplier[0];
    }

    /**
     * Initialize versions with dependencies only. In this form,
     * the effective version of this element is calculated
     * statically as the lowest version of all dependencies at
     * the time of the call.
     * @param dependencies The dependencies of this element as its parts.
     */
    public VersionData(Versioned... dependencies) {
        this.dependencyVersions = new LongSupplier[dependencies.length];

        for (int depidx = 0; depidx < dependencies.length; depidx++) {
            int lambdaIdx = depidx;
            dependencyVersions[depidx] = () -> dependencies[lambdaIdx].getVersion();
        }
        long effectivePartsVersion = getMaxDependencyVersion();
        this.partVersions = new LongSupplier[] { () -> effectivePartsVersion };
    }

    /**
     * Initialize version data with a separate version source for this element's
     * computed state, as well as references to the dependencies which that state
     * may rely on.
     * @param thisVersionSource A way to derive this element's version value.
     * @param dependencies A set of dependencies which may invalidate the state of this element.
     */
    public VersionData(LongSupplier thisVersionSource, Versioned... dependencies) {
        this.partVersions= new LongSupplier[] {thisVersionSource};
        this.dependencyVersions = new LongSupplier[dependencies.length];

        for (int depidx = 0; depidx < dependencies.length; depidx++) {
            int lambdaIdx = depidx;
            dependencyVersions[depidx] = () -> dependencies[lambdaIdx].getVersion();
        }

    }

    /**
     * Get the version for this element. An element is considered valid <em>only</em>
     * for other elements of the same version.
     *
     * @return A long version for the versioned element.
     */
    @Override
    public long getVersion() {
        long minVersion = Long.MAX_VALUE;
        for (int i = 0; i < partVersions.length; i++) {
            minVersion = Math.min(minVersion,partVersions[i].getAsLong());
        }
        return minVersion;
    }

    public long getMaxDependencyVersion() {
        if (dependencyVersions.length==0) {
            return Long.MAX_VALUE;
        } else {
            long maxVersion = Long.MIN_VALUE;
            for (int i = 0; i < dependencyVersions.length; i++) {
                maxVersion = Math.max(maxVersion, dependencyVersions[i].getAsLong());
            }
            return maxVersion;
        }
    }

    public void addVersionDependency(Versioned... dep) {
        for (Versioned versions : dep) {
            this.dependencies.add(versions);
        }
    }

    @Override
    public boolean isValid() {
        return getVersion() >= getMaxDependencyVersion();
    }

}
