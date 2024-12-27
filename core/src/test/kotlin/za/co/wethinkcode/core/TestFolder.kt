package za.co.wethinkcode.core

import za.co.wethinkcode.core.FileUtility.Companion.requireGitRoot
import za.co.wethinkcode.core.FileUtility.Companion.delete
import za.co.wethinkcode.core.exceptions.EndException
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path

class TestFolder(val root: Path, val outputter: Outputter) {
    constructor(outputter: Outputter) : this(gitRootTestingTemp(outputter), outputter)

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
        private fun gitRootTestingTemp(outputter: Outputter): Path {
            try {
                val gitRoot = requireGitRoot(Path.of("."), outputter)
                val testing = gitRoot.resolve("testing")
                Files.createDirectories(testing)
                val result = Files.createTempDirectory(testing, "test")
                return result
            } catch (wrapped: IOException) {
                outputter.add(Message(MessageType.Error, "Could not create testing subfolder."))
                throw EndException()
            }
        }
    }
}
