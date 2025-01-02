package za.co.wethinkcode.core

import java.nio.file.Path

interface Reporter {
    fun add(message: Message)

    fun saveResults(results: Path)

    fun saveGrade(results: Path)
}
