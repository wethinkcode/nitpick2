package za.co.wethinkcode.core.parse


class Commit(val detail: LogDetail) : MutableSet<LogDetail> by LogDetailsByTimestamp() {

    operator fun get(index: Int): LogDetail = this.toList()[index]

    fun owns(run: LogDetail): Boolean =
        run.timestamp < detail.timestamp
                && (run.email.startsWith(detail.email) || detail.email.startsWith(run.email))
                && run.branch == detail.branch

    fun toFlowGrid(grid: FlowGrid) {
        this.forEach { detail ->
            val destX = grid.width
            when (detail.type) {
                RunType.run -> {
                    grid[destX, 1] = FlowCell(detail, CellType.RunBar)
                    grid[destX, 0] = FlowCell(detail, CellType.LocalLeft)
                }

                RunType.local -> {
                    grid[destX, 1] = FlowCell(detail, CellType.LocalBar)
                    grid[destX, 0] = FlowCell(detail, CellType.LocalRight)
                }

                else -> {
                    grid[destX, 1] = FlowCell(detail, CellType.Nothing)
                    grid[destX, 0] = FlowCell(detail, CellType.Nothing)
                }
            }
        }
        val destX = grid.width
        grid[destX, 1] = FlowCell(detail, CellType.LocalBar)
        grid[destX, 0] = FlowCell(detail, CellType.LocalRight)
    }
}
