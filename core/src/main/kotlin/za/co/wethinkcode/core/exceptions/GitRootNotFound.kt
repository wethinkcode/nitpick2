package za.co.wethinkcode.core.exceptions

import za.co.wethinkcode.core.Message
import za.co.wethinkcode.core.MessageType
import za.co.wethinkcode.core.Outputter
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