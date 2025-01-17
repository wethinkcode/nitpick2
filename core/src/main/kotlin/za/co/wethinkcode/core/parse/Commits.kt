package za.co.wethinkcode.core.parse

class Commits : MutableSet<Commit> by sortedSetOf(CommitComparator()) {
    class CommitComparator : Comparator<Commit> {
        override fun compare(p0: Commit?, p1: Commit?): Int {
            if (p0 == null || p1 == null) return 0
            return p0.detail.timestamp.compareTo(p1.detail.timestamp)
        }
    }

    operator fun get(index: Int): Commit = this.toList()[index]
}