package za.co.wethinkcode.core.flow

data class TestResult(
    val name: String,
    val status: TestStatus,
    val isNew: Boolean
) {
    val className: String = name.substringBefore('.')
    val testName: String = name.substringAfter('.')

    companion object {
        final val NONE = TestResult("No.Test", TestStatus.unrun, false)
    }
}
