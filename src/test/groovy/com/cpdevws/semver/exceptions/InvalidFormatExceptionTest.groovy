package com.cpdevws.semver.exceptions

import org.junit.Before
import org.junit.Rule
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Author: cpdews (cpdevws@gmail.com)
 */
class InvalidFormatExceptionTest {

    def DEFAULT_MESSAGE = "Invalid format exception"

    @Rule
    public ExpectedException expectedException = ExpectedException.none()

    @Before
    void setup(){
        expectedException.expect(InvalidFormatException)
    }

    @Test
    void should_raise_exception_with_default_message_when_default_constructor_is_used() throws InvalidFormatException {
        expectedException.expectMessage(DEFAULT_MESSAGE)
        throw new InvalidFormatException()
    }

    @Test
    void should_raise_exception_with_specific_message_when_message_is_passed_in_the_container() throws InvalidFormatException {
        def CUSTOM_MESSAGE = "Invalid format"
        expectedException.expectMessage(CUSTOM_MESSAGE)
        throw new InvalidFormatException(CUSTOM_MESSAGE)
    }

}