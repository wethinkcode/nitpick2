package za.co.wethinkcode.core.flow

class CollatedTests {

    private val results = LinkedHashMap<String, TestResult>()

    fun begin() {
        results.keys.forEach { key ->
            results.put(key, TestResult(key, TestStatus.unrun, false, 0))
        }
    }

    fun add(name: String, result: TestStatus) {
        val isNew = !results.containsKey(name)
        results.put(name, TestResult(name, result, isNew, 0))
    }

    fun toList(): List<TestResult> = results.values.toList()

    fun add(passes: List<String>, fails: List<String>, disables: List<String>, aborts: List<String>) {
        passes.forEach { add(it, TestStatus.pass) }
        fails.forEach { add(it, TestStatus.fail) }
        disables.forEach { add(it, TestStatus.disable) }
        aborts.forEach { add(it, TestStatus.abort) }
    }
}