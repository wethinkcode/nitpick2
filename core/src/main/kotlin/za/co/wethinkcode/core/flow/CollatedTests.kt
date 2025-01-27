package za.co.wethinkcode.core.flow

class CollatedTests {

    private val current = sortedSetOf(SequenceComparator())

    private class SequenceComparator : Comparator<TestResult> {
        override fun compare(first: TestResult?, second: TestResult?): Int {
            if (first == null || second == null) return 0
            return first.sequence.compareTo(second.sequence)
        }
    }

    fun contains(name: String): Boolean = current.any { it.name == name }

    private val old = mutableListOf<String>()
    private var isFirstRun = true

    fun add(name: String, result: TestStatus) {
        current.add(TestResult(name, result, determineNewness(name), determineSequence(name)))
        old.add(name)
    }

    operator fun get(index: Int): TestResult = oldUnrunTestsAdded()[index]

    fun toList(): List<TestResult> = oldUnrunTestsAdded()

    fun add(passes: List<String>, fails: List<String>, disables: List<String>, aborts: List<String>) {
        passes.forEach { add(it, TestStatus.pass) }
        fails.forEach { add(it, TestStatus.fail) }
        disables.forEach { add(it, TestStatus.disable) }
        aborts.forEach { add(it, TestStatus.abort) }
    }

    fun endRun() {
        current.clear()
        isFirstRun = false
    }

    private fun determineSequence(name: String): Int {
        old.indices.forEach {
            if (old[it] == name) return it
        }
        return old.size
    }


    private fun oldUnrunTestsAdded(): List<TestResult> {
        old.indices.forEach {
            val name = old[it]
            if (!contains(name)) {
                current.add(TestResult(name, TestStatus.unrun, false, it))
            }
        }
        return current.toList()
    }

    private fun determineNewness(name: String): Boolean {
        if (isFirstRun) return false
        return !old.contains(name)
    }
}