package za.co.wethinkcode.core.flow

import java.awt.Color

class BarShape(
    override val detail: FlowDetail,
    lastUpperRight: FlowPoint,
    val fill: Color,
    val border: Color
) :
    FlowShape {
    override val x = lastUpperRight.x

    val width = 1
    val height = Math.max(1, lastUpperRight.y)
}