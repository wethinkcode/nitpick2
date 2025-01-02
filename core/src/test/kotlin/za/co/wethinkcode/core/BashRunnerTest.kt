package za.co.wethinkcode.core

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import java.io.IOException
import java.nio.file.Path
import java.util.*

internal class BashRunnerTest {
    var reporter: Reporter = CollectingReporter()

    private val NON_EXISTING_SH: List<String> = Arrays.asList(PickParser.bashPath(), "non-existing.sh")
    private val EXISTING_SH: List<String> = Arrays.asList(PickParser.bashPath(), "saysSomething.sh")
    private val FAILED_SH: List<String> = Arrays.asList(PickParser.bashPath(), "failing.sh")
    private val STDERR_SH: List<String> = Arrays.asList(PickParser.bashPath(), "stderr.sh")
    private val STDOUT_SH: List<String> = Arrays.asList(PickParser.bashPath(), "stdout.sh")
    private val shellFolder: Path = Path.of("../testData/bash")
    private val exerciseFolder: Path = Path.of("someExerciseFolder")

    @Test
    @Throws(IOException::class)
    fun handlesNonExistingShell() {
        val bash = BashRunner(NON_EXISTING_SH, shellFolder, reporter)
        val result = bash.bash(exerciseFolder)
        Assertions.assertThat(result.code).isNotEqualTo(0)
    }

    @Test
    @Throws(IOException::class)
    fun handlesNonZeroShell() {
        val bash = BashRunner(FAILED_SH, shellFolder, reporter)
        val result = bash.bash(exerciseFolder)
        Assertions.assertThat(result.code).isEqualTo(1)
    }


    @Test
    @Throws(IOException::class)
    fun handlesZeroShell() {
        val bash = BashRunner(EXISTING_SH, shellFolder, reporter)
        val result = bash.bash(exerciseFolder)
        println(result.stdout)
        println(result.stderr)
        Assertions.assertThat(result.code).isEqualTo(0)
    }

    @Test
    @Throws(IOException::class)
    fun capturesStdErr() {
        val bash = BashRunner(STDERR_SH, shellFolder, reporter)
        val result = bash.bash(exerciseFolder)
        Assertions.assertThat(result.stderr.next()).isEqualTo("This is on stderr.")
        Assertions.assertThat(result.code).isEqualTo(0)
    }

    @Test
    @Throws(IOException::class)
    fun capturesStdOut() {
        val bash = BashRunner(STDOUT_SH, shellFolder, reporter)
        val result = bash.bash(exerciseFolder)
        Assertions.assertThat(result.stdout.next()).isEqualTo("This is on stdout.")
        Assertions.assertThat(result.code).isEqualTo(0)
    }
}