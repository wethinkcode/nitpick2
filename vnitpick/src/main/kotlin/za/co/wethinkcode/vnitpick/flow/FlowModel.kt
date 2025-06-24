package za.co.wethinkcode.vnitpick.flow

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import dev.vishna.watchservice.KWatchEvent
import dev.vishna.watchservice.asWatchChannel
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.consumeEach
import za.co.wethinkcode.core.flow.FlowDetailLoader
import za.co.wethinkcode.core.flow.FlowShape
import za.co.wethinkcode.flow.FileHelpers.*
import java.nio.file.Path
import kotlin.io.path.exists

class FlowModel(path: Path) {

    val path: Path

    val width = mutableStateOf(0)
    val height = mutableStateOf(0)
    val shapes = mutableStateListOf<FlowShape>()
    val isJltk = mutableStateOf(false)
    val hover = mutableStateOf("")
    val current = mutableStateOf<FlowShape?>(null)
    val cellSize = mutableStateOf(20)
    val watchChannel = path.resolve(FLOW_FOLDER).toFile().asWatchChannel()

    val scale = mutableStateOf(2f)
    val offset = mutableStateOf(Offset(0f, 0f))
    val pivot = mutableStateOf(Offset(0f, 0f))

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
        commits.layoutToShapes(shapes)
        width.value = commits.width
        height.value = commits.height
    }

    fun zoomIn() {
        scale.value *= 1.1f
    }

    fun zoomOut() {
        scale.value *= .9f
    }

    fun hover(it: FlowShape, isHovered: Boolean) {
        if (isHovered) hover.value = it.tip
        else hover.value = ""
    }

    fun close() {
        watchChannel.close()
    }

    fun pan(pan: Offset) {
        println("pan $this")
        offset.value += pan
    }
}

const val FLOW_SIZE = 20f
