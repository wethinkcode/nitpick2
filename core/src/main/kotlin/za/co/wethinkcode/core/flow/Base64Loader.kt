package za.co.wethinkcode.core.flow

import za.co.wethinkcode.flow.FileHelpers.JLTK_LOG_SUFFIX
import za.co.wethinkcode.flow.FileHelpers.JLTK_TMP_SUFFIX
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Path
import java.util.*
import kotlin.io.path.exists
import kotlin.io.path.isDirectory

class Base64Loader {
    val decoder = Base64.getDecoder()

    fun load(path: Path): List<String> {
        if (!path.exists()) return listOf(makeBase64Error(path, "File does not exist."))
        if (!path.isDirectory()) return listOf(makeBase64Error(path, "File is not a folder."))
        val runs = mutableListOf<String>()
        Files.list(path).forEach { log ->
            if (isJltkLog(log)) {
                safeLoad(runs, log)
            }
        }
        return runs
    }

    private fun isJltkLog(log: Path): Boolean {
        val name = log.toString();
        return name.endsWith(JLTK_TMP_SUFFIX) || name.endsWith(JLTK_LOG_SUFFIX)
    }

    fun safeLoad(
        runs: MutableList<String>,
        log: Path
    ) {
        try {
            unsafeLoad(log, runs)
        } catch (ignored: IllegalArgumentException) {
            runs.add(makeBase64Error(log, "Could not parse base64."))
        }
    }

    private fun unsafeLoad(
        log: Path,
        runs: MutableList<String>
    ) {
        val lines = Files.readAllLines(log)
        for (line in lines) {
            val decoded = decoder.decode(line).toString(Charset.forName("UTF-8"))
            runs.add(decoded)
        }
    }

    fun makeBase64Error(path: Path, error: String): String {
        val parse = ParsedFileName(path.toString())
        return """
            ---
            type: base64
            timestamp: '${parse.timestamp}'
            committer: ${parse.email}
            email: ${parse.email}
            branch: ${parse.branch}
            file: ${path.toAbsolutePath()}
            error: $error
        """.trimIndent()
    }
}