package za.co.wethinkcode.core.flow

import kotlin.math.max


class CommitShape(val commit: Commit, lastUpperRight: FlowPoint) :
    FlowShape {
    override val test: TestResult = TestResult.NONE
    override val detail = commit.detail
    override val isCommit: Boolean = true
    override val x: Int = lastUpperRight.x
    override val y: Int = 0
    override val tip: String = "${detail.type.name}: ${detail.timestamp}"
    override val width: Int = commit.width
    override val height: Int = max(2, lastUpperRight.y + 1)
    override val kind: FlowShape.Kind
        get() = when (detail.type) {
            RunType.commit -> FlowShape.Kind.commit
            RunType.local -> FlowShape.Kind.local
            else -> FlowShape.Kind.error
        }

    init {
        println(lastUpperRight)
    }
}