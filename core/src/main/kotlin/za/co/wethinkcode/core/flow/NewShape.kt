package za.co.wethinkcode.core.flow

data class NewShape(
    val at: FlowPoint,
    val detail: FlowDetail,
    val tip: String,
    val tests: List<TestResult> = emptyList(),
    val test: TestResult = TestResult("No.Test", TestStatus.unrun, false)
)
