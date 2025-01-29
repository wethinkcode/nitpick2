package za.co.wethinkcode.core.flow


class TestShape(
    override val detail: FlowDetail,
    override val x: Int,
    val y: Int,
    val result: TestResult
) : FlowShape {
    override val tip: String = "${detail.type.name}: ${result.name}"

}