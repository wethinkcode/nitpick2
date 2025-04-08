package za.co.wethinkcode.core.flow

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import za.co.wethinkcode.core.TestFolder
import java.nio.file.Path
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit


class CommitsTest {

    val commits = Commits()
    val folder = TestFolder()

    @Test
    fun stopsAtTimeStamp() {
        commits.load(Path.of(".."), "2025-03-17T12:52:30")
        assertThat(commits.size).isGreaterThan(3)
        folder.delete()
    }

    @Test
    fun zeroCommitsMeansZeroCommits() {
        folder.initGitRepo()
        commits.load(folder.root, nowPlusOne())
        assertThat(commits.size).isEqualTo(0)
        folder.delete()
    }

    @Test
    fun uninitializedMeansZeroCommits() {
        folder.initGitRepo()
        folder.writeSourceAndAdd()
        folder.commit()
        commits.load(folder.root, nowPlusOne())
        assertThat(commits.size).isEqualTo(0)
        folder.delete()
    }

    @Test
    fun initializedZeroCommitsMeansZeroCommits() {
        folder.initGitRepo()
        folder.writeSourceAndAdd()
        folder.commit()
        folder.runLog()
        commits.load(folder.root, nowPlusOne())
        assertThat(commits.size).isEqualTo(0)
        folder.delete()
    }

    @Test
    fun initializedCommitsMeansOneCommit() {
        folder.initGitRepo()
        folder.writeSourceAndAdd()
        folder.runLog()
        val earliest = now()
        folder.commit()
        commits.load(folder.root, earliest)
        assertThat(commits.size).isEqualTo(1)
    }

    fun now(): String {
        val now = LocalDateTime.now()
        return now.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME).split("\\.".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray().get(0);
    }

    fun nowPlusOne(): String {
        val now = LocalDateTime.now()
        val plus = now.plus(1, ChronoUnit.SECONDS)

        return plus.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME).split("\\.".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray().get(0);
    }
}