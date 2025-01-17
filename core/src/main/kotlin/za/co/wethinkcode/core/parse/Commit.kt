package za.co.wethinkcode.core.parse


class Commit(val detail: LogDetail) : MutableSet<LogDetail> by LogDetailsByTimestamp() {
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

    operator fun get(index: Int): LogDetail = this.toList()[index]

    fun owns(run: LogDetail): Boolean =
        run.timestamp < detail.timestamp
                && (run.email.startsWith(detail.email) || detail.email.startsWith(run.email))
                && run.branch == detail.branch
}
