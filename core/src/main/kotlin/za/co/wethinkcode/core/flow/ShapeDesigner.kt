package za.co.wethinkcode.core.flow

import java.awt.Color

class ShapeDesigner {
    val results = TestResults()

    fun design(commits: Commits): FlowShapes {
        if (commits.isEmpty()) {
            return FlowShapes(1, 1)
        }
        val height = commits.maxOf { it.height } + 1
        val width = commits.sumOf { it.width }
        val shapes = FlowShapes(height, width)
        var previousUpperRight = FlowPoint(0, 0)
        for (commit in commits) {
            previousUpperRight = commitShape(commit, previousUpperRight, shapes)
        }
        return shapes
    }

    private fun commitShape(
        commit: Commit,
        previousUpperRight: FlowPoint,
        paths: FlowShapes
    ): FlowPoint {
        val upperRightFromRuns = runsForCommit(previousUpperRight, commit, paths)
        paths.shapes.add(
            CommitShape(
                commit,
                FlowPoint(previousUpperRight.x, upperRightFromRuns.y)
            )
        )
        return FlowPoint(upperRightFromRuns.x + 1, upperRightFromRuns.y)
    }

    private fun runsForCommit(
        previousUpperRight: FlowPoint,
        commit: Commit,
        paths: FlowShapes
    ): FlowPoint {
        var currentUpperRight = previousUpperRight
        for (run in commit) {
            currentUpperRight = runShape(run, currentUpperRight, paths)
        }
        return currentUpperRight
    }

    private fun runShape(
        run: FlowDetail,
        previousUpperRight: FlowPoint,
        paths: FlowShapes
    ): FlowPoint {
        when (run.type) {
            RunType.run -> return makeRunShape(run, previousUpperRight, paths)
            RunType.test -> return makeTestShape(run, previousUpperRight, paths)
            RunType.base64 -> return makeBase64Shape(run, previousUpperRight, paths)
            else -> return makeUnknownShape(run, previousUpperRight, paths)
        }
    }

    private fun makeUnknownShape(
        run: FlowDetail,
        previousUpperRight: FlowPoint,
        paths: FlowShapes
    ): FlowPoint {
        paths.shapes.add(BarShape(run, previousUpperRight, Color.blue, Color.BLACK))
        return FlowPoint(previousUpperRight.x + 1, previousUpperRight.y)
    }

    private fun makeBase64Shape(
        run: FlowDetail,
        previousUpperRight: FlowPoint,
        paths: FlowShapes
    ): FlowPoint {
        paths.shapes.add(BarShape(run, previousUpperRight, Color.YELLOW, Color.BLACK))
        return FlowPoint(previousUpperRight.x + 1, previousUpperRight.y)
    }

    private fun makeRunShape(
        run: FlowDetail,
        previousUpperRight: FlowPoint,
        paths: FlowShapes
    ): FlowPoint {
        paths.shapes.add(BarShape(run, previousUpperRight, Color.darkGray, Color.darkGray))
        return FlowPoint(previousUpperRight.x + 1, previousUpperRight.y)
    }

    private fun makeTestShape(
        detail: FlowDetail,
        previousUpperRight: FlowPoint,
        paths: FlowShapes
    ): FlowPoint {
        results.add(detail.passes, detail.fails, detail.disables, detail.aborts)
        var y = 1
        val resultCopy = results.toList().reversed()
        for (result in results.toList()) {
            paths.shapes.add(TestShape(detail, previousUpperRight.x, y, result, resultCopy))
            y += 1
        }
        results.endRun()
        return FlowPoint(previousUpperRight.x + 1, y - 1)
    }
}