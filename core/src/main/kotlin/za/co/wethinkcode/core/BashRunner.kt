package za.co.wethinkcode.core

import org.buildobjects.process.ProcBuilder
import org.buildobjects.process.ProcResult
import org.buildobjects.process.TimeoutException
import java.nio.file.Path
import java.util.stream.IntStream

class BashRunner(val args: List<String>, val workingFolder: Path, private val reporter: Reporter) : PickRunnable {
    override fun pick(submission: Path) {
        val interpreter = BashInterpreter(reporter)
        interpreter.interpret(bash(submission))
    }


    fun bash(submission: Path): BashResult {
        val stdout = BashStreamConsumer()
        val stderr = BashStreamConsumer()

        val builder: ProcBuilder = ProcBuilder(args[0])
            .ignoreExitStatus()
            .withTimeoutMillis(TIMEOUT_MS)
            .withVar("EXERCISE_FOLDER", submission.toString())
            .withErrorConsumer(stderr)
            .withOutputConsumer(stdout)
            .withWorkingDirectory(workingFolder.toFile())
        IntStream.range(1, args.size).mapToObj { index: Int -> args[index] }
            .forEach { arg: String? -> builder.withArg(arg) }
        try {
            println("Working [$workingFolder]")
            val result: ProcResult = builder.run()
            stderr.thread!!.join()
            stdout.thread!!.join()
            return BashResult(
                result.getCommandLine(),
                result.getExitValue(),
                stdout.saver!!.lines,
                stderr.saver!!.lines
            )
        } catch (ignored: TimeoutException) {
            throw PickTimeout(builder.getCommandLine(), reporter)
        } catch (e: InterruptedException) {
            throw RuntimeException(e)
        }
    }

    companion object {
        const val GIT_BASH_PATH: String = "C:/Program Files/Git/bin/bash.exe"
        const val LINUX_BASH_PATH: String = "/usr/bin/bash"
        const val MACOS_BASH_PATH: String = "/bin/bash"

        const val TIMEOUT_MS: Long = 60000L
    }
}
