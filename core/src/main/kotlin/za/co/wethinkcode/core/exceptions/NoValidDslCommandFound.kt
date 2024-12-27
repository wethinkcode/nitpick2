package za.co.wethinkcode.core.exceptions

import za.co.wethinkcode.core.Message
import za.co.wethinkcode.core.MessageType
import za.co.wethinkcode.core.Outputter

class NoValidDslCommandFound(outputter: Outputter) : EndException(makeAndOutputText(outputter)) {
    companion object {
        private fun makeAndOutputText(outputter: Outputter): String {
            val text = "No valid DSL commands in pick.dsl."
            outputter.add(Message(MessageType.Error, text))
            return text
        }
    }
}
