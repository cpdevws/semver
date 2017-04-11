package org.crosslibs.semver

import org.crosslibs.semver.exceptions.InvalidFormatException
import org.junit.Test

/**
 * @author cpdews (cpdevws@gmail.com)
 */
class StringVersionTest {

    class StringVersionImpl extends StringVersion {

        StringVersionImpl(String versionStr = null){
            super(versionStr)
        }

        @Override
        void validateFormat(String idStr) throws InvalidFormatException {
            // Allow everything
        }
    }

    @Test
    void should_parse_version_string_into_ids_when_all_parts_are_valid(){
        def stringVersion = new StringVersionImpl("1.2.3.4")
        assert stringVersion.ids == ['1', '2', '3', '4']
    }

    @Test
    void should_parse_version_string_with_out_error_when_version_string_is_null(){
        def stringVersion = new StringVersionImpl()
        assert stringVersion.ids == null
    }

    @Test
    void should_set_version_string_as_undefined_when_version_string_is_null(){
        assert (new StringVersionImpl()).isNotDefined() == true
    }

    @Test
    void should_set_version_string_as_not_defined_when_version_string_is_null(){
        assert (new StringVersionImpl()).isDefined() == false
    }

    @Test
    void should_set_version_string_as_undefined_when_version_string_is_empty_or_only_has_spaces(){
        assert (new StringVersionImpl("")).isNotDefined() == true
        assert (new StringVersionImpl("     ")).isNotDefined() == true
    }

    @Test
    void should_set_version_string_as_not_defined_when_version_string_is_empty_or_only_has_spaces(){
        assert (new StringVersionImpl("")).isDefined() == false
        assert (new StringVersionImpl("    ")).isDefined() == false
    }

    @Test
    void should_set_version_string_as_defined_when_version_string_is_not_empty(){
        assert (new StringVersionImpl("123")).isNotDefined() == false
    }

    @Test
    void should_set_version_string_as_not_undefined_when_version_string_is_not_empty(){
        assert (new StringVersionImpl("123")).isDefined() == true
    }

    @Test
    void should_return_default_string_when_version_is_not_defined(){
        assert (new StringVersionImpl()).toString() == StringVersion.DEFAULT_VERSION_STRING
        assert (new StringVersionImpl('')).toString() == StringVersion.DEFAULT_VERSION_STRING
        assert (new StringVersionImpl('   ')).toString() == StringVersion.DEFAULT_VERSION_STRING
    }

    @Test
    void should_return_formatted_string_without_delimiter_when_version_is_defined_with_one_id(){
        assert (new StringVersionImpl('123')).toString() == '123'
    }

    @Test
    void should_return_formatted_string_with_delimiter_when_version_is_defined_more_than_one_id(){
        assert (new StringVersionImpl('123.324')).toString() == '123.324'
    }

    @Test
    void should_be_able_to_compare_two_string_versions(){
        assert new StringVersionImpl() == new StringVersionImpl("  ")
        assert new StringVersionImpl() == new StringVersionImpl('123')
        assert new StringVersionImpl('123') == new StringVersionImpl('1abc')
        assert new StringVersionImpl('123') == new StringVersionImpl('123')
        assert new StringVersionImpl('123.r1') == new StringVersionImpl('123.0.1')
        assert new StringVersionImpl('123.r1') == new StringVersionImpl('123')
        assert new StringVersionImpl('123.r1') == new StringVersionImpl('123.r1')
    }
}
