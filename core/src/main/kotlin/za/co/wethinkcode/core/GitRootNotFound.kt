package za.co.wethinkcode.core

import java.nio.file.Path

class GitRootNotFound(from: Path, outputter: Outputter) : EndException(makeAndOutputText(from, outputter)) {
    companion object {
        private fun makeAndOutputText(from: Path, outputter: Outputter): String {
            val text = "Could not find .git folder as siblinig or uncle of:" +
                    "[" + from + "]."
            outputter.add(Message(MessageType.Error, text))
            return text
        }
    }
}