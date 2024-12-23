package za.co.wethinkcode.core

import java.nio.file.Path

class PickDslNotRead(path: Path, outputter: Outputter) : EndException(makeAndOutputText(path, outputter)) {
    companion object {
        private fun makeAndOutputText(path: Path, outputter: Outputter): String {
            val text = """
                The pick.dsl file could not be read.
                Absolute: [$path]
                
                """.trimIndent()
            outputter.add(Message(MessageType.Error, text))
            return text
        }
    }
}
