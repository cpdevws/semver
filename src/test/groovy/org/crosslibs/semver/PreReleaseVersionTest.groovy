package org.crosslibs.semver

import org.crosslibs.semver.exceptions.InvalidFormatException
import org.junit.Test

/**
 * @author cpdews (cpdevws@gmail.com)
 */
class PreReleaseVersionTest {

    @Test
    void should_parse_version_string_into_ids_when_all_parts_are_valid(){
        assert (new PreReleaseVersion("")).ids == null
        assert (new PreReleaseVersion("1.2.3-r4.4")).ids == ['1', '2', '3-r4', '4']
        assert (new PreReleaseVersion("alpha")).ids == ['alpha']
    }

    @Test(expected = InvalidFormatException.class)
    void should_raise_exception_on_parse_version_string_into_ids_when_the_str_has_malformed_id(){
        new PreReleaseVersion("1.2.3.012.-r4.4")
    }

    @Test(expected = InvalidFormatException.class)
    void should_raise_exception_on_parse_version_string_into_ids_when_the_str_has_invalid_character(){
        new PreReleaseVersion("1.2.3.12+-r4.4")
    }

    @Test
    void should_be_able_to_compare_two_string_versions(){
        assert null < new PreReleaseVersion()
        assert new PreReleaseVersion() == new PreReleaseVersion("  ")
        assert new PreReleaseVersion('123') <= new PreReleaseVersion('1abc')
        assert new PreReleaseVersion('123') == new PreReleaseVersion('123')
        assert new PreReleaseVersion('123.r1') > new PreReleaseVersion('123.0.1')
        assert new PreReleaseVersion('123.r1') >= new PreReleaseVersion('123')
        assert new PreReleaseVersion('123.r1') == new PreReleaseVersion('123.r1')
        assert new PreReleaseVersion('123') >= new PreReleaseVersion('23')
        assert new PreReleaseVersion('abc.123') >= new PreReleaseVersion('abc.45')
        assert new PreReleaseVersion() > new PreReleaseVersion('SNAPSHOT')
        assert new PreReleaseVersion() == new PreReleaseVersion('')
        assert new PreReleaseVersion('123') <= new PreReleaseVersion('123.321')
    }


}
