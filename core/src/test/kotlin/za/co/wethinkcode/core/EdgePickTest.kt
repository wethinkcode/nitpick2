package za.co.wethinkcode.core

import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import java.io.IOException
import java.io.PrintStream
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption

class EdgePickTest {
    var outputter: CollectingOutputter = CollectingOutputter()
    var folder: TestFolder = TestFolder(outputter)

    @Test
    @Throws(IOException::class)
    fun leavesResultsBehind() {
        val lms = Exercise(Path.of("../testData/authorSimple/protectedExercise"), outputter)
        val exercise = Exercise(folder.root, outputter)
        exercise.makeFromLms(lms)
        val pick = EdgePick(folder.root.toString(), false, outputter)
        pick.run()
        Assertions.assertThat(folder.root.resolve(LONG_RESULT_LEAF)).exists()
        folder.delete()
    }

    @Test
    @Throws(IOException::class)
    fun leavesGradeResultsBehind() {
        val lms = Exercise(Path.of("../testData/authorSimple/exercise"), outputter)
        val exercise = Exercise(folder.root, outputter)
        exercise.makeFromLms(lms)
        val pick: LmsPick = LmsPick("../testData/authorSimple/exercise", folder.root.toString(), true, outputter)
        pick.run()
        Assertions.assertThat(folder.root.resolve(GRADE_RESULT_LEAF)).exists()
        val lines = Files.readAllLines(folder.root.resolve(GRADE_RESULT_LEAF))
        Assertions.assertThat(lines[0]).isEqualTo("GradePass")
        folder.delete()
    }

    @Test
    @Throws(IOException::class)
    fun doesNotRequireLmsFolderInLmsPick() {
        val lms = Exercise(Path.of("../testData/authorSimple/exercise"), outputter)
        val exercise = Exercise(folder.root, outputter)
        exercise.makeFromLms(lms)
        FileUtility.delete(folder.root.resolve(LMS_FOLDER))
        val pick: LmsPick = LmsPick("../testData/authorSimple/exercise", folder.root.toString(), false, outputter)
        pick.run()
        Assertions.assertThat(folder.root.resolve(GRADE_RESULT_LEAF)).exists()
        val lines = Files.readAllLines(folder.root.resolve(GRADE_RESULT_LEAF))
        Assertions.assertThat(lines[0]).isEqualTo("GradePass")
        folder.delete()
    }

    @Test
    @Throws(IOException::class)
    fun processesAlteredFilesWithOverwrite() {
        val lms = Exercise(Path.of("../testData/authorSimple/protectedExercise"), outputter)
        val exercise = Exercise(folder.root, outputter)
        exercise.makeFromLms(lms)
        val neverTouchMe = exercise.root.resolve("never_touch_me.txt")
        assertThat(FileUtility.fileExists(neverTouchMe)).isTrue()
        appendTo(neverTouchMe, "Some extra text.")
        val pick = EdgePick(folder.root.toString(), true, outputter)
        pick.processAlteredFiles()
        Assertions.assertThat(outputter.messages.size).isEqualTo(1)
        Assertions.assertThat(outputter.messages[0]!!.type).isEqualTo(MessageType.Altered)
        assertThat(
            FileUtility.isIdentical(
                lms.root.resolve("never_touch_me.txt"),
                neverTouchMe
            )
        ).isTrue()
        folder.delete()
    }

    @Test
    @Throws(IOException::class)
    fun processesAlteredFilesWithoutOverwrite() {
        val lms = Exercise(Path.of("../testData/authorSimple/protectedExercise"), outputter)
        val exercise = Exercise(folder.root, outputter)
        exercise.makeFromLms(lms)
        val neverTouchMe = exercise.root.resolve("never_touch_me.txt")
        assertThat(FileUtility.fileExists(neverTouchMe)).isTrue()
        appendTo(neverTouchMe, "Some extra text.")
        val pick = EdgePick(folder.root.toString(), false, outputter)
        pick.processAlteredFiles()
        Assertions.assertThat(outputter.messages.size).isEqualTo(1)
        Assertions.assertThat(outputter.messages[0]!!.type).isEqualTo(MessageType.Altered)
        assertThat(
            FileUtility.isIdentical(
                lms.root.resolve("never_touch_me.txt"),
                neverTouchMe
            )
        ).isFalse()
        folder.delete()
    }

    @Test
    @Tag("slow")
    @Throws(IOException::class)
    fun simpleJavaExerciseSucceeds() {
        val lms = Exercise(Path.of("../testData/authorSimpleJava/exercise"), outputter)
        val exercise = Exercise(folder.root, outputter)
        exercise.makeFromLms(lms)
        val pick: LmsPick = LmsPick("../testData/authorSimpleJava/exercise", folder.root.toString(), true, outputter)
        pick.run()
        Assertions.assertThat(folder.root.resolve(GRADE_RESULT_LEAF)).exists()
        val lines = Files.readAllLines(folder.root.resolve(GRADE_RESULT_LEAF))
        Assertions.assertThat(lines[0]).isEqualTo("GradePass")
        folder.delete()
    }

    @Test
    @Throws(IOException::class)
    fun processesJavaAlteredFilesWithOverwrite() {
        val lms = Exercise(Path.of("../testData/authorSimpleJava/protectedExercise"), outputter)
        val exercise = Exercise(folder.root, outputter)
        exercise.makeFromLms(lms)
        val neverChangeTheTest = exercise.root.resolve("src/test/java/za/co/wethinkcode/SayHelloTest.java")
        assertThat(FileUtility.fileExists(neverChangeTheTest)).isTrue()
        appendTo(neverChangeTheTest, "Some extra text.")
        val pick = EdgePick(folder.root.toString(), true, outputter)
        pick.processAlteredFiles()
        Assertions.assertThat(outputter.messages.size).isEqualTo(1)
        Assertions.assertThat(outputter.messages[0]!!.type).isEqualTo(MessageType.Altered)
        assertThat(
            FileUtility.isIdentical(
                lms.root.resolve("src/test/java/za/co/wethinkcode/SayHelloTest.java"),
                neverChangeTheTest
            )
        ).isTrue()
        folder.delete()
    }


    @Throws(IOException::class)
    fun appendTo(path: Path?, text: String?) {
        FileUtility.makeWritable(path)
        val stream = Files.newOutputStream(path, StandardOpenOption.APPEND)
        val writer = PrintStream(stream)
        writer.println(text)
        writer.flush()
        writer.close()
    }
}
