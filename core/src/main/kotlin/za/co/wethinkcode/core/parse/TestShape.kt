package za.co.wethinkcode.core.parse


class TestShape(
    override val detail: LogDetail,
    override val x: Int,
    val y: Int,
    val result: TestResult,
    val tests: List<TestResult>
) : LogShape {
}