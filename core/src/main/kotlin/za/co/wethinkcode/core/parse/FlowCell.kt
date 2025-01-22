package za.co.wethinkcode.core.parse

data class FlowCell(val detail: LogDetail, val type: CellType) {
}

val NO_CELL = FlowCell(
    NO_DETAIL,
    CellType.Nothing
)

