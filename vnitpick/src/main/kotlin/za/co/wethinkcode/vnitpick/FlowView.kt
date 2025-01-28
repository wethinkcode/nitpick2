@file:OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)

package za.co.wethinkcode.vnitpick

import androidx.compose.foundation.BasicTooltipBox
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberBasicTooltipState
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupPositionProviderAtPosition
import za.co.wethinkcode.core.flow.BarShape
import za.co.wethinkcode.core.flow.CommitShape
import za.co.wethinkcode.core.flow.RunType
import za.co.wethinkcode.core.flow.TestShape
import za.co.wethinkcode.core.flow.TestStatus

@Composable
fun FlowPage(model: FlowModel) {
    Box(Modifier.fillMaxSize().background(color = Color.LightGray)) {
        if (model.isJltk.value) {
            Box(
                Modifier.width((20 * model.width.value).dp).height((20 * model.height.value).dp)
                    .background(color = Color.LightGray)
            ) {
                model.shapes.forEach {
                    when (it) {
                        is CommitShape -> FlowCommit(it, model.height.value) {
                            model.flowClick(it)
                        }

                        is BarShape -> FlowBar(it, model.height.value) {
                            model.flowClick(it)
                        }

                        is TestShape -> FlowTest(it, model.height.value) {
                            model.flowClick(it)
                        }
                    }
                }
            }
        } else {
            AdviceBox(
                listOf(
                    "This is not a jltk project.\n", "Please open a jltk project."
                )
            )
        }
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
fun FlowCommit(shape: CommitShape, totalHeight: Int, onClick: () -> Unit) {
    val offsetX = shape.x * 20
    val offsetY = (totalHeight - shape.height) * 20
    val background = if (shape.detail.type == RunType.local) LOCAL_BACKGROUND else COMMIT_BACKGROUND
    FlowTip(offsetX, offsetY, true, "Commit: ${shape.detail.timestamp}") {
        Box(
            modifier = Modifier.width((shape.width * 20).dp).height((shape.height * 20).dp).clip(flowCommitShape)
                .clickable {
                    onClick()
                }.background(background).border(1.dp, Color.Black, shape = flowCommitShape)
        )
    }
}

@Composable
fun FlowBar(shape: BarShape, totalHeight: Int, onClick: () -> Unit) {
    val offsetX = shape.x * 20
    val offsetY = (totalHeight - (shape.height + 1)) * 20
    FlowTip(offsetX, offsetY, false, "Run: ${shape.detail.timestamp}") {
        Box(modifier = Modifier.width((shape.width * 20).dp).height((shape.height * 20).dp).clickable {
            onClick()
        }.background(Color.Gray).border(1.dp, Color.Black, shape = RectangleShape))
    }
}

@Composable
fun FlowTest(shape: TestShape, totalHeight: Int, onClick: () -> Unit) {
    val offsetX = shape.x * 20
    val offsetY = (totalHeight - ((shape.y + 1))) * 20
    val background = when (shape.result.status) {
        TestStatus.fail -> FAILED_BACKGROUND
        TestStatus.pass -> PASSED_BACKGROUND
        TestStatus.disable -> DISABLED_BACKGROUND
        TestStatus.abort -> ABORT_BACKGROUND
        TestStatus.unrun -> UNRUN_BACKGROUND
    }
    val tip = shape.result.name
    FlowTip(offsetX, offsetY, false, tip) {
        Box(modifier = Modifier.width(20.dp).height(20.dp).clickable {
            onClick()
        }.background(background).border(1.dp, Color.Black, shape = RectangleShape))
    }
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