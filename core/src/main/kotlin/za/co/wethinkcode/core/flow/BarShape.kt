package za.co.wethinkcode.core.flow

class BarShape(
    override val detail: FlowDetail,
    lastUpperRight: FlowPoint
) :
    FlowShape {
    override val x = lastUpperRight.x

    val width = 1
    val height = Math.max(1, lastUpperRight.y)
}