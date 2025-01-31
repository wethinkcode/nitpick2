package za.co.wethinkcode.core.flow

data class TestResult(
    val name: String,
    val status: TestStatus,
    val isNew: Boolean,
    val sequence: Int
) {
    val className: String = name.substringBefore('.')
    val testName: String = name.substringAfter('.')
}
