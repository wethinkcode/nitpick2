package za.co.wethinkcode.core.exceptions

import za.co.wethinkcode.core.Message
import za.co.wethinkcode.core.MessageType
import za.co.wethinkcode.core.Reporter

class DslUnknownCommand(line: String, reporter: Reporter) : EndException(makeAndOutputText(line, reporter)) {
    companion object {
        private fun makeAndOutputText(line: String, reporter: Reporter): String {
            val text = "Unknown DSL command [$line]."
            reporter.add(Message(MessageType.Error, text))
            return text
        }
    }
}
