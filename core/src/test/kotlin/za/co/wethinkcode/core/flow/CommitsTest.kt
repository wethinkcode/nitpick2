package za.co.wethinkcode.core.flow

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import za.co.wethinkcode.core.TestFolder
import java.nio.file.Path


class CommitsTest {

    val commits = Commits()
    val folder = TestFolder()

    @Test
    fun stopsAtTimeStamp() {
        commits.load(Path.of(".."), "2025-03-17T12:52:30")
        assertThat(commits.size).isGreaterThan(3)
    }

    @Test
    fun zeroCommitsInRepo() {
    }
}