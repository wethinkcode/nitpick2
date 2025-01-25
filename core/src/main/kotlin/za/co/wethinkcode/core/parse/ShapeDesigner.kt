package za.co.wethinkcode.core.parse

import java.awt.Color

class ShapeDesigner {
    val results = TestResults()

    fun design(commits: Commits): LogShapes {
        if (commits.isEmpty()) {
            return LogShapes(1, 1)
        }
        val height = commits.maxOf { it.height } + 1
        val width = commits.sumOf { it.width }
        val shapes = LogShapes(height, width)
        var previousUpperRight = LogPoint(0, 0)
        for (commit in commits) {
            previousUpperRight = commitShape(commit, previousUpperRight, shapes)
        }
        return shapes
    }

    private fun commitShape(
        commit: Commit,
        previousUpperRight: LogPoint,
        paths: LogShapes
    ): LogPoint {
        val upperRightFromRuns = runsForCommit(previousUpperRight, commit, paths)
        paths.shapes.add(
            CommitShape(
                commit,
                LogPoint(previousUpperRight.x, upperRightFromRuns.y)
            )
        )
        return LogPoint(upperRightFromRuns.x + 1, upperRightFromRuns.y)
    }

    private fun runsForCommit(
        previousUpperRight: LogPoint,
        commit: Commit,
        paths: LogShapes
    ): LogPoint {
        var currentUpperRight = previousUpperRight
        for (run in commit) {
            currentUpperRight = runShape(run, currentUpperRight, paths)
        }
        return currentUpperRight
    }

    private fun runShape(
        run: LogDetail,
        previousUpperRight: LogPoint,
        paths: LogShapes
    ): LogPoint {
        when (run.type) {
            RunType.run -> return makeRunShape(run, previousUpperRight, paths)
            RunType.test -> return makeTestShape(run, previousUpperRight, paths)
            RunType.base64 -> return makeBase64Shape(run, previousUpperRight, paths)
            else -> return makeUnknownShape(run, previousUpperRight, paths)
        }
    }

    private fun makeUnknownShape(
        run: LogDetail,
        previousUpperRight: LogPoint,
        paths: LogShapes
    ): LogPoint {
        paths.shapes.add(BarShape(run, previousUpperRight, Color.blue, Color.BLACK))
        return LogPoint(previousUpperRight.x + 1, previousUpperRight.y)
    }

    private fun makeBase64Shape(
        run: LogDetail,
        previousUpperRight: LogPoint,
        paths: LogShapes
    ): LogPoint {
        paths.shapes.add(BarShape(run, previousUpperRight, Color.YELLOW, Color.BLACK))
        return LogPoint(previousUpperRight.x + 1, previousUpperRight.y)
    }

    private fun makeRunShape(
        run: LogDetail,
        previousUpperRight: LogPoint,
        paths: LogShapes
    ): LogPoint {
        paths.shapes.add(BarShape(run, previousUpperRight, Color.darkGray, Color.darkGray))
        return LogPoint(previousUpperRight.x + 1, previousUpperRight.y)
    }

    private fun makeTestShape(
        detail: LogDetail,
        previousUpperRight: LogPoint,
        paths: LogShapes
    ): LogPoint {
        results.add(detail.passes, detail.fails, detail.disables, detail.aborts)
        var y = 1
        val resultCopy = results.toList().reversed()
        for (result in results.toList()) {
            paths.shapes.add(TestShape(detail, previousUpperRight.x, y, result, resultCopy))
            y += 1
        }
        results.endRun()
        return LogPoint(previousUpperRight.x + 1, y - 1)
    }
}