package za.co.wethinkcode.core.flow


interface FlowShape {

    enum class Kind {
        base64,
        error,
        unknown,
        run,
        commit,
        local,
        failed,
        passed,
        disabled,
        aborted,
        unrun,
    }

    val finner: Float get():Float = (x + width - 1) * CELL_SIZE.toFloat()
    val fouter: Float get():Float = (x + width) * CELL_SIZE.toFloat()
    val fx: Float get():Float = (x * CELL_SIZE).toFloat()
    val fy: Float get() = (y * CELL_SIZE).toFloat()
    val fbottom: Float get():Float = fy
    val fmiddle: Float get() = fy + CELL_SIZE
    val ftop: Float get() = fbottom + fheight
    val fwidth: Float get():Float = (width * CELL_SIZE).toFloat()
    val fheight: Float get():Float = (height * CELL_SIZE).toFloat()

    val isCommit: Boolean
    val kind: Kind
    val x: Int
    val y: Int
    val width: Int
    val height: Int
    val detail: FlowDetail
    val test: TestResult
    val tip: String
    val titleBlock: String
        get() = when (detail.type) {
            RunType.base64 -> "Encoding Error"
            RunType.run -> "Main Program Run"
            RunType.commit -> "Git Commit"
            RunType.local -> "Uncommitted Files"
            RunType.test -> "JUnit Run"
            RunType.error -> "Error"
            RunType.unknown -> "Unknown Entry"
        }

    companion object {
        val CELL_SIZE = 12

        fun makeBarShape(
            detail: FlowDetail,
            lastUpperRight: FlowPoint
        ): FlowShape {
            return BarShape(detail, lastUpperRight)
        }

        fun makeTestShape(
            detail: FlowDetail,
            x: Int,
            y: Int,
            test: TestResult,
            tests: List<TestResult>
        ): FlowShape {
            return TestShape(detail, x, y, test, tests)
        }
    }
}