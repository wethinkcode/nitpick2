package za.co.wethinkcode.core.flow


class CommitShape(val commit: Commit, lastUpperRight: FlowPoint) :
    FlowShape {
    override val detail = commit.detail

    override val x: Int = lastUpperRight.x
    override val tip: String = "${detail.type.name}: ${detail.timestamp}"
    val width: Int = commit.width
    val height: Int = lastUpperRight.y + 1
}