package za.co.wethinkcode.core.exceptions

import za.co.wethinkcode.core.Message
import za.co.wethinkcode.core.MessageType
import za.co.wethinkcode.core.Reporter
import java.nio.file.Path

class ExerciseNotFound(exercise: Path, reporter: Reporter) : EndException(makeAndOutputText(exercise, reporter)) {
    companion object {
        private fun makeAndOutputText(exercise: Path, reporter: Reporter): String {
            val text = "The exercise folder at [$exercise] does not exist."
            reporter.add(Message(MessageType.Error, text))
            return text
        }
    }
}
