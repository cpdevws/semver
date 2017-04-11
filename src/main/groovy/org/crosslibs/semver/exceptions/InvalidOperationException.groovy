package org.crosslibs.semver.exceptions

/**
 * Used to raise exceptions when an unsupported operation is invoked
 * on the verison object
 *
 * @author cpdews (cpdevws@gmail.com)
 */
class InvalidOperationException extends Exception {

    /**
     * Class constructor with default message
     */
    InvalidOperationException(){
        this("Invalid operation exception")
    }

    /**
     * Class constructor with custom message
     * @param message custom message to included in the exception
     */
    InvalidOperationException(String message){
        super(message)
    }

}