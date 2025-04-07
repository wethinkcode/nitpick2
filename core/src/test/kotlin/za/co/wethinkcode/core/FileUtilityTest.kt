package za.co.wethinkcode.core

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import za.co.wethinkcode.core.FileUtility.Companion.fileExists
import za.co.wethinkcode.core.FileUtility.Companion.folderExists
import za.co.wethinkcode.core.exceptions.GitRootNotFound
import java.io.IOException
import java.nio.file.Files
import java.nio.file.attribute.PosixFilePermission

class FileUtilityTest {
    var reporter: Reporter = CollectingReporter()
    var folder: TestFolder = TestFolder(reporter)

    @Test
    @Throws(IOException::class)
    fun requireGitRepoAtTop() {
        folder.initGitRepo()
        assertThat(FileUtility.requireGitRoot(folder.root, reporter)).isEqualTo(folder.root)
        folder.delete()
    }

    @Test
    @Throws(IOException::class)
    fun requireGitRepoFromInside() {
        folder.initGitRepo()
        val insidePath = folder.root.resolve("folder")
        Files.createDirectory(insidePath)
        assertThat(FileUtility.requireGitRoot(insidePath, reporter)).isEqualTo(folder.root)
        folder.delete()
    }

    @Test
    @Throws(IOException::class)
    fun requireGitRepoNonExistent() {
        // Can't use testing folder, it's *in* a repo.
        val tempPath = Files.createTempDirectory(FileUtility.NITPICK_PREFIX)
        tempPath.toFile().deleteOnExit()
        val insidePath = tempPath.resolve("folder")
        Files.createDirectory(insidePath)
        Assertions.assertThrows(
            GitRootNotFound::class.java
        ) {
            FileUtility.requireGitRoot(insidePath, reporter)
        }
        Files.deleteIfExists(insidePath)
        Files.delete(tempPath)
        // Must still delete test folder on success
        folder.delete()
    }

    @Test
    @Throws(IOException::class)
    fun deleteWithNested() {
        val extraPath = folder.root.resolve("extra")
        Files.createDirectory(extraPath)
        val leafPath = extraPath.resolve("extra.txt")
        Files.createFile(leafPath)
        FileUtility.delete(folder.root)
        assertThat(folder.root.toFile().exists()).isFalse()
    }

    @Test
    @Throws(IOException::class)
    fun wipeWithNestedLeaf() {
        val extraPath = folder.root.resolve("extra")
        Files.createDirectory(extraPath)
        val leafPath = extraPath.resolve("extra.txt")
        Files.createFile(leafPath)
        FileUtility.wipe(folder.root, reporter)
        assertThat(folder.root.toFile().listFiles().size).isEqualTo(0)
        folder.delete()
    }

    @Test
    @Throws(IOException::class)
    fun wipeWithNestedFolder() {
        val extraPath = folder.root.resolve("extra")
        Files.createDirectory(extraPath)
        val leafPath = extraPath.resolve("extra.txt")
        Files.createFile(leafPath)
        FileUtility.wipe(folder.root, reporter)
        assertThat(folder.root.toFile().listFiles().size).isEqualTo(0)
        folder.delete()
    }

    @Test
    @Throws(IOException::class)
    fun fileAndFolderExists() {
        val extraPath = folder.root.resolve("extra")
        assertThat(folderExists(extraPath)).isFalse()
        Files.createDirectory(extraPath)
        assertThat(folderExists(extraPath)).isTrue()
        assertThat(fileExists(extraPath)).isFalse()

        val leafPath = extraPath.resolve("extra.txt")
        assertThat(fileExists(leafPath)).isFalse()
        Files.createFile(leafPath)
        assertThat(fileExists(leafPath)).isTrue()
        assertThat(folderExists(leafPath)).isFalse()
        folder.delete()
    }

    @Test
    @Throws(IOException::class)
    fun mkReadOnly() {
        if (FileUtility.isPosixSystem) {
            val extraText = folder.root.resolve("extra.txt")
            Files.createFile(extraText)
            FileUtility.makeReadOnly(extraText)
            val result = Files.getPosixFilePermissions(extraText).stream().toList()
            assertThat(result).doesNotContain(
                PosixFilePermission.GROUP_WRITE,
                PosixFilePermission.GROUP_EXECUTE,
                PosixFilePermission.OWNER_WRITE,
                PosixFilePermission.OTHERS_WRITE
            )
        }
        folder.delete()
    }
}
