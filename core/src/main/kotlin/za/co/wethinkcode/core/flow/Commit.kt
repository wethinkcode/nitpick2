package za.co.wethinkcode.core.flow


class Commit(val detail: FlowDetail) : MutableSet<FlowDetail> by FlowDetailsByTimestamp() {

    val height: Int
        get() {
            if (isEmpty()) return 2
            else return maxOf { entry ->
                when (entry.type) {
                    RunType.test -> entry.total
                    else -> 1
                }
            } + 1
        }

    val width: Int
        get() = Math.max(size + 1, 2)

    operator fun get(index: Int): FlowDetail = this.toList()[index]

    fun owns(run: FlowDetail): Boolean =
        run.timestamp < detail.timestamp
                && (run.email.startsWith(detail.email) || detail.email.startsWith(run.email))
                && run.branch == detail.branch

    fun layoutToShapes(
        previousUpperRight: FlowPoint,
        shapes: MutableList<FlowShape>,
        testCollator: TestResults
    ): FlowPoint {
        val upperRightFromRuns = layoutAllRuns(previousUpperRight, shapes, testCollator)
        val shape = CommitShape(
            this,
            FlowPoint(previousUpperRight.x, upperRightFromRuns.y)
        )
        shapes.add(shape)
        return FlowPoint(upperRightFromRuns.x + 1, upperRightFromRuns.y)
    }

    private fun layoutAllRuns(
        previousUpperRight: FlowPoint,
        shapes: MutableList<FlowShape>,
        testCollator: TestResults
    ): FlowPoint {
        var currentUpperRight = previousUpperRight
        for (run in this) {
            currentUpperRight = layoutOneRun(run, currentUpperRight, shapes, testCollator)
        }
        return currentUpperRight
    }

    private fun layoutOneRun(
        run: FlowDetail,
        previousUpperRight: FlowPoint,
        shapes: MutableList<FlowShape>,
        testCollator: TestResults
    ): FlowPoint {
        return when (run.type) {
            RunType.run -> layoutRun(run, previousUpperRight, shapes)
            RunType.test -> layoutTest(run, previousUpperRight, shapes, testCollator)
            RunType.base64 -> layoutError(run, previousUpperRight, shapes)
            else -> layoutUnknown(run, previousUpperRight, shapes)
        }
    }

    private fun layoutUnknown(
        run: FlowDetail,
        previousUpperRight: FlowPoint,
        shapes: MutableList<FlowShape>
    ): FlowPoint {
        shapes.add(BarShape(run, previousUpperRight))
        return FlowPoint(previousUpperRight.x + 1, previousUpperRight.y)
    }

    private fun layoutError(
        run: FlowDetail,
        previousUpperRight: FlowPoint,
        shapes: MutableList<FlowShape>
    ): FlowPoint {
        shapes.add(BarShape(run, previousUpperRight))
        return FlowPoint(previousUpperRight.x + 1, previousUpperRight.y)
    }

    private fun layoutRun(
        run: FlowDetail,
        previousUpperRight: FlowPoint,
        shapes: MutableList<FlowShape>
    ): FlowPoint {
        shapes.add(BarShape(run, previousUpperRight))
        return FlowPoint(previousUpperRight.x + 1, previousUpperRight.y)
    }

    private fun layoutTest(
        detail: FlowDetail,
        previousUpperRight: FlowPoint,
        shapes: MutableList<FlowShape>,
        testCollator: TestResults
    ): FlowPoint {
        testCollator.add(detail.passes, detail.fails, detail.disables, detail.aborts)
        var y = 1
        val resultCopy = testCollator.toList().reversed()
        for (result in testCollator.toList()) {
            shapes.add(TestShape(detail, previousUpperRight.x, y, result, resultCopy))
            y += 1
        }
        testCollator.endRun()
        return FlowPoint(previousUpperRight.x + 1, y - 1)
    }
}
