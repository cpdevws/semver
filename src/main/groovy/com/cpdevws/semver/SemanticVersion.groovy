package com.cpdevws.semver

import static com.cpdevws.semver.NormalVersion.Type.*

/**
 * Author: cpdews (cpdevws@gmail.com)
 */
class SemanticVersion implements Comparable<SemanticVersion> {

    NormalVersion normalVersion
    PreReleaseVersion preReleaseVersion
    BuildMetadata buildMetadata

    static final String PRE_RELEASE_VERSION_DELIMITER = '-'
    static final String BUILD_METADATA_DELIMITER = '+'


    SemanticVersion(int major = 0, int minor = 0, int patch = 0, PreReleaseVersion preReleaseVersion = null, BuildMetadata buildMetadata = null){
        normalVersion = new NormalVersion(major, minor, patch)
        this.preReleaseVersion = preReleaseVersion
        this.buildMetadata = null
    }

    SemanticVersion(NormalVersion normalVersion, PreReleaseVersion preReleaseVersion = null, BuildMetadata buildMetadata = null){
        this.normalVersion = normalVersion
        this.preReleaseVersion = preReleaseVersion
        this.buildMetadata = buildMetadata
    }

    SemanticVersion(String semanticVersionStr) {
        parse(semanticVersionStr)
    }

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

    def next() {
        normalVersion++
        return this
    }

    SemanticVersion plus(int incrementPatchByValue){
        return new SemanticVersion(normalVersion + incrementPatchByValue)
    }

    def plus(SemanticVersion o){
        o = o ?: new SemanticVersion()
        return new SemanticVersion(this.normalVersion + o.normalVersion)
    }

    void increment(NormalVersion.Type type = PATCH){
        normalVersion.increment(type)
    }

    boolean isReleaseVersion(){
        return (preReleaseVersion == null) || (preReleaseVersion.isNotDefined())
    }

    boolean isPreReleaseVersion(){
        return !isReleaseVersion()
    }

    boolean hasNoBuildMetadata(){
        return (buildMetadata == null) || (buildMetadata.isNotDefined())
    }

    boolean hasBuildMetadata(){
        return !hasNoBuildMetadata()
    }

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
