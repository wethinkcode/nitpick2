package za.co.wethinkcode.core.flow

import za.co.wethinkcode.flow.FileHelpers.FLOW_LOG_SUFFIX
import za.co.wethinkcode.flow.FileHelpers.FLOW_TMP_SUFFIX
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Path
import java.util.*
import kotlin.io.path.exists
import kotlin.io.path.isDirectory

class FlowDetailLoader {
    val decoder = Base64.getDecoder()

    fun load(path: Path): Commits {
        val details = loadFlowDetails(path)
        val commits = Commits()
        val earliest = details[0].timestamp
        commits.load(path, earliest)
        commits.putDetailsInCommits(details)
        return commits
    }


    fun loadFlowDetails(path: Path): List<FlowDetail> {
        if (!path.exists()) return listOf(makeBase64Error(path, "File does not exist."))
        if (!path.isDirectory()) return listOf(makeBase64Error(path, "File is not a folder."))
        val runs = mutableListOf<FlowDetail>()
        Files.list(path).forEach { log ->
            if (isJltkLog(log)) {
                safeLoad(runs, log)
            }
        }
        return runs.sortedBy { it.timestamp }
    }

    private fun isJltkLog(log: Path): Boolean {
        val name = log.toString();
        return name.endsWith(FLOW_TMP_SUFFIX) || name.endsWith(FLOW_LOG_SUFFIX)
    }

    fun safeLoad(
        runs: MutableList<FlowDetail>,
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
        runs: MutableList<FlowDetail>
    ) {
        val lines = Files.readAllLines(log)
        for (line in lines) {
            val decoded = decoder.decode(line).toString(Charset.forName("UTF-8"))
            runs.add(YamlFlowDetail(decoded).convert())
        }
    }

    fun makeBase64Error(path: Path, error: String): FlowDetail {
        val parse = ParsedFileName(path.toString())
        return YamlFlowDetail(
            """
            ---
            type: base64
            timestamp: '${parse.timestamp}'
            committer: ${parse.email}
            email: ${parse.email}
            branch: ${parse.branch}
            file: ${path.toAbsolutePath()}
            error: $error
        """.trimIndent()
        ).convert()
    }
}