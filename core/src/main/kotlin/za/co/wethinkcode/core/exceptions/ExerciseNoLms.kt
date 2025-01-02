package za.co.wethinkcode.core.exceptions

import za.co.wethinkcode.core.Message
import za.co.wethinkcode.core.MessageType
import za.co.wethinkcode.core.Reporter
import java.nio.file.Path

class ExerciseNoLms(exercise: Path, reporter: Reporter) : EndException(makeAndOutputText(exercise, reporter)) {
    companion object {
        private fun makeAndOutputText(exercise: Path, reporter: Reporter): String {
            val text = "The folder at [" + exercise.parent.toString() + "] has no lms subfolder."
            reporter.add(Message(MessageType.Error, text))
            return text
        }
    }
}
