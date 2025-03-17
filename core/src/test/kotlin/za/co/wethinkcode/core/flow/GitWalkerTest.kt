package za.co.wethinkcode.core.flow

import org.eclipse.jgit.lib.Constants
import org.eclipse.jgit.revwalk.RevSort
import org.eclipse.jgit.revwalk.RevWalk
import org.eclipse.jgit.storage.file.FileRepositoryBuilder
import org.junit.jupiter.api.Test
import java.nio.file.Path


class GitWalkerTest {

    @Test
    fun basic() {
        val repo = FileRepositoryBuilder().findGitDir().build()
        val walk = RevWalk(repo)
        walk.markStart(walk.parseCommit(repo.resolve(Constants.HEAD)))
        walk.sort(RevSort.COMMIT_TIME_DESC) // chronological order
        for (commit in walk) {
            val time = commit.committerIdent.`when`
            val shortMessage = commit.shortMessage
            val hash = commit.name
            println("$hash $time $shortMessage")
        }
        walk.close()
    }

    fun gitRoot(from:Path):Path {
        var candidate = from.toAbsolutePath()
        while (candidate.nameCount > 1) {
            val candidateGit = candidate.resolve(".git")
            if (candidateGit.toFile().exists() && candidateGit.toFile().isDirectory) return candidate
            candidate = candidate.resolve("..").toAbsolutePath().normalize()
        }
        throw RuntimeException("No .git root.")
    }
}