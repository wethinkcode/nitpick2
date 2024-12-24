package za.co.wethinkcode.nitpick

import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import za.co.wethinkcode.core.CollectingOutputter

class EndToEndTest {
    var outputter = CollectingOutputter()
    var optionParser = OptionParser(outputter)
    @Disabled("WIP")
    @Test
    fun perfectRun() {
        val command = optionParser.parse(
            "pick",
            "--submission",
            "../testData/exercisePassing"
        )
        command.run()
    }

    @Test
    fun generateAuthorSimple() {
        val command = optionParser.parse(
            "generate",
            "-l",
            "../testData/authorSimple/exercise",
        )
        command.run()
    }
}
