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
            RunType.base64 -> layoutError(previousUpperRight, shapes)
            else -> layoutUnknown(previousUpperRight, shapes)
        }
    }

    private fun layoutUnknown(
        previousUpperRight: FlowPoint,
        shapes: MutableList<FlowShape>
    ): FlowPoint {
        shapes.add(BarShape(this, previousUpperRight))
        return FlowPoint(previousUpperRight.x + 1, previousUpperRight.y)
    }

    private fun layoutError(
        previousUpperRight: FlowPoint,
        shapes: MutableList<FlowShape>
    ): FlowPoint {
        shapes.add(BarShape(this, previousUpperRight))
        return FlowPoint(previousUpperRight.x + 1, previousUpperRight.y)
    }

    private fun layoutRun(
        previousUpperRight: FlowPoint,
        shapes: MutableList<FlowShape>
    ): FlowPoint {
        shapes.add(BarShape(this, previousUpperRight))
        return FlowPoint(previousUpperRight.x + 1, previousUpperRight.y)
    }

    private fun layoutTest(
        previousUpperRight: FlowPoint,
        shapes: MutableList<FlowShape>,
        collatedTests: CollatedTests
    ): FlowPoint {
        collatedTests.add(passes, fails, disables, aborts)
        var y = 1
        for (result in collatedTests.toList()) {
            shapes.add(TestShape(this, previousUpperRight.x, y, result))
            y += 1
        }
        collatedTests.endRun()
        return FlowPoint(previousUpperRight.x + 1, y - 1)
    }
}
