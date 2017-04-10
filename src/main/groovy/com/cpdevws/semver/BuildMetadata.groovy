package com.cpdevws.semver

import com.cpdevws.semver.exceptions.InvalidFormatException

/**
 * Author: cpdews (cpdevws@gmail.com)
 */
class BuildMetadata extends StringVersion {

    BuildMetadata(String buildMetadataStr = null){
        super(buildMetadataStr)
    }

    @Override
    void validateFormat(String idStr) throws InvalidFormatException {
        if (!(idStr ==~ /^[0-9a-zA-Z-]+$/)){
            throw new InvalidFormatException("Invalid version string id: invalid character used")
        }
    }
}
