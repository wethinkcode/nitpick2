package za.co.wethinkcode.vnitpick

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import za.co.wethinkcode.core.flow.Base64Loader
import za.co.wethinkcode.core.flow.FlowCollater
import za.co.wethinkcode.core.flow.FlowShape
import za.co.wethinkcode.core.flow.ShapeDesigner
import za.co.wethinkcode.core.flow.YamlConverter
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
        val entries = YamlConverter().convert(yamls)
        val commits = FlowCollater().collate(entries)
        val layout = ShapeDesigner().design(commits)
        width.value = layout.width
        height.value = layout.height
        shapes.clear()
        shapes.addAll(layout.shapes)
    }

    fun flowClick(shape: FlowShape) {
        println("Clicked")
    }
}

const val FLOW_SIZE = 20f
