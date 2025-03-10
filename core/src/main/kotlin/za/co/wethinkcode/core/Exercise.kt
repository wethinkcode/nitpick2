package za.co.wethinkcode.core

import za.co.wethinkcode.core.exceptions.ExerciseNoLms
import za.co.wethinkcode.core.exceptions.ExerciseNotFound
import za.co.wethinkcode.core.exceptions.NoDslFileFound
import za.co.wethinkcode.core.exceptions.WipeSecurityException
import java.io.IOException
import java.io.PrintWriter
import java.nio.file.Files
import java.nio.file.Path
import java.util.*

class Exercise(val root: Path, private val reporter: Reporter) {
    val lms: Path = root.resolve(LMS_FOLDER)
    val vault: Path = root.resolve(VAULT_FOLDER)

    val dsl: Path = lms.resolve(DSL_LEAF)
    val protection: Path = lms.resolve(PROTECTED_TXT_LEAF)

    val meta: Path = lms.resolve(META_TXT_LEAF)

    fun pickUsing(pickFolder: Path) {
        val parser = PickParser(pickFolder, root, reporter)
        val picker = parser.parse(pickFolder.resolve(DSL_LEAF))
        picker.pick(root)
    }

    fun makeFromLms(source: Exercise) {
        makeOrWipe()
        copyFrom(source.root)
        copyProtectedFilesToVault(source.protectedFilesFromLms())
        addRepoDetails(source)
    }

    private fun addRepoDetails(source: Exercise) {
        val repo: LmsRepo = FileUtility.getRepoInformation(source.root, reporter)
        val repoDetails: PrintWriter = FileUtility.openForWriting(meta, "Cannot open repo details for writing")
        repoDetails.println("lms=" + repo.remote)
        repoDetails.println("exercise=" + repo.exercise)
        repoDetails.println("commit=" + repo.Sha)
        repoDetails.println("version=" + Version.version)
        repoDetails.flush()
        repoDetails.close()
    }

    fun makeOrWipe() {
        try {
            Files.createDirectories(root)
            val subpaths: List<Subpath> = SubpathLoader.fetch(root, MatchAllButGit(), reporter)
            Collections.reverse(subpaths)
            println("Wiping.")
            for (path in subpaths) {
                FileUtility.delete(root.resolve(path.path))
            }
        } catch (wrapped: SecurityException) {
            throw WipeSecurityException(reporter, root, wrapped)
        } catch (wrapped: IOException) {
            throw WipeSecurityException(reporter, root, wrapped)
        }
    }

    fun copyFrom(source: Path) {
        try {
            unsafeCopyFrom(source)
        } catch (wrapped: IOException) {
            throw RuntimeException(wrapped)
        }
    }

    @Throws(IOException::class)
    private fun unsafeCopyFrom(source: Path) {
        val subpaths: List<Subpath> = SubpathLoader.fetch(source, { subpath -> true }, reporter)
        for ((path, isFolder) in subpaths) {
            val sourceSubpath = source.resolve(path)
            val destinationPath = root.resolve(path)
            if (isFolder) {
                Files.createDirectory(destinationPath)
            } else {
                Files.copy(sourceSubpath, destinationPath)
            }
        }
    }

    fun copyProtectedFilesToVault(toProtects: List<Subpath>) {
        if (toProtects.isEmpty()) return
        FileUtility.createDirectories(vault)
        for ((path, isFolder) in toProtects) {
            val source = root.resolve(path)
            val target = vault.resolve(path)
            if (isFolder) {
                FileUtility.createDirectories(target)
            } else {
                println("Protecting: [$source]")
                FileUtility.copy(source, target)
                FileUtility.makeReadOnly(target)
                FileUtility.makeReadOnly(source)
            }
        }
    }

    fun protectedFilesFromLms(): List<Subpath> {
        val protectedGlobs = readProtectionTextLines()
        val candidates: List<Subpath> = SubpathLoader.fetch(root, MatchByGlob(protectedGlobs), reporter)
        return candidates.stream().filter { subpath -> !subpath.path.startsWith(LMS_FOLDER) }.toList()
    }

    private fun readProtectionTextLines(): List<String> {
        if (!FileUtility.fileExists(protection)) return emptyList()
        try {
            return Files.readAllLines(protection)
        } catch (wrapped: IOException) {
            throw RuntimeException(wrapped)
        }
    }

    fun alteredFiles(originals: Path, filesToCheck: List<Subpath>): ArrayList<Subpath> {
        val result = ArrayList<Subpath>()
        for (subpath in filesToCheck) {
            if (!subpath.isFolder) {
                val original = originals.resolve(subpath.path)
                val toCheck = root.resolve(subpath.path)
                if (!FileUtility.isIdentical(original, toCheck)) result.add(subpath)
            }
        }
        return result
    }

    fun validate() {
        if (!FileUtility.folderExists(root)) throw ExerciseNotFound(root, reporter)
        if (!FileUtility.folderExists(lms)) throw ExerciseNoLms(lms, reporter)
        if (!FileUtility.fileExists(dsl)) throw NoDslFileFound(dsl, reporter)
    }
}
