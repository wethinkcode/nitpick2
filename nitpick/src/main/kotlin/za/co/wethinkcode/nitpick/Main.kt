package za.co.wethinkcode.nitpick

import za.co.wethinkcode.core.CollectingReporter
import za.co.wethinkcode.core.exceptions.EndException

class Main {

}

fun main(args: Array<String>) {
    val outputter = CollectingReporter()
    EndException.runCommandSafely(outputter) {
        val parser = OptionParser(outputter)
        val command = parser.parse(*args)
        command.run()
    }
}

