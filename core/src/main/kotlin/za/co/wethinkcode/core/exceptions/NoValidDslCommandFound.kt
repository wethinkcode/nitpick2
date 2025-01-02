package za.co.wethinkcode.core.exceptions

import za.co.wethinkcode.core.Message
import za.co.wethinkcode.core.MessageType
import za.co.wethinkcode.core.Reporter

class NoValidDslCommandFound(reporter: Reporter) : EndException(makeAndOutputText(reporter)) {
    companion object {
        private fun makeAndOutputText(reporter: Reporter): String {
            val text = "No valid DSL commands in pick.dsl."
            reporter.add(Message(MessageType.Error, text))
            return text
        }
    }
}
