package org.crosslibs.semver

import org.crosslibs.semver.exceptions.InvalidFormatException

/**
 * Implements the pre-release version string as part of the semantic versioning 2.0 rules
 *
 * @author cpdews (cpdevws@gmail.com)
 */
class PreReleaseVersion extends StringVersion {

    /**
     * Class constructor with optional parameter
     * @param preReleaseVersionStr pre-release version string
     */
    PreReleaseVersion(String preReleaseVersionStr = null){
        super(preReleaseVersionStr)
    }

    /**
     * Validation rules for pre-release version
     * @param idStr string id
     * @throws InvalidFormatException when id string contains an invalid character or is a non-zero number starting with 0
     */
    @Override
    void validateFormat(String idStr) throws InvalidFormatException {
        if (idStr ==~ /^0(\d+)$/) {
            throw new InvalidFormatException("Invalid version string id: numeric string IDs must not start with 0")
        }

        if (!(idStr ==~ /^[0-9a-zA-Z-]+$/)){
            throw new InvalidFormatException("Invalid version string id: invalid character used")
        }
    }

    /**
     * Compare two pre-release version objects
     * @param o other pre-release version to be compared against
     * @return 0 if equal, -ve if this is of lower precedence to o, +ve if this is of higher precedence to o.
     */
    @Override
    int compareTo(StringVersion o) {

        // If o is a release build
        if(o == null || o.isNotDefined()) {
            // Then it is of higher precedence, if the current is a pre-release build
            // Otherwise, both are release builds, hence of same precedence.
            return isNotDefined() ? 0 : -1
        }

        // If this is a release build, it is of higher precedence
        if(isNotDefined()) {
            return 1
        }

        // Both are pre-release builds
        def (ids1, oids1) = [[], []]
        ids?.each { ids1 << it }
        o?.ids?.each { oids1 << it }

        String id, oid
        while (ids1.size() > 0 && oids1.size() > 0) {
            (id, oid) = [ids1.remove(0), oids1.remove(0)]
            if (id != oid) {
                // Compare as numbers if they are numeric IDs
                if(id ==~ /^\d+$/ && oid ==~ /^\d+$/){
                    return id.toInteger().compareTo(oid.toInteger())
                }
                return id.compareTo(oid)
            }
        }

        if (ids1.size() > 0) {
            return ids1.remove(0).compareTo(DEFAULT_VERSION_STRING)
        }
        else if(oids1.size() > 0) {
            return DEFAULT_VERSION_STRING.compareTo(oids1.remove(0))
        }

        return 0
    }
}
