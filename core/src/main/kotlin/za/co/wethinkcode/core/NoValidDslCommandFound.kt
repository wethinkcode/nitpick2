package za.co.wethinkcode.core

class NoValidDslCommandFound(outputter: Outputter) : EndException(makeAndOutputText(outputter)) {
    companion object {
        private fun makeAndOutputText(outputter: Outputter): String {
            val text = "No valid DSL commands in pick.dsl."
            outputter.add(Message(MessageType.Error, text))
            return text
        }
    }
}
