/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package za.co.wethinkcode.nitpick

import org.assertj.core.api.AssertionsForClassTypes
import org.junit.jupiter.api.Test
import za.co.wethinkcode.core.CollectingOutputter
import za.co.wethinkcode.core.EdgePick
import za.co.wethinkcode.core.Generate
import za.co.wethinkcode.core.LmsPick
import java.nio.file.Path

class OptionParserTest {
    var parser = OptionParser(CollectingOutputter())
    @Test
    fun noCommandGiven() {
        AssertionsForClassTypes.assertThat(parser.parse()).isInstanceOf(Help::class.java)
    }

    @Test
    fun invalidCommandGiven() {
        AssertionsForClassTypes.assertThat(parser.parse()).isInstanceOf(Help::class.java)
    }

    @Test
    fun multipleCommandsGiven() {
        AssertionsForClassTypes.assertThat(parser.parse("multiple", "commands")).isInstanceOf(Help::class.java)
    }

    @Test
    fun pickWithNoSubmissionIsCwd() {
        val result = parser.parse("pick")
        AssertionsForClassTypes.assertThat(result).isInstanceOf(EdgePick::class.java)
        val asPick = result as EdgePick
        AssertionsForClassTypes.assertThat(asPick.submission.root).isEqualTo(Path.of(".").toAbsolutePath().normalize())
    }

    @Test
    fun lmsPickWithNoSubmissionIsCwd() {
        val result = parser.parse(
            "pick",
            "-l",
            "../core/testData/authorSimple/exercise"
        )
        AssertionsForClassTypes.assertThat(result).isInstanceOf(LmsPick::class.java)
        val asPick = result as LmsPick
        AssertionsForClassTypes.assertThat(asPick.submission.root).isEqualTo(Path.of(".").toAbsolutePath().normalize())
    }

    @Test
    fun generateProducesGenerator() {
        AssertionsForClassTypes.assertThat(
            parser.parse(
                "generate",
                "-l",
                "../core/testData/authorSimple/exercise"
            )
        ).isInstanceOf(Generate::class.java)
    }

    @Test
    fun generateWithNoLmsAssumesCwd() {
        val result = parser.parse(
            "generate"
        )
        AssertionsForClassTypes.assertThat(result).isInstanceOf(Generate::class.java)
        val asGenerate = result as Generate
        AssertionsForClassTypes.assertThat(asGenerate.lms.root).isEqualTo(Path.of(".").toAbsolutePath().normalize())
    }

    @Test
    fun failsWithDSwtichAndNoArgument() {
        val result = parser.parse(
            "generate",
            "-d"
        )
        AssertionsForClassTypes.assertThat(result).isInstanceOf(Help::class.java)
    }

    @Test
    fun edgePick() {
        AssertionsForClassTypes.assertThat(
            parser.parse(
                "pick",
                "-s",
                "../core/testData/exercisePassing"
            )
        ).isInstanceOf(EdgePick::class.java)
    }

    @Test
    fun lmsPick() {
        AssertionsForClassTypes.assertThat(
            parser.parse(
                "pick",
                "-l",
                "../core/testData/authorSimple/exercise",
                "-s",
                "../core/testData/exercisePassing"
            )
        ).isInstanceOf(LmsPick::class.java)
    }
}