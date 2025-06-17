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
        get() = Math.max(size + 1, 1)

    operator fun get(index: Int): FlowDetail = this.toList()[index]

    fun owns(run: FlowDetail): Boolean {
        return run.timestamp < detail.timestamp
    }

    fun layoutToShapes(
        lastUpperRight: FlowPoint,
        shapes: MutableList<FlowShape>,
        collatedTests: CollatedTests
    ): FlowPoint {
        val upperRightFromRuns = layoutAllRuns(lastUpperRight, shapes, collatedTests)
        val shape = CommitShape(
            this,
            FlowPoint(lastUpperRight.x, upperRightFromRuns.y)
        )
        shapes.add(shape)
        return FlowPoint(upperRightFromRuns.x + 1, upperRightFromRuns.y)
    }

    private fun layoutAllRuns(
        previousUpperRight: FlowPoint,
        shapes: MutableList<FlowShape>,
        collatedTests: CollatedTests
    ): FlowPoint {
        var currentUpperRight = previousUpperRight
        forEach { run -> currentUpperRight = run.layoutOneRun(currentUpperRight, shapes, collatedTests) }
        return currentUpperRight
    }
}
