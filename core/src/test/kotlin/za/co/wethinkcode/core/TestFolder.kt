package za.co.wethinkcode.core

import org.eclipse.jgit.api.Git
import org.eclipse.jgit.api.Status
import org.eclipse.jgit.api.errors.GitAPIException
import za.co.wethinkcode.core.FileUtility.Companion.delete
import za.co.wethinkcode.core.FileUtility.Companion.requireGitRoot
import za.co.wethinkcode.core.exceptions.EndException
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path

class TestFolder(val root: Path, val reporter: Reporter) {
    constructor(reporter: Reporter) : this(gitRootTestingTemp(reporter), reporter)
    constructor() : this(NullReporter())

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

    fun initGitRepo() {
        try {
            val git = Git.init().setDirectory(root.toFile()).call()
            git.close()
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    fun gitStatus(): Status {
        try {
            val git = Git.open(root.toFile())
            val status = git.status().call()
            git.close()
            return status
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    @Throws(IOException::class, GitAPIException::class)
    fun initDetachedHead() {
        initGitRepo()
        val git = Git.open(root.toFile())
        println(git.repository.workTree.name)
        Files.writeString(root.resolve("file.txt"), "Some string.")
        git.add().addFilepattern(root.toString()).call()
        val commit = git.commit().setMessage("First").call()
        val hash = commit.name
        git.checkout().setName(hash).call()
        git.close()
    }
}
