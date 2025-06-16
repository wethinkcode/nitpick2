@file:OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)

package za.co.wethinkcode.vnitpick.flow

import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupPositionProviderAtPosition
import za.co.wethinkcode.core.flow.*
import za.co.wethinkcode.vnitpick.AdviceBox
import za.co.wethinkcode.vnitpick.Styles.DEFAULT_FONT_SIZE
import za.co.wethinkcode.vnitpick.Styles.LARGE_FONT_SIZE
import kotlin.math.sign

@Composable
fun FlowPage(model: FlowModel) {
    if (model.isJltk.value == false) {
        AdviceBox(
            listOf(
                "This is not a jltk project.\n", "Please open a jltk project."
            )
        )
    } else {
        Row(Modifier.fillMaxWidth()) {
            Column(Modifier.fillMaxWidth(.75f).fillMaxHeight().clip(RectangleShape)) {
                Row(Modifier.fillMaxWidth()) {
                    Text(model.hover.value, fontSize = LARGE_FONT_SIZE, softWrap = false)
                }
                Row(Modifier.clip(RectangleShape)) {
                    FlowGraph(model)
                }
            }
            Spacer(Modifier.fillMaxHeight().width(1.dp))
            Column(Modifier.fillMaxWidth().fillMaxHeight().background(Color.LightGray)) {
                DetailView(model.current.value)
            }
        }
    }
}

@Composable
fun FlowGraph(model: FlowModel) {
    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray)
            .onPointerEvent(PointerEventType.Scroll) {
                val change = it.changes.first()
                val delta = change.scrollDelta.y.toInt().sign
                if (delta < 0) scale *= 0.9f
                else scale *= 1.1f
            }
            .pointerInput(Unit) {
                detectTransformGestures { centroid, pan, zoom, _ ->
                    println(zoom)
                    offset += (centroid - offset) * (1f - zoom) + pan
                }
                awaitPointerEventScope {
                    while (true) {
                        println("*")
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
        withTransform(
            {
                scale(scale, scale)
                translate(offset.x, offset.y)
            }
        ) {
            drawLine(
                color = Color.Black,
                start = Offset(0f, 0f),
                end = Offset(500f, 0f),
            )
            drawRect(
                topLeft = Offset(20f, 0f),
                color = Color.Magenta,
                size = Size(20f, 1000f)
            )
            drawRect(
                topLeft = Offset(40f, 700f),
                color = Color.Blue,
                size = Size(20f, 100f)
            )
        }
    }
}

fun backgroundFromShape(shape: NewShape): Color {
    return when (shape.detail.type) {
        RunType.commit -> COMMIT_BACKGROUND
        RunType.run -> RUN_BACKGROUND
        RunType.test -> determineTestColor(shape.test)
        else -> Color.Yellow
    }
}

@Composable
fun DetailView(shape: FlowShape?) {
    Column(Modifier.fillMaxHeight().padding(10.dp)) {
        if (shape != null) {
            Text(shape.titleBlock, fontSize = LARGE_FONT_SIZE)
            CommitBlock(shape)
            if (shape is TestShape) TestBlock(shape)
        }
    }
}

@Composable
fun Base64Error() {
    Column(Modifier.background(Color.Yellow)) {
        Text("Encoding Error", fontSize = LARGE_FONT_SIZE)
        Text("This is a corrupted log entry.")
    }
}

@Composable
fun TestBlock(shape: TestShape) {
    if (shape.detail.type == RunType.test) {
        LazyColumn(Modifier.fillMaxWidth().padding(0.dp, CELL_SIZE.dp)) {
            items(shape.tests) { test ->
                val textColor = determineTestText(test)
                val textBackground = determineTestColor(test)
                Column(
                    Modifier.fillMaxWidth()
                        .background(textBackground)
                ) {
                    Text(test.className, color = textColor)
                    Text(
                        test.testName,
                        color = textColor,
                        fontSize = LARGE_FONT_SIZE,
                    )
                    Spacer(Modifier.height(1.dp).fillMaxWidth().background(Color.Black))
                }
            }
        }
    }
}

fun determineTestColor(test: TestResult): Color {
    return when (test.status) {
        TestStatus.fail -> FAILED_BACKGROUND
        TestStatus.pass -> PASSED_BACKGROUND
        TestStatus.disable -> DISABLED_BACKGROUND
        TestStatus.abort -> ABORT_BACKGROUND
        TestStatus.unrun -> UNRUN_BACKGROUND
    }
}

fun determineTestText(test: TestResult): Color {
    if (test.isNew) return Color.White
    else return Color.Black
}

@Composable
fun CommitBlock(shape: FlowShape) {
    Column(
        Modifier.fillMaxWidth()
            .padding(5.dp),
        verticalArrangement = Arrangement.Center
    ) {
        if (shape.detail.type == RunType.local) {
            Text("Uncommitted Files", fontSize = DEFAULT_FONT_SIZE)
        } else {
            Text(shape.detail.timestamp, fontSize = DEFAULT_FONT_SIZE)
        }
        Text(shape.detail.branch, fontSize = DEFAULT_FONT_SIZE)
        Text(shape.detail.email, fontSize = DEFAULT_FONT_SIZE)
        Text(shape.detail.committer, fontSize = DEFAULT_FONT_SIZE)
    }

}

private val flowCommitShape = GenericShape { size, _ ->
    moveTo(0f, size.height)
    lineTo(0f, size.height - CELL_SIZE.toFloat())
    lineTo(size.width - CELL_SIZE.toFloat(), size.height - CELL_SIZE.toFloat())
    lineTo(size.width - CELL_SIZE.toFloat(), 0f)
    lineTo(size.width, 0f)
    lineTo(size.width, size.height)
}


@Composable
fun FlowCommit(shape: CommitShape, totalHeight: Int, onEnter: (Boolean) -> Unit, onClick: () -> Unit) {
    val offsetX = shape.x * CELL_SIZE
    val offsetY = (totalHeight - shape.height) * CELL_SIZE
    val width = shape.width * CELL_SIZE
    val height = shape.height * CELL_SIZE
    val background = if (shape.detail.type == RunType.local) LOCAL_BACKGROUND else COMMIT_BACKGROUND
    val clip = flowCommitShape
    FlowItem(offsetX, offsetY, clip, width, height, onEnter, onClick, background)
}

@Composable
fun FlowBar(shape: BarShape, totalHeight: Int, onEnter: (Boolean) -> Unit, onClick: () -> Unit) {
    val offsetX = shape.x * CELL_SIZE
    val offsetY = (totalHeight - (shape.height + 1)) * CELL_SIZE
    val width = shape.width * CELL_SIZE
    val height = shape.height * CELL_SIZE
    val background = Color.Gray
    val clip = RectangleShape
    FlowItem(offsetX, offsetY, clip, width, height, onEnter, onClick, background)
}

@Composable
fun FlowTest(shape: TestShape, totalHeight: Int, onEnter: (Boolean) -> Unit, onClick: () -> Unit) {
    val offsetX = shape.x * CELL_SIZE
    val offsetY = (totalHeight - ((shape.y + 1))) * CELL_SIZE
    val width = CELL_SIZE
    val height = CELL_SIZE
    val background = determineTestColor(shape.result)
    val clip = RectangleShape
    FlowItem(offsetX, offsetY, clip, width, height, onEnter, onClick, background)
}

@Composable
fun FlowBox(
    offsetX: Int,
    offsetY: Int,
    clip: Shape,
    width: Int,
    height: Int,
    onEnter: (Boolean) -> Unit,
    onClick: () -> Unit,
    background: Color
) {
    Box(modifier = Modifier.offset(offsetX.dp, offsetY.dp).clip(clip)
        .width(width.dp)
        .height(height.dp)
        .pointerInput(PointerEventType.Enter) {
            awaitPointerEventScope {
                while (true) {
                    val event = awaitPointerEvent()
                    onEnter(true)
                }
            }
        }
        .pointerInput(PointerEventType.Exit) {
            awaitPointerEventScope {
                while (true) {
                    val event = awaitPointerEvent()
                    onEnter(false)
                }
            }
        }
        .clickable { onClick() }
        .background(background)
//        .border(1.dp, Color.Black, shape = clip)
    )
}


@Composable
fun FlowItem(
    offsetX: Int,
    offsetY: Int,
    clip: Shape,
    width: Int,
    height: Int,
    onEnter: (Boolean) -> Unit,
    onClick: () -> Unit,
    background: Color
) {
    Box(modifier = Modifier.offset(offsetX.dp, offsetY.dp).clip(clip)
        .width(width.dp)
        .height(height.dp)
        .pointerInput(PointerEventType.Enter) {
            awaitPointerEventScope {
                while (true) {
                    val event = awaitPointerEvent()
                    onEnter(true)
                }
            }
        }
        .pointerInput(PointerEventType.Exit) {
            awaitPointerEventScope {
                while (true) {
                    val event = awaitPointerEvent()
                    onEnter(false)
                }
            }
        }
        .clickable { onClick() }
        .background(background)
//        .border(1.dp, Color.Black, shape = clip)
    )
}


@Composable
fun FlowTip(offsetX: Int, offsetY: Int, isEll: Boolean, tip: String, content: @Composable () -> Unit) {
    val tooltipState = rememberBasicTooltipState(isPersistent = true)
    val modifier = if (isEll) Modifier.offset(offsetX.dp, offsetY.dp).clip(flowCommitShape)
    else Modifier.offset(offsetX.dp, offsetY.dp)
    BasicTooltipBox(
        modifier = modifier, positionProvider = PopupPositionProviderAtPosition(
            Offset(0f, 0f), true, Offset(0f, 0f), windowMarginPx = 5
        ), tooltip = {
            Text(tip)
        }, state = tooltipState
    ) { content() }
}

val COMMIT_BACKGROUND = Color(0xDA, 0x70, 0xD6)
val LOCAL_BACKGROUND = Color(0xFF, 0x7F, 0x50)
val RUN_BACKGROUND = Color(0x45, 0x4f, 0x4f)

val PASSED_BACKGROUND = Color(0, 255, 0)
val FAILED_BACKGROUND = Color(255, 0, 0)
val DISABLED_BACKGROUND = Color(255, 255, 0)
val ABORT_BACKGROUND = Color(255, 255, 255)
val UNRUN_BACKGROUND = Color(128, 128, 128)
val CELL_SIZE = 12