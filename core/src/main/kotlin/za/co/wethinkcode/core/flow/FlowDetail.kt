package za.co.wethinkcode.core.flow


data class FlowDetail(
    val branch: String,
    val type: RunType,
    val timestamp: String,
    val committer: String,
    val email: String,
    val passes: List<String> = emptyList(),
    val fails: List<String> = emptyList(),
    val disables: List<String> = emptyList(),
    val aborts: List<String> = emptyList(),
    val messages: List<String> = emptyList()
) {
    val total: Int get() = passes.size + fails.size + disables.size + aborts.size
    val isError: Boolean get() = messages.isNotEmpty()
    val summary: String
        get() {
            if (type == RunType.test) {
                return "test $total: ${passes.size}/${fails.size}/${disables.size}/${aborts.size}"
            }
            return type.name
        }

    fun layoutOneRun(
        previousUpperRight: FlowPoint,
        shapes: MutableList<FlowShape>,
        collatedTests: CollatedTests
    ): FlowPoint {
        return when (type) {
            RunType.run -> layoutRun(previousUpperRight, shapes)
            RunType.test -> layoutTest(previousUpperRight, shapes, collatedTests)
            RunType.run -> layoutRun(previousUpperRight, shapes)
            RunType.test -> layoutTest(previousUpperRight, shapes, collatedTests)
            RunType.base64 -> layoutError(previousUpperRight, shapes)
            else -> layoutUnknown(previousUpperRight, shapes)
        }
    }

    private fun layoutUnknown(
        previousUpperRight: FlowPoint,
        shapes: MutableList<FlowShape>
    ): FlowPoint {
        shapes.add(FlowShape.makeBarShape(this, previousUpperRight))
        return FlowPoint(previousUpperRight.x + 1, previousUpperRight.y)
    }

    private fun layoutError(
        previousUpperRight: FlowPoint,
        shapes: MutableList<FlowShape>
    ): FlowPoint {
        shapes.add(FlowShape.makeBarShape(this, previousUpperRight))
        return FlowPoint(previousUpperRight.x + 1, previousUpperRight.y)
    }

    private fun layoutRun(
        lastUpperRight: FlowPoint,
        shapes: MutableList<FlowShape>
    ): FlowPoint {
        shapes.add(FlowShape.makeBarShape(this, lastUpperRight))
        val height = Math.max(1, lastUpperRight.y)
        return FlowPoint(lastUpperRight.x + 1, lastUpperRight.y)
    }

    private fun layoutTest(
        previousUpperRight: FlowPoint,
        shapes: MutableList<FlowShape>,
        collatedTests: CollatedTests
    ): FlowPoint {
        collatedTests.begin()
        collatedTests.add(passes, fails, disables, aborts)
        var y = 1
        val testList = collatedTests.toList().reversed()
        for (result in collatedTests.toList()) {
            shapes.add(
                FlowShape.makeTestShape(this, previousUpperRight.x, y, result, testList)
            )
            y += 1
        }
        return FlowPoint(previousUpperRight.x + 1, y - 1)
    }
}
