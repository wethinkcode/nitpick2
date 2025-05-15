package za.co.wethinkcode.vnitpick.projects

import java.nio.file.Path

class Mru(val from: Path = Path.of("~/.lms")) {

    private val paths = mutableSetOf<Path>()

    val items get() = paths.toList()

    init {

    }

    fun add(path: Path) {

    }

}