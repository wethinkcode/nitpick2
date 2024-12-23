package za.co.wethinkcode.core

class PickTimeout(line: String, outputter: Outputter) : EndException(makeAndOutputText(line, outputter)) {
    companion object {
        private fun makeAndOutputText(line: String, outputter: Outputter): String {
            val text = "The shell command called during pick timed out [$line]."
            outputter.add(Message(MessageType.Error, text))
            return text
        }
    }
}
