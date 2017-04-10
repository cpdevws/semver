package com.cpdevws.semver

import com.cpdevws.semver.exceptions.InvalidFormatException

/**
 * Author: cpdews (cpdevws@gmail.com)
 */
class PreReleaseVersion extends StringVersion {

    PreReleaseVersion(String preReleaseVersionStr = null){
        super(preReleaseVersionStr)
    }

    @Override
    void validateFormat(String idStr) throws InvalidFormatException {
        if (idStr ==~ /^0(\d+)$/) {
            throw new InvalidFormatException("Invalid version string id: numeric string IDs must not start with 0")
        }

        if (!(idStr ==~ /^[0-9a-zA-Z-]+$/)){
            throw new InvalidFormatException("Invalid version string id: invalid character used")
        }
    }

    @Override
    int compareTo(StringVersion o) {

        // Comparing with a release version
        if(o == null || o.isNotDefined()) {
            return isNotDefined() ? 0 : -1
        }

        // Clearly this is a release build
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
