package za.co.wethinkcode.core

class DslUnknownCommand(line: String, outputter: Outputter) : EndException(makeAndOutputText(line, outputter)) {
    companion object {
        private fun makeAndOutputText(line: String, outputter: Outputter): String {
            val text = "Unknown DSL command [$line]."
            outputter.add(Message(MessageType.Error, text))
            return text
        }
    }
}
