package za.co.wethinkcode.core.exceptions

import za.co.wethinkcode.core.Message
import za.co.wethinkcode.core.MessageType
import za.co.wethinkcode.core.Reporter
import java.nio.file.Path

class PickDslNotRead(path: Path, reporter: Reporter) : EndException(makeAndOutputText(path, reporter)) {
    companion object {
        private fun makeAndOutputText(path: Path, reporter: Reporter): String {
            val text = """
                The pick.dsl file could not be read.
                Absolute: [$path]
                
                """.trimIndent()
            reporter.add(Message(MessageType.Error, text))
            return text
        }
    }
}
