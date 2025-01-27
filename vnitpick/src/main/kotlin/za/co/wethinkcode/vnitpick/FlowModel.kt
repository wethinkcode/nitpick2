package za.co.wethinkcode.vnitpick

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import za.co.wethinkcode.core.flow.Base64Loader
import za.co.wethinkcode.core.flow.FlowCollater
import za.co.wethinkcode.core.flow.FlowShape
import za.co.wethinkcode.core.flow.StringToDetail
import java.nio.file.Path

class FlowModel {

    val raw = mutableStateListOf("")
    val width = mutableStateOf(0)
    val height = mutableStateOf(0)
    val shapes = mutableStateListOf<FlowShape>()

    fun load(path: Path) {
        val yamls = Base64Loader().load(path.resolve(".jltk"))
        raw.clear()
        yamls.forEach { yaml -> raw.add(yaml) }
        val entries = StringToDetail().convert(yamls)
        val commits = FlowCollater().collate(entries)
        val layout = commits.layoutToShapes()
        width.value = commits.width
        height.value = commits.height
        shapes.clear()
        shapes.addAll(layout.shapes)
    }

    fun flowClick(shape: FlowShape) {
        println("Clicked")
    }
}

const val FLOW_SIZE = 20f
