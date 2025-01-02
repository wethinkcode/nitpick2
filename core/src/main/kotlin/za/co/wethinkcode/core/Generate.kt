package za.co.wethinkcode.core

import java.nio.file.Path

class Generate(
    authorString: String?,
    private val destinationString: String,
    private val reporter: Reporter
) : Runnable {
    val lms: Exercise

    init {
        val sourcePath = Path.of(authorString).toAbsolutePath().normalize()
        this.lms = Exercise(sourcePath, reporter)
    }


    override fun run() {
        lms.validate()
        val path = makeDestinationPath()
        println("Making at [$path].")
        val destination = Exercise(path, reporter)
        destination.makeFromLms(lms)
        println("Running pick...")
        val pick = EdgePick(destination.root.toString(), false, reporter)
        pick.run()
    }

    fun makeDestinationPath(): Path {
        if (destinationString == NO_DESTINATION_GIVEN) {
            val projectPath: Path = FileUtility.requireGitRoot(lms.root, reporter)
            val buildPath: Path = projectPath.resolve("target")
            val exercisesPath: Path = buildPath.resolve("exercises")
            val lastLmsName = lms.root.getName(lms.root.nameCount - 1)
            return exercisesPath.resolve(lastLmsName)
        }
        return Path.of(destinationString)
    }

    companion object {
        const val NO_DESTINATION_GIVEN: String = "NO_DESTINATION_GIVEN"
    }
}
