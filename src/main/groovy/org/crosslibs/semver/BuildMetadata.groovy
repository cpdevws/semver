package org.crosslibs.semver

import org.crosslibs.semver.exceptions.InvalidFormatException

/**
 * Implements the build metadata part of the semantic versioning 2.0
 *
 * Please note that all build metadata strings are of equal precedence
 *
 * @author cpdews (cpdevws@gmail.com)
 */
class BuildMetadata extends StringVersion {

    /**
     * Class constructor with optional parameter
     * @param buildMetadataStr build metadata
     */
    BuildMetadata(String buildMetadataStr = null){
        super(buildMetadataStr)
    }

    /**
     * Validates the format of the build metadata as per semver 2.0 rules
     * @param idStr string id which is a part of the build metadata
     * @throws InvalidFormatException when an character outside of 0-9,a-z,A-Z or hyphen(-) is present
     */
    @Override
    void validateFormat(String idStr) throws InvalidFormatException {
        if (!(idStr ==~ /^[0-9a-zA-Z-]+$/)){
            throw new InvalidFormatException("Invalid build metadata string id: invalid character used")
        }
    }
}
