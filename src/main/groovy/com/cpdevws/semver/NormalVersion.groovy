package com.cpdevws.semver

import com.cpdevws.semver.exceptions.InvalidFormatException
import com.cpdevws.semver.exceptions.InvalidOperationException

/**
 * Implements the normal version part of the semantic versioning 2.0
 *
 * @author cpdews (cpdevws@gmail.com)
 */
class NormalVersion implements Comparable<NormalVersion> {

    /**
     * Normal version types: major, minor and patch
     */
     enum Type {
        MAJOR,
        MINOR,
        PATCH
    }

    /**
     * Default major version is 0
     */
    int major = 0

    /**
     * Default minor version is 0
     */
    int minor = 0

    /**
     * Default patch version is 0
     */
    int patch = 0

    /**
     * Delimiter used to join the major, minor and patch versions
     */
    static final String DELIMITER = "."

    /**
     * Class constructor
     * @param major optional, uses 0, if not specified
     * @param minor optional, uses 0, if not specified
     * @param patch optional, uses 0, if not specified
     */
    NormalVersion(int major = 0, int minor = 0, int patch = 0){
        setMajor(major)
        setMinor(minor)
        setPatch(patch)
    }

    /**
     * Class constructor with normal version in string format
     * @param normalVersionStr normal version in string format (e.g. '1.2.3' or '1' or '1.2')
     */
    NormalVersion(String normalVersionStr){
        parse(normalVersionStr)
    }

    /**
     * Parses the normal version string to normal version object
     * @param version norma version as string (e.g. '1.2.3' or '1' or '1.2')
     * @throws InvalidFormatException when normal version string is not of the format <major> or <major>.<minor> or <major>.<minor>.<patch>
     */
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

    /**
     * Validate and set major version
     * @param major major version
     * @throws InvalidFormatException when major version is negative
     */
    void setMajor(int major) throws InvalidFormatException {
        assertNonNegative(major)
        this.major = major
    }

    /**
     * Validate and set minor version
     * @param minor minor version
     * @throws InvalidFormatException when minor version is negative
     */
    void setMinor(int minor) throws InvalidFormatException {
        assertNonNegative(minor)
        this.minor = minor
    }

    /**
     * Validate and set patch version
     * @param patch patch version
     * @throws InvalidFormatException when patch version is negative
     */
    void setPatch(int patch) throws InvalidFormatException {
        assertNonNegative(patch)
        this.patch = patch
    }

    /**
     * Validates that the value is a non-negative number
     * @param value value to be validated
     * @throws InvalidFormatException when value is negative
     */
    void assertNonNegative(int value) throws InvalidFormatException {
        if (value < 0) {
            throw new InvalidFormatException("Version number can not be negative")
        }
    }

    /**
     * Converts string to an integer. If a null string is specfied, 0 is returned
     * @param str string to be converted into an integer
     * @return integer value of the string
     */
    def intValue(String str){
        return str == null ? 0 : str.toInteger()
    }

    /**
     * Increments the major version as per semver 2.0 rules
     */
    void incrementMajorVersion() {
        major = major + 1
        minor = patch = 0
    }

    /**
     * Increments the minor version as per semver 2.0 rules
     */
    void incrementMinorVersion() {
        minor = minor + 1
        patch = 0
    }

    /**
     * Increments the patch version as per semver 2.0 rules
     */
    void incrementPatchVersion() {
        patch = patch + 1
    }

    /**
     * Increments the specified version as per semver 2.0 rules
     * @param versionType MAJOR, MINOR or PATCH(default)
     */
    void increment(Type versionType = Type.PATCH) {

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

    /**
     * Overrides + operator. This allows for patch to be incremented by the specified increment
     * @param patchValueIncrement value to be added to the current patch version
     * @return a new normal version object with the new patch version
     */
    NormalVersion plus(int patchValueIncrement) {
        if(patchValueIncrement < 0) {
            throw new InvalidOperationException("Decrement version operation is not supported")
        }
        return new NormalVersion(major, minor, patch + patchValueIncrement)
    }

    /**
     * Overrides ++ operator. This allows for the patch to be incremented with ++ operator
     * @return the same object with incremented patch version
     */
    def next() {
        increment()
        return this
    }

    /**
     * Returns the normal version in string format
     * @return the normal version in string format
     */
    @Override
    String toString() {
        return major + DELIMITER + minor + DELIMITER + patch
    }

    /**
     * Compares two normal versions
     * @param o other normal version to be compared against
     * @return 0 if both are the same, -ve if the current object is lower in precedence than o, +ve if the current object is of higher precedence than o
     */
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