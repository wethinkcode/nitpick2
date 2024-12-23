package za.co.wethinkcode.core

import java.nio.file.Path

interface Outputter {
    fun add(message: Message?)

    fun saveResults(results: Path?)

    fun saveGrade(results: Path?)
}
