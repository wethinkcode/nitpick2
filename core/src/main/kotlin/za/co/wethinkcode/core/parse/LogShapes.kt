package za.co.wethinkcode.core.parse

class LogShapes(val height: Int, val width: Int) {
    val entries = mutableListOf<LogDetail>()
    val shapes = mutableListOf<LogShape>()
}