package za.co.wethinkcode.core

import org.eclipse.jgit.lib.*
import java.io.IOException
import java.io.PrintWriter
import java.math.BigInteger
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import java.nio.file.StandardOpenOption
import java.nio.file.attribute.PosixFilePermission
import java.security.MessageDigest

object FileUtility {
    const val NITPICK_PREFIX: String = "nitpick"

    fun requireGitRoot(from: Path, outputter: Outputter?): Path {
        var candidate = from.toAbsolutePath()
        while (candidate.nameCount > 1) {
            val candidateGit = candidate.resolve(".git")
            if (candidateGit.toFile().exists() && candidateGit.toFile().isDirectory) return candidate
            candidate = candidate.resolve("..").toAbsolutePath().normalize()
        }
        throw GitRootNotFound(from, outputter!!)
    }

    fun delete(root: Path) {
        val deleter = DeletingFileVisitor(root)
        try {
            Files.walkFileTree(root, deleter)
            root.toFile().delete()
        } catch (wrapped: IOException) {
            throw RuntimeException("Deleting File Visitor failed for [$root].", wrapped)
        }
    }

    fun wipe(root: Path, outputter: Outputter) {
        val deleter = DeletingFileVisitor(root)
        try {
            Files.walkFileTree(root, deleter)
        } catch (wrapped: IOException) {
            outputter.add(
                Message(
                    MessageType.Error,
                    "Wiping File Visitor failed for [$root]."
                )
            )
            throw RuntimeException(wrapped)
        }
    }

    fun folderExists(path: Path): Boolean {
        val file = path.toFile()
        return file.exists() && file.isDirectory
    }

    fun fileExists(path: Path): Boolean {
        val file = path.toFile()
        return file.exists() && file.isFile
    }

    fun isIdentical(first: Path, second: Path): Boolean {
        return fileExists(first) && fileExists(second) && getMd5(first) == getMd5(
            second
        )
    }

    fun getMd5(resolve: Path?): String {
        try {
            val data = Files.readAllBytes(resolve)
            val hash = MessageDigest.getInstance("MD5").digest(data)
            return BigInteger(1, hash).toString(16)
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    fun makeReadOnly(path: Path?) {
        if (!isPosixSystem) return
        try {
            val permissions = Files.getPosixFilePermissions(path)
            permissions.remove(PosixFilePermission.GROUP_WRITE)
            permissions.remove(PosixFilePermission.OTHERS_WRITE)
            permissions.remove(PosixFilePermission.OWNER_WRITE)
            Files.setPosixFilePermissions(path, permissions)
        } catch (wrapped: IOException) {
            throw RuntimeException(wrapped)
        }
    }


    fun makeWritable(path: Path?) {
        if (!isPosixSystem) return
        try {
            val permissions = Files.getPosixFilePermissions(path)
            permissions.add(PosixFilePermission.GROUP_WRITE)
            permissions.add(PosixFilePermission.OTHERS_WRITE)
            permissions.add(PosixFilePermission.OWNER_WRITE)
            Files.setPosixFilePermissions(path, permissions)
        } catch (wrapped: IOException) {
            throw RuntimeException(wrapped)
        }
    }

    val isPosixSystem: Boolean
        get() {
            val views = FileSystems.getDefault().supportedFileAttributeViews()
            return views.contains("posix")
        }

    fun createDirectories(path: Path) {
        try {
            Files.createDirectories(path)
        } catch (wrapped: IOException) {
            throw RuntimeException("Creating directories failed for [$path].", wrapped)
        }
    }

    fun copy(source: Path, target: Path) {
        try {
            Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING)
        } catch (wrapped: IOException) {
            throw RuntimeException("Copying from [$source] to [$target].")
        }
    }

    fun openForWriting(path: Path, error: String): PrintWriter {
        try {
            val stream = Files.newOutputStream(
                path,
                StandardOpenOption.TRUNCATE_EXISTING,
                StandardOpenOption.CREATE
            )
            return PrintWriter(stream)
        } catch (e: IOException) {
            throw RuntimeException("$error\n[$path]")
        }
    }

    fun getRepoInformation(lmsRoot: Path, outputter: Outputter?): LmsRepo {
        try {
            RepositoryBuilder()
                .findGitDir(lmsRoot.toFile()).build().use { localRepo ->
                    val configuration: Config = localRepo.getConfig()
                    val remote: String = configuration.getString("remote", "origin", "url")
                    val exercise: Path = localRepo.getWorkTree().toPath()
                    val relative = exercise.relativize(lmsRoot.root)
                    val head: Ref = localRepo.getRefDatabase().findRef("HEAD")
                    val hash: String = head.getObjectId().getName()
                    return LmsRepo(remote, relative.toString(), hash)
                }
        } catch (cause: Exception) {
            throw GitRootNotFound(lmsRoot, outputter!!)
        }
    }
}

