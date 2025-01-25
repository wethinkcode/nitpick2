package za.co.wethinkcode.vnitpick

import androidx.compose.foundation.BasicTooltipBox
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberBasicTooltipState
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupPositionProviderAtPosition
import za.co.wethinkcode.core.parse.CommitShape

@Composable
fun ProjectView(model: ProjectsModel) {
    val project by model.currentProject
    Row {
        if (project == null) {
            Text(
                "No current project\n" + "To get started, use the folder icon to navigate to a project folder."
            )
        } else {
            ProjectPages(project!!)
            ProjectPage(project!!)
        }
    }
}

@Composable
fun ProjectPages(project: ProjectModel) {
    Column(
        Modifier.width(200.dp)
            .fillMaxHeight()
    ) {
        project.pages.forEach { page ->
            PageSelector(page) { project.pageTo(page) }
        }
    }
}

@Composable
fun PageSelector(page: ProjectPage, onClick: () -> Unit) {
    val isSelected by page.isSelected
    val isEnabled by page.isEnabled

    var modifier = Modifier
        .fillMaxWidth()
        .background(color = if (isEnabled) Color.DarkGray else Color.Gray)
        .padding(10.dp)
        .height(50.dp)
    if (isEnabled) modifier = modifier.clickable { onClick() }
    if (isSelected) {
        modifier = modifier.border(1.dp, Color.LightGray, RoundedCornerShape(3.dp))
    }

    Box(modifier, contentAlignment = Alignment.Center) {
        Text(
            page.name,
            color = if (page.isEnabled.value) Color.White else Color.Black,
            fontSize = TextUnit(24f, TextUnitType.Sp)
        )
    }
}

@Composable
fun ProjectPage(project: ProjectModel) {
    Column(Modifier.fillMaxWidth()) {
        when (project.page.value.type) {
            ProjectPageType.Process -> ProcessPage(project.flowModel)
            else -> {
                Text(project.path.toString())
                Text(project.page.value.name)
            }
        }
    }
}

@Composable
fun ProcessPage(model: FlowModel) {
    Box(Modifier.fillMaxSize().background(color = Color.LightGray)) {
        Box(
            Modifier.width((20 * model.width.value).dp)
                .height((20 * model.height.value).dp)
                .background(color = Color.Yellow)
        ) {
            model.shapes.forEach {
                when (it) {
                    is CommitShape -> DrawCommitShape(it, model.height.value)
                }
            }
        }
    }
}

private val TempCommitShape = GenericShape { size, _ ->
    moveTo(0f, size.height)
    lineTo(0f, size.height - 20f)
    lineTo(size.width - 20f, size.height - 20f)
    lineTo(size.width - 20f, 0f)
    lineTo(size.width, 0f)
    lineTo(size.width, size.height)
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun DrawCommitShape(shape: CommitShape, totalHeight: Int) {
    val tooltipState = rememberBasicTooltipState(isPersistent = true)
    val offsetX = shape.x * 20
    val offsetY = (totalHeight - shape.height) * 20
    println("$offsetX,$offsetY")
    println("${shape.x} ${shape.height}")
    println("${shape.width},${shape.height}")
    BasicTooltipBox(
        modifier = Modifier.offset(offsetX.dp, offsetY.dp)
            .clip(TempCommitShape),
        positionProvider = PopupPositionProviderAtPosition(
            Offset(0f, 0f),
            true,
            Offset(0f, 0f),
            windowMarginPx = 5
        ),
        tooltip = {
            Text("${shape.detail.timestamp}")
        },
        state = tooltipState
    ) {
        Box(
            modifier = Modifier.width((shape.width * 20).dp)
                .height((shape.height * 20).dp)
//                .offset(offsetX.dp, offsetY.dp)
                .clip(TempCommitShape)
                .clickable {
                    println("Clicked")
                }
                .background(Color.Red)
                .border(1.dp, Color.Blue, shape = TempCommitShape)
        )
    }
}