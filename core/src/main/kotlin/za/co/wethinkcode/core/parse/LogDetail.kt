package za.co.wethinkcode.core.parse


data class LogDetail(
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