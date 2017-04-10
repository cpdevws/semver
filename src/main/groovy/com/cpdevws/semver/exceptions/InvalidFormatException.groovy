package com.cpdevws.semver.exceptions

/**
 * Author: cpdews (cpdevws@gmail.com)
 */
class InvalidFormatException extends Exception {

    InvalidFormatException(){
        this("Invalid format exception")
    }

    InvalidFormatException(String message){
        super(message)
    }

}