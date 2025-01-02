package za.co.wethinkcode.core

import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import java.nio.file.Path

class GenerateTest {
    var outputter: CollectingReporter = CollectingReporter()

    @Test
    fun generatesCorrectDestinationPath() {
        val generate = Generate(AUTHOR_SIMPLE, Generate.NO_DESTINATION_GIVEN, outputter)
        val actual = generate.makeDestinationPath()
        Assertions.assertThat(actual).endsWithRaw(Path.of("target/exercises/exercise"))
    }

    @Test
    fun addsRepoDetails() {
        val generate = Generate(AUTHOR_SIMPLE, Generate.NO_DESTINATION_GIVEN, outputter)
        generate.run()
        val generated = Path.of("../target/exercises/exercise/.lms/meta.txt")
        assertThat(FileUtility.fileExists(generated)).isTrue()
    }

    @Disabled("External file for test? C'mon, now, GeePaw.")
    @Tag("slow")
    @Test
    fun longPipesDoNotTimeout() {
        /*
        Java calls external processes using pipes. An external process that has no where to put
        its output will block. The buffer size on a pipe in *nix and MacOs is
        large, but on windows it is quite small, so on windows a typical maven run will fill the
        buffer and block, eventually timing out. Someone on another thread must be pulling the
        data from the pipe, which is what the StreamSaver class does.

        This slow test, which runs only on Windows, proves that the StreamSaver class is doing its
        job, actively reading from the pipes during the bash run.

        P.S. This was a hellacious bug to find and fix. :)
        */
        val os = System.getProperty("os.name")
        if (os.startsWith("Windows")) {
            val generate =
                Generate("../../ex-oop-bank-accounts/oop-011-solution", Generate.NO_DESTINATION_GIVEN, outputter)
            generate.run()
            val generated = Path.of("../target/exercises/exercise/.lms/meta.txt")
            assertThat(FileUtility.fileExists(generated)).isTrue()
        }
    }

    companion object {
        const val AUTHOR_SIMPLE: String = "../testData/authorSimple/exercise"
    }
}
