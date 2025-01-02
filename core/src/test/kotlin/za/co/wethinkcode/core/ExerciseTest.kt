package za.co.wethinkcode.core

import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import za.co.wethinkcode.core.exceptions.ExerciseNoLms
import za.co.wethinkcode.core.exceptions.ExerciseNotFound
import za.co.wethinkcode.core.exceptions.NoDslFileFound
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.util.*

class ExerciseTest {
    var reporter: Reporter = CollectingReporter()
    var folder: TestFolder = TestFolder(reporter)
    var exercise: Exercise = Exercise(folder.root, reporter)


    @Test
    @Throws(IOException::class)
    fun basicExercisePaths() {
        Assertions.assertThat(exercise.root).isEqualTo(folder.root)
        Assertions.assertThat(exercise.lms).isEqualTo(folder.root.resolve(LMS_FOLDER))
        Assertions.assertThat(exercise.vault).isEqualTo(folder.root.resolve(VAULT_FOLDER))
        Assertions.assertThat(exercise.protection)
            .isEqualTo(folder.root.resolve(LMS_FOLDER).resolve(PROTECTED_TXT_LEAF))
        Assertions.assertThat(exercise.dsl).isEqualTo(folder.root.resolve(LMS_FOLDER).resolve(DSL_LEAF))
        folder.delete()
    }

    @Test
    @Throws(IOException::class)
    fun validatesNoExerciseFolder() {
        org.junit.jupiter.api.Assertions.assertThrows(
            ExerciseNotFound::class.java
        ) { Exercise(Path.of("../testData/doesNotExist"), reporter).validate() }
        folder.delete()
    }

    @Test
    @Throws(IOException::class)
    fun validatesNoPickDslFound() {
        org.junit.jupiter.api.Assertions.assertThrows(
            NoDslFileFound::class.java
        ) { Exercise(Path.of("../testData/authorNoPick/exercise"), reporter).validate() }
        folder.delete()
    }

    @Test
    @Throws(IOException::class)
    fun validatesMissingLmsFolder() {
        org.junit.jupiter.api.Assertions.assertThrows(
            ExerciseNoLms::class.java
        ) { Exercise(Path.of("../testData/authorNoLms"), reporter).validate() }
        folder.delete()
    }

    @Test
    @Throws(IOException::class)
    fun wipesDestinationExceptForDotGit() {
        val gitPath = folder.root.resolve(".git")
        Files.createDirectory(gitPath)
        val somePath = folder.root.resolve("somefile.txt")
        Files.createFile(somePath)
        exercise.makeOrWipe()
        Assertions.assertThat(somePath.toFile().exists()).isFalse()
        Assertions.assertThat(gitPath.toFile().exists()).isTrue()
        folder.delete()
    }

    @Test
    @Throws(IOException::class)
    fun copiesAllFilesFromSource() {
        val source = Exercise(Path.of("../testData/authorSimple/exercise"), reporter)
        exercise.copyFrom(source.root)
        val atRoot = folder.root.toFile().listFiles()
        val names = Arrays.stream(atRoot).map { file: File -> file.name }
        Assertions.assertThat(names).contains(".lms", "main.py", "test_main.py")
        folder.delete()
    }

    @Test
    @Throws(IOException::class)
    fun getsProtectedPathsFromLms() {
        val source = Exercise(Path.of("../testData/authorSimple/protectedExercise"), reporter)
        val toProtects = source.protectedFilesFromLms()
        val justPaths = toProtects.stream().map { subpath: Subpath -> subpath.path }
        Assertions.assertThat(justPaths).containsExactlyInAnyOrder(
            Path.of("also"),
            Path.of("never_touch_me.txt"),
            Path.of("also/or_me.nope"),
            Path.of("also/not_even_me.nope")
        )
        folder.delete()
    }

    @Test
    @Throws(IOException::class)
    fun getsEmptyProtectedPathsFromLms() {
        val source = Exercise(Path.of("../testData/authorSimple/exercise"), reporter)
        val toProtects = source.protectedFilesFromLms()
        Assertions.assertThat(toProtects).isEmpty()
        folder.delete()
    }

    @Test
    @Throws(IOException::class)
    fun savesProtectedFiles() {
        val lms = Exercise(Path.of("../testData/authorSimple/protectedExercise"), reporter)
        exercise.makeFromLms(lms)
        val neverTouchMe = exercise.vault.resolve("never_touch_me.txt")
        assertThat(FileUtility.fileExists(neverTouchMe)).isTrue()
        val orMe = exercise.vault.resolve("also/or_me.nope")
        assertThat(FileUtility.fileExists(orMe)).isTrue()
        val notEvenMe = exercise.vault.resolve("also/not_even_me.nope")
        assertThat(FileUtility.fileExists(notEvenMe)).isTrue()
        folder.delete()
    }
}
