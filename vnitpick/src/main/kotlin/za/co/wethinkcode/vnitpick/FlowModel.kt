package za.co.wethinkcode.vnitpick

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import za.co.wethinkcode.core.flow.FlowDetailLoader
import za.co.wethinkcode.core.flow.FlowShape
import java.nio.file.Path
import kotlin.io.path.exists

class FlowModel(path: Path) {

    val path: Path

    val width = mutableStateOf(0)
    val height = mutableStateOf(0)
    val shapes = mutableStateListOf<FlowShape>()
    val isJltk = mutableStateOf(false)

    init {
        this.path = path
        isJltk.value = isPathJltk(path)
    }

    private fun isPathJltk(path: Path): Boolean {
        if (!path.resolve(".jltk").exists()) return false
        load()
        return true
    }

    fun load() {
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
