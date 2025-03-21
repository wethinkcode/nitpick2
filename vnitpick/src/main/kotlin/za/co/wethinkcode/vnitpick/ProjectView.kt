@file:OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)

package za.co.wethinkcode.vnitpick

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp

@Composable
fun ProjectView(model: ProjectsModel) {
    val project by model.currentProject
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        if (project == null) {
            AdviceBox(
                listOf(
                    "No current project.",
                    "Use the folder icon to navigate to a project folder."
                )
            )
        } else {
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
    Column(Modifier.fillMaxSize()) {
        FlowPage(project.flowModel)
    }
}

