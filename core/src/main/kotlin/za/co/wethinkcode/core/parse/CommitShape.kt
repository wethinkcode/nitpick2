package za.co.wethinkcode.core.parse


class CommitShape(val commit: Commit, lastUpperRight: LogPoint) :
    LogShape {
    override val detail = commit.detail

    override val x: Int = lastUpperRight.x
    val width: Int = commit.width
    val height: Int = lastUpperRight.y + 1
}