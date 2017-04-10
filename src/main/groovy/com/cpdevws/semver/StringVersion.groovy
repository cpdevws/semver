package com.cpdevws.semver

import com.cpdevws.semver.exceptions.InvalidFormatException

import javax.lang.model.element.NestingKind

/**
 * Author: cpdews (cpdevws@gmail.com)
 */
abstract class StringVersion implements Comparable<StringVersion> {

    def ids = null
    static final String DELIMITER = '.'
    static final String DEFAULT_VERSION_STRING = ''

    StringVersion(String stringVersionStr = null){
        parse(stringVersionStr)
    }

    void parse(String stringVersionStr) throws InvalidFormatException {
        if(stringVersionStr == null || (stringVersionStr.trim() == '')) {
            return
        }

        def ids = stringVersionStr.tokenize(DELIMITER)
        this.ids = []
        ids.each {
            validateFormat(it)
            this.ids << it
        }
    }

    abstract void validateFormat(String idStr) throws InvalidFormatException

    boolean isNotDefined(){
        return (ids == null) || (ids.size == 0)
    }

    boolean isDefined(){
        return !isNotDefined()
    }

    @Override
    String toString(){
        return isDefined() ? ids.join(DELIMITER) : DEFAULT_VERSION_STRING
    }

    @Override
    int compareTo(StringVersion o) {
        return 0
    }
}