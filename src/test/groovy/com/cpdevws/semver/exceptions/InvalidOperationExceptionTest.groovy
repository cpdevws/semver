package com.cpdevws.semver.exceptions

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException

/**
 * Author: cpdews (cpdevws@gmail.com)
 */
class InvalidOperationExceptionTest {

    def DEFAULT_MESSAGE = "Invalid operation exception"

    @Rule
    public ExpectedException expectedException = ExpectedException.none()

    @Before
    void setup(){
        expectedException.expect(InvalidOperationException)
    }

    @Test
    void should_raise_exception_with_default_message_when_default_constructor_is_used() throws InvalidOperationException {
        expectedException.expectMessage(DEFAULT_MESSAGE)
        throw new InvalidOperationException()
    }

    @Test
    void should_raise_exception_with_specific_message_when_message_is_passed_in_the_container() throws InvalidOperationException {
        def CUSTOM_MESSAGE = "Invalid operation"
        expectedException.expectMessage(CUSTOM_MESSAGE)
        throw new InvalidOperationException(CUSTOM_MESSAGE)
    }

}