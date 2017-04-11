package com.cpdevws.semver

import com.cpdevws.semver.exceptions.InvalidFormatException

import javax.lang.model.element.NestingKind

/**
 * Abstract class, provides base for implementing string IDs
 * as per semantic versioning rules 2.0 (e.g. Pre-release String ID, Build Metadata)
 *
 * @author cpdews (cpdevws@gmail.com)
 */
abstract class StringVersion implements Comparable<StringVersion> {

    /***
     * List of string ids that make up the string version
     */
    def ids = null

    /**
     * Joiner for string ids
     */
    static final String DELIMITER = '.'

    /**
     * Default string id is blank (also implies, not defined)
     */
    static final String DEFAULT_VERSION_STRING = ''

    /**
     * Class constructor
     * @param stringVersionStr optional, String version ID
     */
    StringVersion(String stringVersionStr = null){
        parse(stringVersionStr)
    }

    /**
     * Parses the version string into various constituent string ids and validates each string ID that it conforms
     * to semver 2.0 rules
     * @param stringVersionStr version string
     * @throws InvalidFormatException if validateFormat throws InvalidFormatException
     */
    void parse(String stringVersionStr) throws InvalidFormatException {
        if(stringVersionStr == null || (stringVersionStr.trim() == DEFAULT_VERSION_STRING)) {
            return
        }

        def ids = stringVersionStr.tokenize(DELIMITER)
        this.ids = []
        ids.each {
            validateFormat(it)
            this.ids << it
        }
    }

    /**
     * Abstract method the extending classes need to implement
     * @param idStr string id
     * @throws InvalidFormatException if idStr is malformed
     */
    abstract void validateFormat(String idStr) throws InvalidFormatException

    /**
     * Checks if the version string is not defined
     * @return true if there are no constituent id strings, otherwise, returns false
     */
    boolean isNotDefined(){
        return (ids == null) || (ids.size == 0)
    }

    /**
     * Checks if the version string is defined
     * @return true if there are non-blank constituent id strings, otherwise, returns false
     */
    boolean isDefined(){
        return !isNotDefined()
    }

    /**
     * Returns the string formatted version string
     * If the version string is not defined, the default version string ('') is returned
     * @return version string in string format
     */
    @Override
    String toString(){
        return isDefined() ? ids.join(DELIMITER) : DEFAULT_VERSION_STRING
    }

    /**
     * Compares one string version to another string version
     * @param o Other string version against which to compare the current object
     * @return always 0, in the default implementation
     */
    @Override
    int compareTo(StringVersion o) {
        return 0
    }
}