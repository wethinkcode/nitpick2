package za.co.wethinkcode.core.exceptions

import za.co.wethinkcode.core.Message
import za.co.wethinkcode.core.MessageType
import za.co.wethinkcode.core.Reporter
import java.nio.file.Path

class GitRootNotFound(from: Path, reporter: Reporter) : EndException(makeAndOutputText(from, reporter)) {
    companion object {
        private fun makeAndOutputText(from: Path, reporter: Reporter): String {
            val text = "Could not find .git folder as siblinig or uncle of:" +
                    "[" + from + "]."
            reporter.add(Message(MessageType.Error, text))
            return text
        }
    }
}