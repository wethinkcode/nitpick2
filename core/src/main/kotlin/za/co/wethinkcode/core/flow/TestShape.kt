package za.co.wethinkcode.core.flow


class TestShape(
    override val detail: FlowDetail,
    override val x: Int,
    val y: Int,
    val result: TestResult,
    val tests: List<TestResult>
) : FlowShape {
}