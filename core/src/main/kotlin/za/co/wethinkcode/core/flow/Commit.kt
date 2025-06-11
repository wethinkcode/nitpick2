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

    fun owns(run: FlowDetail): Boolean {
        return run.timestamp < detail.timestamp
    }

    fun layoutToShapes(
        lastUpperRight: FlowPoint,
        shapes: MutableList<FlowShape>,
        collatedTests: CollatedTests,
        newShapes: MutableList<NewShape>
    ): FlowPoint {
        val upperRightFromRuns = layoutAllRuns(lastUpperRight, shapes, collatedTests, newShapes)
        val shape = CommitShape(
            this,
            FlowPoint(lastUpperRight.x, upperRightFromRuns.y)
        )
        // shapes.add(shape)
        val height = Math.max(1, upperRightFromRuns.y)
        for (y in 1..height) {
            val shape = NewShape(FlowPoint(upperRightFromRuns.x, y), detail, "Commit: ${detail.timestamp}")
            newShapes.add(shape)
        }
        for (x in lastUpperRight.x..upperRightFromRuns.x) {
            val shape = NewShape(FlowPoint(x, 0), detail, "Commit: ${detail.timestamp}")
            newShapes.add(shape)
        }
        return FlowPoint(upperRightFromRuns.x + 1, upperRightFromRuns.y)
    }

    private fun layoutAllRuns(
        previousUpperRight: FlowPoint,
        shapes: MutableList<FlowShape>,
        collatedTests: CollatedTests,
        newShapes: MutableList<NewShape>
    ): FlowPoint {
        var currentUpperRight = previousUpperRight
        forEach { run -> currentUpperRight = run.layoutOneRun(currentUpperRight, shapes, collatedTests, newShapes) }
        return currentUpperRight
    }
}
