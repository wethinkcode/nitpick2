package za.co.wethinkcode.core

import java.nio.file.Path

class ExerciseNotFound(exercise: Path, outputter: Outputter) : EndException(makeAndOutputText(exercise, outputter)) {
    companion object {
        private fun makeAndOutputText(exercise: Path, outputter: Outputter): String {
            val text = "The exercise folder at [$exercise] does not exist."
            outputter.add(Message(MessageType.Error, text))
            return text
        }
    }
}
