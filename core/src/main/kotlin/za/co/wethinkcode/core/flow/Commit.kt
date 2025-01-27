package za.co.wethinkcode.core.flow


class Commit(val detail: FlowDetail) : MutableSet<FlowDetail> by FlowDetailsByTimestamp() {

    val height: Int
        get() {
            if (isEmpty()) return 2
            else return maxOf { entry ->
                when (entry.type) {
                    RunType.test -> entry.total
                    else -> 1
                }
            } + 1
        }

    val width: Int
        get() = Math.max(size + 1, 2)

    operator fun get(index: Int): FlowDetail = this.toList()[index]

    fun owns(run: FlowDetail): Boolean =
        run.timestamp < detail.timestamp
                && (run.email.startsWith(detail.email) || detail.email.startsWith(run.email))
                && run.branch == detail.branch
}
