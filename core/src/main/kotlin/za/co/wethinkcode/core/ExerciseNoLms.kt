package za.co.wethinkcode.core

import java.nio.file.Path

class ExerciseNoLms(exercise: Path, outputter: Outputter) : EndException(makeAndOutputText(exercise, outputter)) {
    companion object {
        private fun makeAndOutputText(exercise: Path, outputter: Outputter): String {
            val text = "The folder at [" + exercise.parent.toString() + "] has no lms subfolder."
            outputter.add(Message(MessageType.Error, text))
            return text
        }
    }
}
