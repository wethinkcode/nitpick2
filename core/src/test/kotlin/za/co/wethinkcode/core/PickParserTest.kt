package za.co.wethinkcode.core

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import za.co.wethinkcode.core.exceptions.DslUnknownCommand
import za.co.wethinkcode.core.exceptions.NoValidDslCommandFound
import za.co.wethinkcode.core.exceptions.PickDslNotRead
import java.nio.file.Path

internal class PickParserTest {
    var reporter: Reporter = CollectingReporter()

    @Test
    fun throwsOnEmptyLines() {
        val parser = PickParser(IRRELEVANT_PATH, IRRELEVANT_PATH, reporter)
        Assertions.assertThrows(
            NoValidDslCommandFound::class.java
        ) { parser.parse(emptyList()) }
    }

    @Test
    fun throwsOnGarbageLine() {
        val parser = PickParser(IRRELEVANT_PATH, IRRELEVANT_PATH, reporter)
        Assertions.assertThrows(
            DslUnknownCommand::class.java
        ) { parser.parse(listOf("garbage")) }
    }

    @Test
    fun throwsOnBadPickPath() {
        val parser = PickParser(IRRELEVANT_PATH, IRRELEVANT_PATH, reporter)
        Assertions.assertThrows(
            PickDslNotRead::class.java
        ) { parser.parse() }
    }

    @Test
    fun returnsBash() {
        val parser = PickParser(IRRELEVANT_PATH, IRRELEVANT_PATH, reporter)
        val actual = parser.parse(listOf("bash something.sh"))
        org.assertj.core.api.Assertions.assertThat(actual).isInstanceOf(BashRunner::class.java)
        val bash = actual as BashRunner
        org.assertj.core.api.Assertions.assertThat(bash.args).containsExactly(PickParser.bashPath(), "something.sh")
    }

    @Test
    fun ignoresBlanksAndHashtags() {
        val contents = ArrayList<String>()
        contents.add("#")
        contents.add("")
        contents.add("bash something.sh")
        val parser = PickParser(IRRELEVANT_PATH, IRRELEVANT_PATH, reporter)
        val actual = parser.parse(contents)
        org.assertj.core.api.Assertions.assertThat(actual).isInstanceOf(BashRunner::class.java)
        val bash = actual as BashRunner
        org.assertj.core.api.Assertions.assertThat(bash.args).containsExactly(PickParser.bashPath(), "something.sh")
    }

    companion object {
        private val IRRELEVANT_PATH: Path = Path.of("DoesNotMatter")
    }
}