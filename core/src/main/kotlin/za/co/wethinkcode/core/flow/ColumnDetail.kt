package za.co.wethinkcode.core.flow


data class ColumnDetail(val detail: FlowDetail, val tests: List<TestResult> = emptyList())