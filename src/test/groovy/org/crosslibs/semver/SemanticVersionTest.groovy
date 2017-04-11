package org.crosslibs.semver

import org.crosslibs.semver.exceptions.InvalidOperationException
import org.crosslibs.semver.exceptions.InvalidFormatException
import org.junit.Before
import org.junit.Test

/**
 * @author cpdews (cpdevws@gmail.com)
 */
class SemanticVersionTest {

    def semanticVersion = new SemanticVersion()
    def normalVersion = new NormalVersion(1, 2, 3)
    PreReleaseVersion preReleaseVersion = null
    BuildMetadata buildMetadata = null

    @Before
    void setup() {
        semanticVersion.normalVersion = normalVersion
        preReleaseVersion = new PreReleaseVersion('SNAPSHOT-alpha.rc.1.2.3')
        buildMetadata = new BuildMetadata('0012.abc-123.0021')
    }

    @Test
    void should_return_true_for_release_build_when_pre_release_version_is_not_specified(){
        assert semanticVersion.isReleaseVersion() == true
    }

    @Test
    void should_return_false_for_pre_release_build_when_pre_release_version_is_not_specified(){
        assert semanticVersion.isPreReleaseVersion() == false
    }

    @Test
    void should_return_false_for_release_build_when_pre_release_version_is_specified(){
        semanticVersion.preReleaseVersion = preReleaseVersion
        assert semanticVersion.isReleaseVersion() == false
    }

    @Test
    void should_return_true_for_pre_release_build_when_pre_release_version_is_specified(){
        semanticVersion.preReleaseVersion = preReleaseVersion
        assert semanticVersion.isPreReleaseVersion() == true
    }


    @Test
    void should_return_false_for_has_build_metadata_when_build_metadata_is_not_specified(){
        assert semanticVersion.hasBuildMetadata() == false
    }

    @Test
    void should_return_true_for_has_no_build_metadata_when_build_metadata_is_not_specified(){
        assert semanticVersion.hasNoBuildMetadata() == true
    }

    @Test
    void should_return_true_for_has_build_metadata_when_build_metadata_is_specified(){
        semanticVersion.buildMetadata = buildMetadata
        assert semanticVersion.hasBuildMetadata() == true
    }

    @Test
    void should_return_false_for_has_no_build_metadata_when_build_metadata_is_specified(){
        semanticVersion.buildMetadata = buildMetadata
        assert semanticVersion.hasNoBuildMetadata() == false
    }


    @Test(expected = InvalidFormatException.class)
    void should_raise_invalid_format_exception_if_the_string_is_malformed(){
        new SemanticVersion("1.2.")
    }

    @Test(expected = InvalidFormatException.class)
    void should_raise_invalid_format_exception_if_the_string_is_not_malformed_but_version_numbers_are_negative(){
        new SemanticVersion("-1.2.-3")
    }

    @Test(expected = InvalidFormatException.class)
    void should_raise_exception_if_any_version_number_is_negative(){
        new SemanticVersion(-1, 2, -3)
    }

    @Test
    void should_return_normal_version_number_only_in_string_format_when_it_has_normal_version_only() {
        assert "1.2.3" == semanticVersion.toString()
    }

    @Test
    void should_return_normal_version_number_only_in_string_format_when_it_is_a_release_version() {
        semanticVersion.preReleaseVersion = new PreReleaseVersion()
        assert "1.2.3" == semanticVersion.toString()
    }

    @Test
    void should_return_normal_version_number_and_pre_release_version_in_string_format_when_it_has_a_pre_release_version() {
        semanticVersion.preReleaseVersion = preReleaseVersion
        assert "1.2.3-SNAPSHOT-alpha.rc.1.2.3" == semanticVersion.toString()
    }

    @Test
    void should_return_normal_version_number_and_build_metadata_in_string_format_when_it_has_build_metadata_only() {
        semanticVersion.buildMetadata = buildMetadata
        assert "1.2.3+0012.abc-123.0021" == semanticVersion.toString()
    }

    @Test
    void should_return_fully_formatted_string_when_both_pre_release_and_meta_data_are_also_present() {
        semanticVersion.preReleaseVersion = preReleaseVersion
        semanticVersion.buildMetadata = buildMetadata
        assert "1.2.3-SNAPSHOT-alpha.rc.1.2.3+0012.abc-123.0021" == semanticVersion.toString()
    }

    @Test
    void should_increment_patch_version_using_next_operator() {
        assert semanticVersion++ == new SemanticVersion(1, 2, 4)
        assert ++semanticVersion == new SemanticVersion(1, 2, 5)
    }

    @Test
    void should_increment_patch_version_when_a_non_negative_integer_is_added() {
        assert (semanticVersion + 0) == new SemanticVersion(1, 2, 3)
        assert (semanticVersion + 1) == new SemanticVersion(1, 2, 4)
    }

    @Test(expected = InvalidOperationException.class)
    void should_raise_invalid_operation_exception_when_a_negative_number_is_added() {
        semanticVersion + (-1)
    }

    @Test
    void should_increment_major_version_by_1_when_major_version_is_incremented(){
        semanticVersion.increment NormalVersion.Type.MAJOR
        assert new SemanticVersion(2,0,0) == semanticVersion
    }

    @Test
    void should_increment_minor_version_by_1_when_minor_version_is_incremented(){
        semanticVersion.increment NormalVersion.Type.MINOR
        assert new SemanticVersion(1,3,0) == semanticVersion
    }

    @Test
    void should_increment_patch_version_by_1_when_patch_version_is_incremented(){
        semanticVersion.increment NormalVersion.Type.PATCH
        assert new SemanticVersion(1,2,4) == semanticVersion
    }

    @Test
    void should_increment_patch_version_by_1_when_no_version_type_is_specified(){
        semanticVersion.increment()
        assert new SemanticVersion(1,2,4) == semanticVersion
    }

    @Test
    void should_parse_string_into_semantic_version_when_the_string_is_properly_formatted(){
        assert new SemanticVersion("1") == new SemanticVersion(1)
        assert new SemanticVersion("1.2") == new SemanticVersion(1, 2)
        assert new SemanticVersion("1.2.3") == new SemanticVersion(1, 2, 3)
        assert new SemanticVersion("1.2.3-SNAPSHOT") == new SemanticVersion(1, 2, 3, new PreReleaseVersion('SNAPSHOT'))
        assert new SemanticVersion('1.0.0-SNAPSHOT') == new SemanticVersion(1, 0, 0, new PreReleaseVersion('SNAPSHOT'))
        assert new SemanticVersion('1.0.0-SNAPSHOT+abc.123-345') == new SemanticVersion(1, 0, 0, new PreReleaseVersion('SNAPSHOT'), new BuildMetadata('abc.123-345'))
        assert new SemanticVersion('1.0.0+SNAPSHOT') == new SemanticVersion(1, 0, 0, null, new BuildMetadata('SNAPSHOT'))
    }

    @Test
    void should_be_able_to_compare_two_versions(){
        assert semanticVersion > null
        assert new SemanticVersion(1) > new SemanticVersion()
        assert new SemanticVersion(2) > new SemanticVersion(1, 2, 3)
        assert new SemanticVersion(1, 2) >= new SemanticVersion(1, 1)
        assert new SemanticVersion(1, 2) >= new SemanticVersion(1, 2, 0)
        assert new SemanticVersion(1) < new SemanticVersion(1, 1)
        assert new SemanticVersion(1, 1, 0) <= new SemanticVersion(1, 1)
        assert new SemanticVersion(1, 2, 3) == new SemanticVersion(1, 2, 3)
        assert new SemanticVersion(new NormalVersion(1,2,3)) > new SemanticVersion(new NormalVersion(0, 2, 3))
        assert new SemanticVersion() == new SemanticVersion()
        assert new SemanticVersion(1, 2, 0, preReleaseVersion) < new SemanticVersion(1, 2, 0)
        assert new SemanticVersion('1.2.0') >= new SemanticVersion('1.2.0-SNAPSHOT')
        assert new SemanticVersion('1.2.0-SNAPSHOT.12') > new SemanticVersion('1.2.0-SNAPSHOT.2')
        assert new SemanticVersion('') == new SemanticVersion('   ')
        assert new SemanticVersion('1.2.3+123') == new SemanticVersion('1.2.3+345')
        assert new SemanticVersion('1.2.3+123') == new SemanticVersion('1.2.3')
        assert new SemanticVersion('1.2.3-1.2.3+123') == new SemanticVersion('1.2.3-1.2.3')
        assert new SemanticVersion('1.2.3-1.2.3+123') == new SemanticVersion('1.2.3-1.2.3+abcd')
    }

}
