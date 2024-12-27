package za.co.wethinkcode.core.exceptions

import za.co.wethinkcode.core.Message
import za.co.wethinkcode.core.MessageType
import za.co.wethinkcode.core.Outputter

class DslUnknownCommand(line: String, outputter: Outputter) : EndException(makeAndOutputText(line, outputter)) {
    companion object {
        private fun makeAndOutputText(line: String, outputter: Outputter): String {
            val text = "Unknown DSL command [$line]."
            outputter.add(Message(MessageType.Error, text))
            return text
        }
    }
}
