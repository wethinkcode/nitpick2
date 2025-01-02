package za.co.wethinkcode.core

import za.co.wethinkcode.core.FileUtility.Companion.delete
import za.co.wethinkcode.core.FileUtility.Companion.requireGitRoot
import za.co.wethinkcode.core.exceptions.EndException
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path

class TestFolder(val root: Path, val reporter: Reporter) {
    constructor(reporter: Reporter) : this(gitRootTestingTemp(reporter), reporter)

    @Throws(IOException::class)
    fun addGitFolder() {
        val gitPath = root.resolve(".git")
        Files.createDirectory(gitPath)
        val gitFile = gitPath.resolve("git.txt")
    }

    @Throws(IOException::class)
    fun delete() {
        delete(root)
    }

    companion object {
        private fun gitRootTestingTemp(reporter: Reporter): Path {
            try {
                val gitRoot = requireGitRoot(Path.of("."), reporter)
                val testing = gitRoot.resolve("testing")
                Files.createDirectories(testing)
                val result = Files.createTempDirectory(testing, "test")
                return result
            } catch (wrapped: IOException) {
                reporter.add(Message(MessageType.Error, "Could not create testing subfolder."))
                throw EndException()
            }
        }
    }
}
