@file:OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)

package za.co.wethinkcode.vnitpick

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.*
import za.co.wethinkcode.vnitpick.flow.FlowPage
import za.co.wethinkcode.vnitpick.projects.ProjectsModel

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

