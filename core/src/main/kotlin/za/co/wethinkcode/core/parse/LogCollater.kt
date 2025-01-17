package za.co.wethinkcode.core.parse

class LogCollater() {
    fun collate(runs: List<LogDetail>): Commits {
        val commits = Commits()
        val sortedRuns = runs.sortedBy { it.timestamp }
        for (run in sortedRuns.filter { it.type == RunType.commit }) commits.add(Commit(run))
        for (run in sortedRuns.filter { it.type != RunType.commit }) {
            forceRunIntoCommit(commits, run)
        }
        return commits
    }

    private fun forceRunIntoCommit(commits: Commits, run: LogDetail) {
        for (commit in commits) {
            if (commit.owns(run)) {
                commit.add(run)
                return
            }
        }
        val commit = Commit(
            LogDetail(run.branch, RunType.local, "99999", run.committer, run.email)
        )
        commit.add(run)
        commits.add(commit)
    }
}