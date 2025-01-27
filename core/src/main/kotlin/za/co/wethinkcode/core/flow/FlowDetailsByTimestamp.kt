package za.co.wethinkcode.core.flow

class FlowDetailsByTimestamp : MutableSet<FlowDetail> by sortedSetOf(RunComparator()) {
    class RunComparator : Comparator<FlowDetail> {
        override fun compare(first: FlowDetail?, second: FlowDetail?): Int {
            if (first == null || second == null) return 0
            return first.timestamp.compareTo(second.timestamp)
        }
    }
}