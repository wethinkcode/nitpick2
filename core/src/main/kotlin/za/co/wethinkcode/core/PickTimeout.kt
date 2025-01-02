package za.co.wethinkcode.core

import za.co.wethinkcode.core.exceptions.EndException

class PickTimeout(line: String, reporter: Reporter) : EndException(makeAndOutputText(line, reporter)) {
    companion object {
        private fun makeAndOutputText(line: String, reporter: Reporter): String {
            val text = "The shell command called during pick timed out [$line]."
            reporter.add(Message(MessageType.Error, text))
            return text
        }
    }
}
