package za.co.wethinkcode.core

import java.nio.file.Path

class NullReporter : Reporter {
    override fun add(message: Message) {
    }

    override fun saveResults(results: Path) {
    }

    override fun saveGrade(results: Path) {
    }
}