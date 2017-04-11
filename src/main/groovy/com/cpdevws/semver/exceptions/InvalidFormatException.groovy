package com.cpdevws.semver.exceptions

/**
 * Used to raise exceptions when the version is not properly formatted
 * as per the semver 2.0 rules
 *
 * @author cpdews (cpdevws@gmail.com)
 */
class InvalidFormatException extends Exception {

    /**
     * Class constructor with default message
     */
    InvalidFormatException(){
        this("Invalid format exception")
    }

    /**
     * Class constructor with custom message
     * @param message custom message to included in the exception
     */
    InvalidFormatException(String message){
        super(message)
    }

}