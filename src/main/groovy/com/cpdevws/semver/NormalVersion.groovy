package com.cpdevws.semver

import com.cpdevws.semver.exceptions.InvalidFormatException
import com.cpdevws.semver.exceptions.InvalidOperationException

/**
*  Author: cpdews (cpdevws@gmail.com)
*/
class NormalVersion implements Comparable<NormalVersion> {

     enum Type {
        MAJOR,
        MINOR,
        PATCH
    }

    int major = 0
    int minor = 0
    int patch = 0

    static final String DELIMITER = "."

    NormalVersion(){
        this(0,0,0)
    }

    NormalVersion(int major){
        this(major, 0, 0)
    }

    NormalVersion(int major, int minor){
        this(major, minor, 0)
    }

    NormalVersion(int major, int minor, int patch){
        setMajor(major)
        setMinor(minor)
        setPatch(patch)
    }

    NormalVersion(String normalVersionStr){
        parse(normalVersionStr)
    }

    void parse(String version) throws InvalidFormatException {
        version = version?.trim()
        def matcher = (version =~ /^(\d+)($DELIMITER(\d+)($DELIMITER(\d+))?)?$/)
        if(!matcher) {
            throw new InvalidFormatException("Invalid normal version format")
        }

        with {
            major = intValue(matcher[0][1] as String)
            minor = intValue(matcher[0][3] as String)
            patch = intValue(matcher[0][5] as String)
        }
    }

    void setMajor(int majorValue) throws InvalidFormatException {
        assertNonNegative(majorValue)
        this.major = majorValue
    }

    void setMinor(int minorValue) throws InvalidFormatException {
        assertNonNegative(minorValue)
        this.minor = minorValue
    }

    void setPatch(int patchValue) throws InvalidFormatException {
        assertNonNegative(patchValue)
        this.patch = patchValue
    }

    void assertNonNegative(int value) throws InvalidFormatException {
        if (value < 0) {
            throw new InvalidFormatException("Version number can not be negative")
        }
    }

    def intValue(String str){
        return str == null ? 0 : str.toInteger()
    }

    def incrementMajorVersion() {
        major = major + 1
        minor = patch = 0
    }

    def incrementMinorVersion() {
        minor = minor + 1
        patch = 0
    }

    def incrementPatchVersion() {
        patch = patch + 1
    }


    def increment(Type versionType = Type.PATCH) {

        if(versionType == Type.MAJOR) {
            incrementMajorVersion()
        }
        else if(versionType == Type.MINOR) {
            incrementMinorVersion()
        }
        else {
            incrementPatchVersion()
        }
    }

    NormalVersion plus(NormalVersion o) {
        o = o ?: new NormalVersion()
        return new NormalVersion(major + o.major, minor + o.minor, patch + o.patch)
    }

    NormalVersion plus(int patchValueIncrement) {
        if(patchValueIncrement < 0) {
            throw new InvalidOperationException("Decrement version operation is not supported")
        }
        return new NormalVersion(major, minor, patch + patchValueIncrement)
    }

    def next() {
        increment()
        return this
    }

    @Override
    String toString() {
        return major + DELIMITER + minor + DELIMITER + patch
    }

    @Override
    int compareTo(NormalVersion o) {

        o = o ?: new NormalVersion()

        if(o.major != major) {
            return major - o.major
        }

        if(o.minor != minor) {
            return minor - o.minor
        }

        return patch - o.patch
    }
}