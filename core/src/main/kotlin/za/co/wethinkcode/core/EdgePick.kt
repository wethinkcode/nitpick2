package za.co.wethinkcode.core

import java.nio.file.Path

class EdgePick(
    submissionString: String,
    private val overwrite: Boolean,
    private val outputter: Outputter
) : Runnable {
    val submission: Exercise

    init {
        val submissionPath = Path.of(submissionString).toAbsolutePath().normalize()
        submission = Exercise(submissionPath, outputter)
    }

    override fun run() {
        EndException.runCommandSafely(outputter, { unsafeRun() }, submission.root)
    }

    fun unsafeRun() {
        println("Picking [" + submission.root.toString() + "]")
        submission.validate()
        processAlteredFiles()
        submission.pickUsing(submission.lms)
    }

    fun processAlteredFiles() {
        if (!FileUtility.folderExists(submission.vault)) return
        val filesToCheck: List<Subpath> = SubpathLoader.fetch(submission.vault, { subpath -> true }, outputter)
        val alteredFiles = submission.alteredFiles(submission.vault, filesToCheck)
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
            outputter.add(
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
