package za.co.wethinkcode.core.flow

class LogShapes(val height: Int, val width: Int) {
    val entries = mutableListOf<FlowDetail>()
    val shapes = mutableListOf<FlowShape>()
}