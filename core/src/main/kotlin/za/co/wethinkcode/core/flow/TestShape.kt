package za.co.wethinkcode.core.flow


class TestShape(
    override val detail: FlowDetail,
    override val x: Int,
    override val y: Int,
    override val test: TestResult,
    val tests: List<TestResult>
) : FlowShape {
    override val isCommit: Boolean = false

    override val tip: String = "${detail.type.name}: ${test.name}"
    override val width = 1
    override val height = 1
    override val kind: FlowShape.Kind
        get() = when (test.status) {
            TestStatus.pass -> FlowShape.Kind.passed
            TestStatus.fail -> FlowShape.Kind.failed
            TestStatus.disable -> FlowShape.Kind.disabled
            TestStatus.abort -> FlowShape.Kind.aborted
            TestStatus.unrun -> FlowShape.Kind.unrun
        }

}