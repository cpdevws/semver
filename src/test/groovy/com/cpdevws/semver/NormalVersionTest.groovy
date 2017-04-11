package com.cpdevws.semver

import com.cpdevws.semver.exceptions.InvalidFormatException
import com.cpdevws.semver.exceptions.InvalidOperationException
import org.junit.Before
import org.junit.Test;

/**
 * Author: cpdews (cpdevws@gmail.com)
 */
class NormalVersionTest {

    def normalVersion = new NormalVersion()

    @Before
    void setup_normal_version() {
        normalVersion.with {
            major = 1
            minor = 2
            patch = 3
        }
    }

    @Test
    void should_return_full_normal_version_when_converted_to_string() {
        assert new NormalVersion(1,2,3) == normalVersion
    }

    @Test
    void should_increment_major_version_by_1_when_major_version_is_incremented(){
        normalVersion.increment NormalVersion.Type.MAJOR
        assert new NormalVersion(2,0,0) == normalVersion
    }

    @Test
    void should_increment_minor_version_by_1_when_minor_version_is_incremented(){
        normalVersion.increment NormalVersion.Type.MINOR
        assert new NormalVersion(1,3,0) == normalVersion
    }

    @Test
    void should_increment_patch_version_by_1_when_patch_version_is_incremented(){
        normalVersion.increment NormalVersion.Type.PATCH
        assert new NormalVersion(1,2,4) == normalVersion
    }

    @Test
    void should_increment_patch_version_by_1_when_no_version_type_is_specified(){
        normalVersion.increment()
        assert new NormalVersion(1,2,4) == normalVersion
    }

    @Test
    void should_return_version_number_in_string_format() {
        assert "1.2.3" == normalVersion.toString()
    }

    @Test(expected = InvalidFormatException.class)
    void should_raise_invalid_format_exception_if_the_string_is_malformed(){
        new NormalVersion("1.2.")
    }

    @Test(expected = InvalidFormatException.class)
    void should_raise_invalid_format_exception_if_the_string_is_not_malformed_but_version_numbers_are_negative(){
        new NormalVersion("-1.2.-3")
    }

    @Test(expected = InvalidFormatException.class)
    void should_raise_exception_if_major_number_is_negative(){
        new NormalVersion(-1, 2, 3)
    }

    @Test(expected = InvalidFormatException.class)
    void should_raise_exception_if_minor_number_is_negative(){
        new NormalVersion(1, -2, 3)
    }

    @Test(expected = InvalidFormatException.class)
    void should_raise_exception_if_patch_number_is_negative(){
        new NormalVersion(1, 2, -3)
    }

    @Test
    void should_increment_patch_version_using_next_operator() {
        assert normalVersion++ == new NormalVersion(1, 2, 4)
        assert ++normalVersion == new NormalVersion(1, 2, 5)
    }

    @Test
    void should_increment_patch_version_when_a_non_negative_integer_is_added() {
        assert (normalVersion + 0) == new NormalVersion(1, 2, 3)
        assert (normalVersion + 1) == new NormalVersion(1, 2, 4)
    }

    @Test(expected = InvalidOperationException.class)
    void should_raise_invalid_operation_exception_when_a_negative_number_is_added() {
        normalVersion + (-1)
    }

    @Test
    void should_parse_string_into_normal_version_when_the_string_is_properly_formatted(){
        assert new NormalVersion("1") == new NormalVersion(1)
        assert new NormalVersion("1.2") == new NormalVersion(1, 2)
        assert new NormalVersion("1.2.3") == new NormalVersion(1, 2, 3)
    }

    @Test
    void should_be_able_to_compare_two_versions(){
        assert normalVersion > null
        assert new NormalVersion(1) > new NormalVersion()
        assert new NormalVersion(2) > new NormalVersion(1, 2, 3)
        assert new NormalVersion(1, 2) >= new NormalVersion(1, 1)
        assert new NormalVersion(1, 2) >= new NormalVersion(1, 2, 0)
        assert new NormalVersion(1) < new NormalVersion(1, 1)
        assert new NormalVersion(1, 1, 0) <= new NormalVersion(1, 1)
        assert new NormalVersion(1, 2, 3) == new NormalVersion(1, 2, 3)
    }

}