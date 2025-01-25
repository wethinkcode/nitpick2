package za.co.wethinkcode.core.parse

import java.awt.Color

class BarShape(
    override val detail: LogDetail,
    lastUpperRight: LogPoint,
    val fill: Color,
    val border: Color
) :
    LogShape {
    override val x = lastUpperRight.x

    val width = 1
    val height = Math.max(1, lastUpperRight.y)
}