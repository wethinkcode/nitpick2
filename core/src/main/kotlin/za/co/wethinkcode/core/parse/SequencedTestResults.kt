package za.co.wethinkcode.core.parse

class SequencedTestResults {

    private val results = sortedSetOf(SequenceComparator())

    private class SequenceComparator : Comparator<TestResult> {
        override fun compare(first: TestResult?, second: TestResult?): Int {
            if (first == null || second == null) return 0
            return first.sequence.compareTo(second.sequence)
        }
    }

    fun contains(name: String): Boolean = results.filter { it.name == name }.isNotEmpty()

    fun add(result: TestResult) = results.add(result)
    fun clear() = results.clear()
    fun toList(): List<TestResult> = results.toList()
}