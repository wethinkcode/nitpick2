package za.co.wethinkcode.core

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.nio.file.Path

class SubpathLoaderTest {
    var reporter: Reporter = CollectingReporter()

    @Test
    fun loadsAllSubpaths() {
        val root = Path.of("../testData/subpaths").toAbsolutePath().normalize()
        val result: List<Subpath> = SubpathLoader.fetch(root, { path -> true }, reporter)
        assertThat(result.stream().map { subpath -> subpath.path }).containsExactly(
            Path.of("folder"),
            Path.of("folder/nestedOne.txt"),
            Path.of("folder/nestedTwo.txt"),
            Path.of("topLevel.txt")
        )
    }

    @Test
    fun loadsAllSubpathsButTopLevel() {
        val root = Path.of("../testData/subpaths").toAbsolutePath().normalize()
        val matcher = MatchByGlob("folder**")
        val result: List<Subpath> = SubpathLoader.fetch(root, matcher, reporter)
        assertThat(result.stream().map { subpath -> subpath.path }).containsExactly(
            Path.of("folder"),
            Path.of("folder/nestedOne.txt"),
            Path.of("folder/nestedTwo.txt")
        )
    }

    @Test
    fun loadsAllSubpathsButNested() {
        val root = Path.of("../testData/subpaths").toAbsolutePath().normalize()
        val matcher = MatchByGlob("*")
        val result: List<Subpath> = SubpathLoader.fetch(root, matcher, reporter)
        assertThat(result.stream().map { subpath -> subpath.path }).containsExactly(
            Path.of("folder"),
            Path.of("topLevel.txt")
        )
    }
}
