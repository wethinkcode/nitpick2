package za.co.wethinkcode.core.flow

class BarShape(
    override val detail: FlowDetail,
    lastUpperRight: FlowPoint
) :
    FlowShape {
    override val x = lastUpperRight.x
    override val tip: String = "${detail.type.name}: ${detail.timestamp}"

    val width = 1
    val height = Math.max(1, lastUpperRight.y)
}