@file:OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)

package za.co.wethinkcode.vnitpick

import androidx.compose.foundation.BasicTooltipBox
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberBasicTooltipState
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupPositionProviderAtPosition
import za.co.wethinkcode.core.flow.BarShape
import za.co.wethinkcode.core.flow.CommitShape
import za.co.wethinkcode.core.flow.RunType
import za.co.wethinkcode.core.flow.TestShape
import za.co.wethinkcode.core.flow.TestStatus
import za.co.wethinkcode.vnitpick.Styles.LARGE_FONT_SIZE

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
            Column(Modifier.fillMaxWidth(.80f).fillMaxHeight()) {
                Row(Modifier.fillMaxWidth()) {
                    Text(model.hover.value, fontSize = LARGE_FONT_SIZE, softWrap = false)
                }
                FlowGraph(model)
            }
            Spacer(Modifier.fillMaxHeight().width(1.dp))
            Column(Modifier.fillMaxWidth().fillMaxHeight().background(Color.LightGray)) {
                DetailView(model)
            }
        }
    }
}

@Composable
fun FlowGraph(model: FlowModel) {
    PanAndZoom {
        Column(
            Modifier.fillMaxSize().background(Color.White),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                Modifier.width((20 * model.width.value).dp).height((20 * model.height.value).dp)
                    .background(color = Color.LightGray)
            ) {
                model.shapes.forEach {
                    when (it) {
                        is CommitShape -> FlowCommit(
                            it,
                            model.height.value,
                            { flag -> model.hover(it, flag) }) {
                            model.flowClick(it)
                        }

                        is BarShape -> FlowBar(it, model.height.value, { flag -> model.hover(it, flag) }) {
                            model.flowClick(it)
                        }

                        is TestShape -> FlowTest(
                            it,
                            model.height.value,
                            { flag -> model.hover(it, flag) }) {
                            model.flowClick(it)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DetailView(model: FlowModel) {
    Column(Modifier.width(200.dp)) {
        Text("Here Ya Go")
    }
}

private val flowCommitShape = GenericShape { size, _ ->
    moveTo(0f, size.height)
    lineTo(0f, size.height - 20f)
    lineTo(size.width - 20f, size.height - 20f)
    lineTo(size.width - 20f, 0f)
    lineTo(size.width, 0f)
    lineTo(size.width, size.height)
}


@Composable
fun FlowCommit(shape: CommitShape, totalHeight: Int, onEnter: (Boolean) -> Unit, onClick: () -> Unit) {
    val offsetX = shape.x * 20
    val offsetY = (totalHeight - shape.height) * 20
    val width = shape.width * 20
    val height = shape.height * 20
    val background = if (shape.detail.type == RunType.local) LOCAL_BACKGROUND else COMMIT_BACKGROUND
    val clip = flowCommitShape
    FlowItem(offsetX, offsetY, clip, width, height, onEnter, onClick, background)
}

@Composable
fun FlowBar(shape: BarShape, totalHeight: Int, onEnter: (Boolean) -> Unit, onClick: () -> Unit) {
    val offsetX = shape.x * 20
    val offsetY = (totalHeight - (shape.height + 1)) * 20
    val width = shape.width * 20
    val height = shape.height * 20
    val background = Color.Gray
    val clip = RectangleShape
    FlowItem(offsetX, offsetY, clip, width, height, onEnter, onClick, background)
}

@Composable
fun FlowTest(shape: TestShape, totalHeight: Int, onEnter: (Boolean) -> Unit, onClick: () -> Unit) {
    val offsetX = shape.x * 20
    val offsetY = (totalHeight - ((shape.y + 1))) * 20
    val width = 20
    val height = 20
    val background = when (shape.result.status) {
        TestStatus.fail -> FAILED_BACKGROUND
        TestStatus.pass -> PASSED_BACKGROUND
        TestStatus.disable -> DISABLED_BACKGROUND
        TestStatus.abort -> ABORT_BACKGROUND
        TestStatus.unrun -> UNRUN_BACKGROUND
    }
    val clip = RectangleShape
    FlowItem(offsetX, offsetY, clip, width, height, onEnter, onClick, background)
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
        .background(background).border(1.dp, Color.Black, shape = clip))
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

val PASSED_BACKGROUND = Color(0, 255, 0)
val FAILED_BACKGROUND = Color(255, 0, 0)
val DISABLED_BACKGROUND = Color(255, 255, 0)
val ABORT_BACKGROUND = Color(255, 255, 255)
val UNRUN_BACKGROUND = Color(128, 128, 128)