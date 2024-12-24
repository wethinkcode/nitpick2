package za.co.wethinkcode.nitpick

import za.co.wethinkcode.core.CollectingOutputter
import za.co.wethinkcode.core.EndException


object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        val outputter = CollectingOutputter()
        EndException.runCommandSafely(outputter) {
            val parser = OptionParser(outputter)
            val command = parser.parse(*args)
            command.run()
        }
    }
}
