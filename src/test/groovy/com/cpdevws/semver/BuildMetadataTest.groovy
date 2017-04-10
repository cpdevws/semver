package com.cpdevws.semver

import com.cpdevws.semver.exceptions.InvalidFormatException
import org.junit.Test

/**
 * Author: cpdews (cpdevws@gmail.com)
 */
class BuildMetadataTest {

    @Test
    void should_parse_version_string_into_ids_when_all_parts_are_valid(){
        assert (new BuildMetadata("")).ids == null
        assert (new BuildMetadata("1.2.3-r4.4")).ids == ['1', '2', '3-r4', '4']
        assert (new BuildMetadata("alpha")).ids == ['alpha']
    }

    @Test(expected = InvalidFormatException.class)
    void should_raise_exception_on_parse_version_string_into_ids_when_the_str_has_invalid_character_in_id(){
        new BuildMetadata("1.2.3.012+-r4.4")
    }

    @Test
    void should_be_able_to_compare_two_string_versions() {
        assert null < new BuildMetadata()
        assert new BuildMetadata() == new BuildMetadata("  ")
        assert new BuildMetadata() == new BuildMetadata("123")
        assert new BuildMetadata("abc.123.asdsa.0012") == new BuildMetadata("123")
    }

}
