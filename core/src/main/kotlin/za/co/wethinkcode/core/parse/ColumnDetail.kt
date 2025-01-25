package za.co.wethinkcode.core.parse


data class ColumnDetail(val detail: LogDetail, val tests: List<TestResult> = emptyList())