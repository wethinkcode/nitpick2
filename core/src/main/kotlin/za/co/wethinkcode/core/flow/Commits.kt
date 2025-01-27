package za.co.wethinkcode.core.flow

import java.awt.Color

class Commits : MutableSet<Commit> by sortedSetOf(CommitComparator()) {
    val testCollator = TestResults()

    val height get() = maxOf { it.height } + 1
    val width get() = sumOf { it.width }

    class CommitComparator : Comparator<Commit> {
        override fun compare(p0: Commit?, p1: Commit?): Int {
            if (p0 == null || p1 == null) return 0
            return p0.detail.timestamp.compareTo(p1.detail.timestamp)
        }
    }

    operator fun get(index: Int): Commit = this.toList()[index]


    fun layoutToShapes(shapes: MutableList<FlowShape>): FlowShapes {
        if (isEmpty()) {
            return FlowShapes(1, 1)
        }
        val height = maxOf { it.height } + 1
        val width = sumOf { it.width }
        val flowShapes = FlowShapes(height, width)
        var previousUpperRight = FlowPoint(0, 0)
        forEach { commit ->
            previousUpperRight = commitShape(commit, previousUpperRight, flowShapes, shapes)
        }
        return flowShapes
    }

    private fun commitShape(
        commit: Commit,
        previousUpperRight: FlowPoint,
        paths: FlowShapes,
        shapes: MutableList<FlowShape>
    ): FlowPoint {
        val upperRightFromRuns = runsForCommit(previousUpperRight, commit, paths, shapes)
        val shape = CommitShape(
            commit,
            FlowPoint(previousUpperRight.x, upperRightFromRuns.y)
        )
        paths.shapes.add(shape)
        shapes.add(shape)
        return FlowPoint(upperRightFromRuns.x + 1, upperRightFromRuns.y)
    }

    private fun runsForCommit(
        previousUpperRight: FlowPoint,
        commit: Commit,
        paths: FlowShapes,
        shapes: MutableList<FlowShape>
    ): FlowPoint {
        var currentUpperRight = previousUpperRight
        for (run in commit) {
            currentUpperRight = runShape(run, currentUpperRight, paths, shapes)
        }
        return currentUpperRight
    }

    private fun runShape(
        run: FlowDetail,
        previousUpperRight: FlowPoint,
        paths: FlowShapes,
        shapes: MutableList<FlowShape>
    ): FlowPoint {
        return when (run.type) {
            RunType.run -> makeRunShape(run, previousUpperRight, paths, shapes)
            RunType.test -> makeTestShape(run, previousUpperRight, paths, shapes)
            RunType.base64 -> makeBase64Shape(run, previousUpperRight, paths, shapes)
            else -> makeUnknownShape(run, previousUpperRight, paths, shapes)
        }
    }

    private fun makeUnknownShape(
        run: FlowDetail,
        previousUpperRight: FlowPoint,
        paths: FlowShapes,
        shapes: MutableList<FlowShape>
    ): FlowPoint {
        val element = BarShape(run, previousUpperRight, Color.blue, Color.BLACK)
        paths.shapes.add(element)
        shapes.add(element)
        return FlowPoint(previousUpperRight.x + 1, previousUpperRight.y)
    }

    private fun makeBase64Shape(
        run: FlowDetail,
        previousUpperRight: FlowPoint,
        paths: FlowShapes,
        shapes: MutableList<FlowShape>
    ): FlowPoint {
        val element = BarShape(run, previousUpperRight, Color.YELLOW, Color.BLACK)
        paths.shapes.add(element)
        shapes.add(element)
        return FlowPoint(previousUpperRight.x + 1, previousUpperRight.y)
    }

    private fun makeRunShape(
        run: FlowDetail,
        previousUpperRight: FlowPoint,
        paths: FlowShapes,
        shapes: MutableList<FlowShape>
    ): FlowPoint {
        val element = BarShape(run, previousUpperRight, Color.darkGray, Color.darkGray)
        paths.shapes.add(element)
        shapes.add(element)
        return FlowPoint(previousUpperRight.x + 1, previousUpperRight.y)
    }

    private fun makeTestShape(
        detail: FlowDetail,
        previousUpperRight: FlowPoint,
        paths: FlowShapes,
        shapes: MutableList<FlowShape>
    ): FlowPoint {
        testCollator.add(detail.passes, detail.fails, detail.disables, detail.aborts)
        var y = 1
        val resultCopy = testCollator.toList().reversed()
        for (result in testCollator.toList()) {
            val element = TestShape(detail, previousUpperRight.x, y, result, resultCopy)
            paths.shapes.add(element)
            shapes.add(element)
            y += 1
        }
        testCollator.endRun()
        return FlowPoint(previousUpperRight.x + 1, y - 1)
    }
}