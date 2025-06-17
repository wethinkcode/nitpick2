package za.co.wethinkcode.core.flow

class BarShape(
    override val detail: FlowDetail,
    lastUpperRight: FlowPoint
) :
    FlowShape {
    override val x = lastUpperRight.x
    override val y = 1
    override val tip: String = "${detail.type.name}: ${detail.timestamp}"

    override val width = 1
    override val height = Math.max(1, lastUpperRight.y)

    override val kind: FlowShape.Kind
        get() = when (detail.type) {
            RunType.base64 -> FlowShape.Kind.base64
            RunType.run -> FlowShape.Kind.run
            RunType.unknown -> FlowShape.Kind.unknown
            else -> FlowShape.Kind.error
        }
}