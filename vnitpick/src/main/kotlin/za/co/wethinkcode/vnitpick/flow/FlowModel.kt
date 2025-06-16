package za.co.wethinkcode.vnitpick.flow

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import dev.vishna.watchservice.KWatchEvent
import dev.vishna.watchservice.asWatchChannel
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.consumeEach
import za.co.wethinkcode.core.flow.*
import za.co.wethinkcode.flow.FileHelpers.*
import java.nio.file.Path
import kotlin.io.path.exists
import kotlin.math.max

class FlowModel(path: Path) {

    val path: Path

    val width = mutableStateOf(0)
    val height = mutableStateOf(0)
    val shapes = mutableStateListOf<FlowShape>()
    val newShapes = mutableStateListOf<NewShape>()
    val isJltk = mutableStateOf(false)
    val hover = mutableStateOf("")
    val current = mutableStateOf<FlowShape?>(null)
    val cellSize = mutableStateOf(20)
    val watchChannel = path.resolve(FLOW_FOLDER).toFile().asWatchChannel()

    init {
        this.path = path
        isJltk.value = isPathJltk(path)
        if (isJltk.value) {
            CoroutineScope(Dispatchers.IO).launch {
                watchChannel.consumeEach { event ->
                    println(event.toString())
                    if (event.kind == KWatchEvent.Kind.Modified
                        && (
                                event.file.name.endsWith(FLOW_TMP_SUFFIX)
                                        || event.file.name.endsWith(FLOW_LOG_SUFFIX)
                                )
                    ) {
                        withContext(Dispatchers.Main) {
                            load()
                        }
                    }
                }

            }
        }
    }

    private fun isPathJltk(path: Path): Boolean {
        if (!path.resolve(FLOW_FOLDER).exists()) return false
        load()
        return true
    }

    fun load() {
        val commits = FlowDetailLoader().load(path.resolve(FLOW_FOLDER))
        shapes.clear()
        commits.layoutToShapes(shapes, newShapes)
        width.value = commits.width
        height.value = commits.height
    }

    fun flowClick(shape: FlowShape) {
        current.value = shape
    }

    fun flowClick(shape: NewShape) {
    }

    fun zoomIn() {
        val new = cellSize.value * .9f
        cellSize.value = max(new.toInt(), 5)
    }

    fun zoomOut() {
        val new = cellSize.value * 1.1f
        cellSize.value = new.toInt()
    }

    fun hover(it: FlowShape, isHovered: Boolean) {
        if (isHovered) hover.value = it.tip
        else hover.value = ""
    }

    fun hover(it: NewShape, isHovered: Boolean) {
        if (isHovered) hover.value = it.tip
        else hover.value = ""
    }

    fun close() {
        watchChannel.close()
    }
}

const val FLOW_SIZE = 20f
