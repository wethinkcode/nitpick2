package za.co.wethinkcode.core

class BashInterpreter(private val reporter: Reporter) {
    fun interpret(result: BashResult) {
        when (result.code) {
            127 -> outputMissingShell(result)
            0 -> outputCompletedShell(result)
            else -> outputFailedShell(result)
        }
    }

    fun outputCompletedShell(result: BashResult) {
        processLines(result.stdout, MessageType.StdOut)
        processLines(result.stderr, MessageType.StdErr)
    }

    private fun processLines(lines: LinesIterator, defaultType: MessageType) {
        while (lines.hasNext()) processNextBlock(defaultType, lines)
    }

    private fun processNextBlock(defaultType: MessageType, source: LinesIterator) {
        val block = ArrayList<String>()
        var type = processMessageType(source.next(), block)
        if (type == MessageType.NoType) type = defaultType
        accumulateBlock(source, block)
        outputBlock(type, block)
    }

    private fun processMessageType(line: String, block: ArrayList<String>): MessageType {
        val result = determineType(line)
        when (result) {
            MessageType.NoType -> block.add(line)
            MessageType.Unknown -> block.add("(Original type was: [$line])")
            else -> {}
        }
        return result
    }

    private fun determineType(line: String): MessageType {
        if (!line.startsWith(START_DELIMITER)) return MessageType.NoType
        for (candidate in MessageType.entries) {
            val match = START_DELIMITER + candidate.toString()
            if (line.equals(match, ignoreCase = true)) return candidate
        }
        return MessageType.Unknown
    }

    private fun outputBlock(messageType: MessageType, block: List<String>) {
        reporter.add(
            Message(messageType, java.lang.String.join("\n", block))
        )
    }

    private fun outputFailedShell(result: BashResult) {
        reporter.add(
            Message(
                MessageType.Exception, """
     The external bash operation returned a non-zero error code.
     This does not mean that the submission failed, only that the
     nitpick itself has failed.
     [${result.command}]
     The output up to the failure is captured below.
     Please contact the nitpick support team with this output.
     
     """.trimIndent()
            )
        )
    }

    private fun outputMissingShell(result: BashResult) {
        reporter.add(
            Message(
                MessageType.Exception, """
     The external bash file could not be found.
     [${result.command}]
     This does not mean that the submission failed, only that the
     nitpick itself has failed.
     Please contact the nitpick support team with this output.
     
     """.trimIndent()
            )
        )
    }

    companion object {
        const val START_DELIMITER: String = ">>>"
        const val END_DELIMITER: String = "<<<"
        private fun accumulateBlock(source: LinesIterator, block: ArrayList<String>) {
            while (source.hasNext()) {
                val candidate = source.next()
                if (candidate.startsWith(START_DELIMITER)) {
                    block.add("(No closing delimiter.)")
                    source.backwards()
                    return
                }
                if (candidate.startsWith(END_DELIMITER)) {
                    return
                }
                block.add(candidate)
            }
        }
    }
}
