package za.co.wethinkcode.vnitpick.projects

import java.nio.charset.Charset
import java.nio.file.Path
import java.nio.file.StandardOpenOption
import kotlin.io.path.*

class Mru(val from: Path = homePath(), val keep: Int = 10) {

    private val paths = mutableListOf<Path>()

    val items get() = if (paths.isEmpty()) listOf(from) else paths.reversed()

    init {
        load()
    }

    fun add(path: Path) {
        if (paths.contains(path)) {
            paths.remove(path)
        } else {
            if (paths.size >= keep) {
                paths.remove(paths.first())
            }
        }
        paths.add(path)
        save()
    }

    private fun save() {
        from.createDirectories()
        val asStrings = paths.map { it.toAbsolutePath().toString() }
        from.resolve(MRU_FILE).writeLines(
            asStrings,
            Charset.defaultCharset(),
            StandardOpenOption.WRITE,
            StandardOpenOption.CREATE,
            StandardOpenOption.TRUNCATE_EXISTING
        )
    }

    fun load() {
        from.createDirectories()
        val path = from.resolve(MRU_FILE)
        if (path.exists()) {
            val lines = path.readLines()
            lines.forEach { paths.add(Path.of(it)) }
        }
    }

    fun strings(): List<String> {
        return items.map { it.toString() }
    }

    companion object {
        const val MRU_FILE = "mru.txt"

        fun homePath(): Path {
            return Path.of(System.getProperty("user.home"))
        }
    }

}