package org.crosslibs.semver

import static org.crosslibs.semver.NormalVersion.Type.*

/**
 * Implements semantic versioning 2.0
 *
 * @author cpdews (cpdevws@gmail.com)
 */
class SemanticVersion implements Comparable<SemanticVersion> {

    /**
     * Normal version part of the semantic version
     */
    NormalVersion normalVersion

    /**
     * Pre-release part of the semantic version
     */
    PreReleaseVersion preReleaseVersion

    /**
     * Build metadata part of the semantic version
     */
    BuildMetadata buildMetadata

    /**
     * Separator between normal version and the pre-release version
     */
    static final String PRE_RELEASE_VERSION_DELIMITER = '-'

    /**
     * Separator between the normal / pre-release version and the build metadata
     */
    static final String BUILD_METADATA_DELIMITER = '+'

    /**
     * Class Constructor
     * @param major optional, uses 0 if not specified
     * @param minor optional, uses 0 if not specified
     * @param patch optional, uses 0 if not specified
     * @param preReleaseVersion optional, pre-release version
     * @param buildMetadata optional build metadata
     */
    SemanticVersion(int major = 0, int minor = 0, int patch = 0, PreReleaseVersion preReleaseVersion = null, BuildMetadata buildMetadata = null){
        this(new NormalVersion(major, minor, patch), preReleaseVersion, buildMetadata)
    }

    /**
     *  Class constructor, requires at least normal version to be specified
     * @param normalVersion mandatory, normal version
     * @param preReleaseVersion optional, pre-release version
     * @param buildMetadata optional, build metadata
     */
    SemanticVersion(NormalVersion normalVersion, PreReleaseVersion preReleaseVersion = null, BuildMetadata buildMetadata = null){
        this.normalVersion = normalVersion
        this.preReleaseVersion = preReleaseVersion
        this.buildMetadata = buildMetadata
    }

    /**
     * Class constructor
     * Parses the string representation of the semantic version to instantiate the object
     * @param semanticVersionStr semantic version in string format (e.g. '1.2.3-SNAPSHOT+bcef1ad3s')
     */
    SemanticVersion(String semanticVersionStr) {
        parse(semanticVersionStr)
    }

    /**
     * Parses the semantic version and populates the instance variables
     * @param semanticVersionStr semantic version in string format (e.g. '1.2.3-SNAPSHOT+bcef1ad3s')
     */
    void parse(String semanticVersionStr) {
        if(semanticVersionStr == null || semanticVersionStr.trim() == ''){
            return
        }

        String buildMetadataStr = null
        if(semanticVersionStr.indexOf(BUILD_METADATA_DELIMITER) != -1){
            buildMetadataStr = semanticVersionStr.substring(semanticVersionStr.indexOf(BUILD_METADATA_DELIMITER) + 1)
            semanticVersionStr = semanticVersionStr.substring(0, semanticVersionStr.indexOf(BUILD_METADATA_DELIMITER))
        }

        String preReleaseVersionStr = null
        if(semanticVersionStr.indexOf(PRE_RELEASE_VERSION_DELIMITER) != -1){
            preReleaseVersionStr = semanticVersionStr.substring(semanticVersionStr.indexOf(PRE_RELEASE_VERSION_DELIMITER) + 1)
            semanticVersionStr = semanticVersionStr.substring(0, semanticVersionStr.indexOf(PRE_RELEASE_VERSION_DELIMITER))
        }

        with {
            normalVersion = new NormalVersion(semanticVersionStr)
            preReleaseVersion = new PreReleaseVersion(preReleaseVersionStr)
            buildMetadata = new BuildMetadata(buildMetadataStr)
        }
    }

    /**
     * Overrides ++ operator. This allows for the patch to be incremented with ++ operator
     * @return the same object with incremented patch version
     */
    def next() {
        normalVersion++
        return this
    }

    /**
     * Overrides + operator. This allows for patch to be incremented by the specified increment
     * @param patchValueIncrement value to be added to the current patch version
     * @return a new semantic version object with the new patch version
     */
    SemanticVersion plus(int patchValueIncrement){
        def semanticVersion = new SemanticVersion(normalVersion + patchValueIncrement)
        semanticVersion.preReleaseVersion = this.preReleaseVersion
        semanticVersion.buildMetadata = this.buildMetadata
        return semanticVersion
    }

    /**
     * Increments the specified version as per semver 2.0 rules
     * @param versionType MAJOR, MINOR or PATCH(default)
     */
    void increment(NormalVersion.Type type = PATCH){
        normalVersion.increment(type)
    }

    /**
     * Checks if the semantic version represents a release version
     * @return true, if this pre-release version is either null or not defined, otherwise, false
     */
    boolean isReleaseVersion(){
        return (preReleaseVersion == null) || (preReleaseVersion.isNotDefined())
    }

    /**
     * Checks if the semantic version represents a pre-release version
     * @return true, if this pre-release version is defined, otherwise, false
     */
    boolean isPreReleaseVersion(){
        return !isReleaseVersion()
    }

    /**
     * Checks if the semantic version does not contain build metadata
     * @return true, if build metadata is either null or not defined, otherwise, false
     */
    boolean hasNoBuildMetadata(){
        return (buildMetadata == null) || (buildMetadata.isNotDefined())
    }

    /**
     * Checks if the semantic version contains build metadata
     * @return true, if build metadata is defined, otherwise, false
     */
    boolean hasBuildMetadata(){
        return !hasNoBuildMetadata()
    }

    /**
     * Returns the semantic version as a properly formatted string
     * @return semantic version in string format
     */
    @Override
    String toString(){
        String version = (normalVersion ?: new NormalVersion()).toString()
        if(isPreReleaseVersion()){
            version += PRE_RELEASE_VERSION_DELIMITER + preReleaseVersion.toString()
        }
        if(hasBuildMetadata()){
            version += BUILD_METADATA_DELIMITER + buildMetadata.toString()
        }

        return version
    }

    /**
     * Compares two semantic versions
     * @param o other semantic version to be compared against
     * @return 0 if both are the same, -ve if the current version is lower in precedence than o, +ve if the current version is of higher precedence than o
     */
    @Override
    int compareTo(SemanticVersion o) {
        o = o ?: new SemanticVersion()

        def normalVersion1 = normalVersion ?: new NormalVersion()
        if (normalVersion1 != o.normalVersion) {
            return normalVersion1.compareTo(o.normalVersion)
        }

        def preReleaseVersion1 = preReleaseVersion ?: new PreReleaseVersion()
        if (preReleaseVersion1 != o.preReleaseVersion) {
            return preReleaseVersion1.compareTo(o.preReleaseVersion)
        }

        def buildMetadata1 = buildMetadata ?: new BuildMetadata()
        return buildMetadata1.compareTo(o?.buildMetadata)
    }
}
