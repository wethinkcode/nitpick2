package za.co.wethinkcode.core.flow

data class TestResult(val name: String, val status: TestStatus, val isNew: Boolean, val sequence: Int) {
}
