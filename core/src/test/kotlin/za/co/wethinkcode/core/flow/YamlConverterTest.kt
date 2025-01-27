package za.co.wethinkcode.core.flow

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class YamlConverterTest {
    val builder = YamlConverter()

    @Test
    fun `fromYaml with non-yaml text`() {
        val entry = builder.convert("")
        assertThat(entry.messages).containsExactly(YamlConverter.INVALID_YAML)
    }

    @Test
    fun `fromYaml with perfect test run`() {
        val yaml = """
            ---
            branch: branch
            committer: Committer
            email: CommitterEmail
            type: test
            timestamp: 'Timestamp'
            passes:
            - passed
            fails:
            - failed
            disables:
            - disabled
            aborts:
            - aborted
        """.trimIndent()
        val record = builder.convert(yaml)
        with(record) {
            assertThat(type).isEqualTo(RunType.test)
            assertThat(branch).isEqualTo("branch")
            assertThat(committer).isEqualTo("Committer")
            assertThat(email).isEqualTo("CommitterEmail")
            assertThat(timestamp).isEqualTo("Timestamp")
            assertThat(passes).containsExactly("passed")
            assertThat(fails).containsExactly("failed")
            assertThat(disables).containsExactly("disabled")
            assertThat(aborts).containsExactly("aborted")
            assertThat(messages).isEmpty()
            assertThat(isError).isFalse
        }
    }


    @Test
    fun `fromYaml with perfect main run`() {
        val yaml = """
            ---
            branch: branch
            committer: Committer
            email: CommitterEmail
            type: run
            timestamp: 'Timestamp'
        """.trimIndent()
        val record = builder.convert(yaml)
        with(record) {
            assertThat(type).isEqualTo(RunType.run)
            assertThat(branch).isEqualTo("branch")
            assertThat(committer).isEqualTo("Committer")
            assertThat(email).isEqualTo("CommitterEmail")
            assertThat(timestamp).isEqualTo("Timestamp")
            assertThat(passes).isEmpty()
            assertThat(fails).isEmpty()
            assertThat(disables).isEmpty()
            assertThat(aborts).isEmpty()
            assertThat(messages).isEmpty()
            assertThat(isError).isFalse
        }
    }

    @Test
    fun `fromYaml with perfect commit`() {
        val yaml = """
            ---
            branch: branch
            committer: Committer
            email: CommitterEmail
            type: commit
            timestamp: 'Timestamp'
        """.trimIndent()
        val record = builder.convert(yaml)
        with(record) {
            assertThat(type).isEqualTo(RunType.commit)
            assertThat(branch).isEqualTo("branch")
            assertThat(committer).isEqualTo("Committer")
            assertThat(email).isEqualTo("CommitterEmail")
            assertThat(timestamp).isEqualTo("Timestamp")
            assertThat(passes).isEmpty()
            assertThat(fails).isEmpty()
            assertThat(disables).isEmpty()
            assertThat(aborts).isEmpty()
            assertThat(messages).isEmpty()
            assertThat(isError).isFalse
        }
    }


    @Test
    fun `fromYaml with unknown type`() {
        val yaml = """
            ---
            branch: branch
            committer: Committer
            email: CommitterEmail
            type: UNKNOWN
            timestamp: 'Timestamp'
            passes:
            - passed
            fails:
            - failed
            disables:
            - disabled
            aborts:
            - aborted
        """.trimIndent()
        val record = builder.convert(yaml)
        with(record) {
            assertThat(type).isEqualTo(RunType.unknown)
            assertThat(branch).isEqualTo("branch")
            assertThat(committer).isEqualTo("Committer")
            assertThat(email).isEqualTo("CommitterEmail")
            assertThat(timestamp).isEqualTo("Timestamp")
            assertThat(passes).containsExactly("passed")
            assertThat(fails).containsExactly("failed")
            assertThat(disables).containsExactly("disabled")
            assertThat(aborts).containsExactly("aborted")
            assertThat(messages).containsExactly(YamlConverter.UNKNOWN_TYPE)
            assertThat(isError).isTrue()
        }
    }

    @Test
    fun `fromYaml with test run missing expected fields`() {
        val yaml = """
            ---
            passes:
            - passed
            fails:
            - failed
            disables:
            - disabled
            aborts:
            - aborted
        """.trimIndent()
        val record = builder.convert(yaml)
        with(record) {
            assertThat(type).isEqualTo(RunType.unknown)
            assertThat(branch).isEqualTo(YamlConverter.UNKNOWN)
            assertThat(committer).isEqualTo(YamlConverter.UNKNOWN)
            assertThat(email).isEqualTo(YamlConverter.UNKNOWN)
            assertThat(timestamp).isEqualTo(YamlConverter.UNKNOWN)
            assertThat(passes).containsExactly("passed")
            assertThat(fails).containsExactly("failed")
            assertThat(disables).containsExactly("disabled")
            assertThat(aborts).containsExactly("aborted")
            assertThat(messages).containsExactlyInAnyOrder(
                YamlConverter.MISSING_BRANCH,
                YamlConverter.MISSING_COMMITTER,
                YamlConverter.MISSING_EMAIL,
                YamlConverter.MISSING_TIMESTAMP,
                YamlConverter.MISSING_TYPE,
                YamlConverter.UNKNOWN_TYPE,
            )
            assertThat(isError).isTrue()
        }
    }
}