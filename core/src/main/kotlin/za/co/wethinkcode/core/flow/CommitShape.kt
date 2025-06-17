package za.co.wethinkcode.core.flow


class CommitShape(val commit: Commit, lastUpperRight: FlowPoint) :
    FlowShape {
    override val detail = commit.detail

    override val x: Int = lastUpperRight.x
    override val y: Int = 0
    override val tip: String = "${detail.type.name}: ${detail.timestamp}"
    override val width: Int = commit.width
    override val height: Int = lastUpperRight.y + 1
    override val kind: FlowShape.Kind
        get() = when (detail.type) {
            RunType.commit -> FlowShape.Kind.commit
            RunType.local -> FlowShape.Kind.local
            else -> FlowShape.Kind.error
        }
}