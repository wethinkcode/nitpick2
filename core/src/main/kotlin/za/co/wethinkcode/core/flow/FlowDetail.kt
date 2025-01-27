package za.co.wethinkcode.core.flow


data class FlowDetail(
    val branch: String,
    val type: RunType,
    val timestamp: String,
    val committer: String,
    val email: String,
    val passes: List<String> = emptyList(),
    val fails: List<String> = emptyList(),
    val disables: List<String> = emptyList(),
    val aborts: List<String> = emptyList(),
    val messages: List<String> = emptyList()
) {
    val all = passes + fails + disables + aborts
    val total: Int get() = passes.size + fails.size + disables.size + aborts.size
    val isError: Boolean get() = messages.isNotEmpty()
    val summary: String
        get() {
            if (type == RunType.test) {
                return "test $total: ${passes.size}/${fails.size}/${disables.size}/${aborts.size}"
            }
            return type.name
        }
}

val NO_DETAIL = FlowDetail("N/A", RunType.unknown, "N/A", "N/A", "N/A")