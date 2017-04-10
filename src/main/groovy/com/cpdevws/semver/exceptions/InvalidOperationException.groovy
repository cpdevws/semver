package com.cpdevws.semver.exceptions

/**
 * Author: cpdews (cpdevws@gmail.com)
 */
class InvalidOperationException extends Exception {

    InvalidOperationException(){
        this("Invalid operation exception")
    }

    InvalidOperationException(String message){
        super(message)
    }

}