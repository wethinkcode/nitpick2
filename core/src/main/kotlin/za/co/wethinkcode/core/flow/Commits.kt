package za.co.wethinkcode.core.flow

import org.eclipse.jgit.lib.Constants
import org.eclipse.jgit.revwalk.RevSort
import org.eclipse.jgit.revwalk.RevWalk
import org.eclipse.jgit.storage.file.FileRepositoryBuilder
import java.nio.file.Path
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class Commits : MutableSet<Commit> by sortedSetOf(CommitComparator()) {
    val collatedTests = CollatedTests()

    val height get() = maxOf { it.height } + 1
    val width get() = sumOf { it.width }

    class CommitComparator : Comparator<Commit> {
        override fun compare(p0: Commit?, p1: Commit?): Int {
            if (p0 == null || p1 == null) return 0
            return p0.detail.timestamp.compareTo(p1.detail.timestamp)
        }
    }

    operator fun get(index: Int): Commit = this.toList()[index]

    fun layoutToShapes(shapes: MutableList<FlowShape>) {
        var previousUpperRight = FlowPoint(0, 0)
        forEach { commit ->
            previousUpperRight = commit.layoutToShapes(previousUpperRight, shapes, collatedTests)
        }
    }

    fun load(path: Path, earliest: String) {
        val repo = FileRepositoryBuilder().findGitDir(path.toFile()).build()
        val walk = RevWalk(repo)
        walk.markStart(walk.parseCommit(repo.resolve(Constants.HEAD)))
        walk.sort(RevSort.COMMIT_TIME_DESC)
        for (commit in walk) {
            val committerIdent = commit.committerIdent
            val name = committerIdent.name
            val email = committerIdent.emailAddress.substringBefore("@")
            val timestamp = timestampFrom(committerIdent.`when`)
            val shortMessage = commit.shortMessage
            val hash = commit.name
            val detail = FlowDetail("main", RunType.commit, timestamp, name, email)
            val commit = Commit(detail)
            if (timestamp < earliest) break;
            add(commit)
        }
        walk.close()
        repo.close()
    }

    companion object {

        fun timestampFrom(time: Date): String {
            val a = LocalDateTime.ofInstant(time.toInstant(), ZoneId.systemDefault())
            return a.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME).split("\\.".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray().get(0)
        }
    }
}