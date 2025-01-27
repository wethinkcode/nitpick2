package za.co.wethinkcode.core.flow

class Commits : MutableSet<Commit> by sortedSetOf(CommitComparator()) {
    val collatedTests = CollatedTests()

    val height get() = maxOf { it.height } + 1
    val width get() = sumOf { it.width }

    class CommitComparator : Comparator<Commit> {
        override fun compare(p0: Commit?, p1: Commit?): Int {
            if (p0 == null || p1 == null) return 0
            return p0.detail.timestamp.compareTo(p1.detail.timestamp)
        }
    }

    operator fun get(index: Int): Commit = this.toList()[index]

    fun layoutToShapes(shapes: MutableList<FlowShape>) {
        var previousUpperRight = FlowPoint(0, 0)
        forEach { commit ->
            previousUpperRight = commit.layoutToShapes(previousUpperRight, shapes, collatedTests)
        }
    }
}