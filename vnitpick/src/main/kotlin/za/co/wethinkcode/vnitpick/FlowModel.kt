package za.co.wethinkcode.vnitpick

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import za.co.wethinkcode.core.flow.FlowDetailLoader
import za.co.wethinkcode.core.flow.FlowShape
import java.nio.file.Path

class FlowModel {

    val width = mutableStateOf(0)
    val height = mutableStateOf(0)
    val shapes = mutableStateListOf<FlowShape>()

    fun load(path: Path) {
        val commits = FlowDetailLoader().load(path.resolve(".jltk"))
        shapes.clear()
        commits.layoutToShapes(shapes)
        width.value = commits.width
        height.value = commits.height
    }

    fun flowClick(shape: FlowShape) {
        println("Clicked")
    }
}

const val FLOW_SIZE = 20f
