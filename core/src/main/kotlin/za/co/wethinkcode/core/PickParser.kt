package za.co.wethinkcode.core

import za.co.wethinkcode.core.exceptions.DslUnknownCommand
import za.co.wethinkcode.core.exceptions.NoValidDslCommandFound
import za.co.wethinkcode.core.exceptions.PickDslNotRead
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.util.*

class PickParser(private val pickSource: Path, private val targetFolder: Path, private val reporter: Reporter) {
    fun parse(): PickRunnable {
        val pickPath = pickSource.resolve(DSL_LEAF)
        return parse(pickPath)
    }

    fun parse(path: Path?): PickRunnable {
        try {
            return parse(Files.readAllLines(path))
        } catch (wrapped: IOException) {
            throw PickDslNotRead(path!!, reporter)
        }
    }

    fun parse(lines: List<String>): PickRunnable {
        // MDH: Pathetic, but it works for the MVP.
        for (line in lines) {
            if (line.startsWith(DSL_COMMENT)) continue
            if (line.isBlank()) continue
            if (line.startsWith(DSL_BASH)) return parseBash(line)
            throw DslUnknownCommand(line, reporter)
        }
        throw NoValidDslCommandFound(reporter)
    }

    fun parseBash(line: String): PickRunnable {
        val commands = line.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val skipFirst = Arrays.stream(commands).skip(1).toList()
        val newFirst = ArrayList<String>()
        newFirst.add(bashPath())
        newFirst.addAll(skipFirst)
        return BashRunner(newFirst, pickSource, reporter)
    }

    companion object {
        const val GIT_BASH_PATH: String = "C:/Program Files/Git/bin/bash.exe"
        const val LINUX_BASH_PATH: String = "/usr/bin/bash"
        const val MACOS_BASH_PATH: String = "/bin/bash"

        fun bashPath(): String {
            val os = System.getProperty("os.name")
            if (os.startsWith("Windows")) return GIT_BASH_PATH
            if (os.startsWith("Mac")) return MACOS_BASH_PATH
            return LINUX_BASH_PATH
        }
    }
}
