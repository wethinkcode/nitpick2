package za.co.wethinkcode.vnitpick

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowLeft
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.CenterFocusWeak
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.material.icons.filled.ZoomOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp

@Composable
fun PanAndZoom(content: @Composable () -> Unit) {
    var scale by remember { mutableStateOf(1f) }
    var rotation by remember { mutableStateOf(0f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    val state = rememberTransformableState { zoomChange, offsetChange, rotationChange ->
        scale *= zoomChange
        rotation += rotationChange
        offset += offsetChange
    }
    Row(Modifier.fillMaxSize()) {
        Column(Modifier.fillMaxHeight().weight(1f)) {
            Surface(
                Modifier
                    // apply other transformations like rotation and zoom
                    // on the pizza slice emoji
                    .clip(RectangleShape)
                    .graphicsLayer(
                        scaleX = scale,
                        scaleY = scale,
                        rotationZ = rotation,
                        translationX = offset.x,
                        translationY = offset.y
                    )
                    // add transformable to listen to multitouch transformation events
                    // after offset
                    .transformable(state = state)
                    .fillMaxSize()
                    .pointerInput(PointerEventType.Scroll) {
                        awaitPointerEventScope {
                            while (true) {
                                val event = awaitPointerEvent()
                                val direction = event.changes.first().scrollDelta.y
                                if (direction == 1.0f) {
                                    scale = scale * 0.9f
                                }
                                if (direction == -1.0f) {
                                    scale = scale * 1.1f
                                }
                            }
                        }
                    }
            ) {
                content()
            }
        }
        Column(Modifier.background(Color.LightGray)) {
            FlowGraphIcon(Icons.Filled.ZoomIn, "Zoom In") { scale *= 1.1f }
            FlowGraphIcon(Icons.Filled.ZoomOut, "Zoom Out") { scale *= .9f }
            FlowGraphIcon(Icons.Filled.CenterFocusWeak, "Center") { }
            FlowGraphIcon(Icons.AutoMirrored.Filled.ArrowRight, "Right") { offset = Offset(offset.x + 15f, offset.y) }
            FlowGraphIcon(Icons.AutoMirrored.Filled.ArrowLeft, "Left") { offset = Offset(offset.x - 15f, offset.y) }
            FlowGraphIcon(Icons.Filled.ArrowUpward, "Up") { offset = Offset(offset.x, offset.y - 15f) }
            FlowGraphIcon(Icons.Filled.ArrowDownward, "Down") { offset = Offset(offset.x, offset.y + 15f) }
        }
    }
}

@Composable
fun FlowGraphIcon(imageVector: ImageVector, description: String, onClick: () -> Unit) {
    Icon(imageVector,
        description,
        Modifier.clickable { onClick() }
            .size(28.dp)
    )

}