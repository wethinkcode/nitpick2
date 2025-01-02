package za.co.wethinkcode.core

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import java.util.*

class BashInterpreterTest {
    var outputter: CollectingReporter = CollectingReporter()
    var interpreter: BashInterpreter = BashInterpreter(outputter)

    var EMPTY_LINES: List<String> = emptyList()
    var UNSTRUCTURED_LINES: List<String> = makeList("This is output.", "With two lines.")

    var RESULTPASS_LINES: List<String> = makeList(">>>ResultPass", "Whoa, you passed!", "<<<")
    var NO_CONTENT_LINED: List<String> = makeList(">>>ResultPass", "<<<")

    var UNKNOWN_TYPE_LINES: List<String> = makeList(">>>What", "Who knows?", "<<<")

    var MIXED_CONTENT_LINES: List<String> =
        makeList("Some stdout.", ">>>Comment", "<<<", "More stdout.", ">>>Comment", "<<<")

    @Test
    fun handlesBadReturnCode() {
        val result = BashResult("command", 1, EMPTY_LINES, EMPTY_LINES)
        interpreter.interpret(result)
        Assertions.assertThat(outputter.messages[0]!!.type).isEqualTo(MessageType.Exception)
    }

    @Test
    fun handlesMissingShell() {
        val result = BashResult("command", 127, EMPTY_LINES, EMPTY_LINES)
        interpreter.interpret(result)
        Assertions.assertThat(outputter.messages[0]!!.type).isEqualTo(MessageType.Exception)
    }

    @Test
    fun wrapsUnmarkedStdOut() {
        val result = BashResult("command", 0, UNSTRUCTURED_LINES, EMPTY_LINES)
        interpreter.interpret(result)
        Assertions.assertThat(outputter.messages.size).isEqualTo(1)
        Assertions.assertThat(outputter.messages[0]!!.type).isEqualTo(MessageType.StdOut)
        Assertions.assertThat(outputter.messages[0]!!.content).isEqualTo("This is output.\nWith two lines.")
    }

    @Test
    fun handlesKnownType() {
        val result = BashResult("command", 0, RESULTPASS_LINES, EMPTY_LINES)
        interpreter.interpret(result)
        Assertions.assertThat(outputter.messages.size).isEqualTo(1)
        Assertions.assertThat(outputter.messages[0]!!.type).isEqualTo(MessageType.ResultPass)
        Assertions.assertThat(outputter.messages[0]!!.content).isEqualTo("Whoa, you passed!")
    }

    @Test
    fun handlesUnknownType() {
        val result = BashResult("command", 0, UNKNOWN_TYPE_LINES, EMPTY_LINES)
        interpreter.interpret(result)
        Assertions.assertThat(outputter.messages.size).isEqualTo(1)
        Assertions.assertThat(outputter.messages[0]!!.type).isEqualTo(MessageType.Unknown)
        Assertions.assertThat(outputter.messages[0]!!.content).isEqualTo("(Original type was: [>>>What])\nWho knows?")
    }

    @Test
    fun handlesNoText() {
        val result = BashResult("command", 0, NO_CONTENT_LINED, EMPTY_LINES)
        interpreter.interpret(result)
        Assertions.assertThat(outputter.messages.size).isEqualTo(1)
        Assertions.assertThat(outputter.messages[0]!!.type).isEqualTo(MessageType.ResultPass)
        Assertions.assertThat(outputter.messages[0]!!.content).isEqualTo("")
    }

    @Test
    fun handlesMixedBlocks() {
        val result = BashResult("command", 0, MIXED_CONTENT_LINES, EMPTY_LINES)
        interpreter.interpret(result)
        Assertions.assertThat(outputter.messages.size).isEqualTo(4)
    }

    fun makeList(vararg strings: String): List<String> {
        return Arrays.stream(strings).toList()
    }
}
