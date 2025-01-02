package za.co.wethinkcode.core

import za.co.wethinkcode.core.exceptions.EndException
import java.nio.file.Path

class LmsPick(
    authorString: String?,
    submissionString: String?,
    private val overwrite: Boolean,
    private val reporter: Reporter
) : Runnable {
    val submission: Exercise

    private val author: Exercise

    init {
        val submissionPath = Path.of(submissionString).toAbsolutePath().normalize()
        submission = Exercise(submissionPath, reporter)
        val authorPath = Path.of(authorString).toAbsolutePath().normalize()
        author = Exercise(authorPath, reporter)
    }

    override fun run() {
        EndException.runCommandSafely(reporter, { unsafeRun() }, submission.root)
    }

    fun unsafeRun() {
        author.validate()
        processAlteredFiles()
        submission.pickUsing(author.lms)
        reporter.saveGrade(submission.root)
    }

    fun processAlteredFiles() {
        val filesToCheck = author.protectedFilesFromLms()
        val alteredFiles = submission.alteredFiles(author.root, filesToCheck)
        val filenames = processAlteredFiles(alteredFiles)
        outputAlteredFiles(filenames)
    }

    private fun outputAlteredFiles(filenames: List<String>) {
        if (!filenames.isEmpty()) {
            val type = MessageType.Altered
            var start = """
                The following files have been altered from the starting code.
                If the exercise is submitted, they will not pass the nit-pick.
                Run nitpick with the -o option to overwrite them with the original text.
                
                """.trimIndent()
            if (overwrite) start = "The following files have been overwritten with the starting code.\n"
            reporter.add(
                Message(
                    type,
                    start + java.lang.String.join("\n", filenames)
                )
            )
        }
    }

    private fun processAlteredFiles(alteredFiles: ArrayList<Subpath>): List<String> {
        val filenames: MutableList<String> = ArrayList()
        for ((path) in alteredFiles) {
            val original = submission.vault.resolve(path)
            val altered = submission.root.resolve(path)
            filenames.add("\t" + altered)
            if (overwrite) {
                FileUtility.copy(original, altered)
            }
        }
        return filenames
    }
}
