package za.co.wethinkcode.vnitpick

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp

@Composable
fun ProjectView(model: ProjectsModel) {
    val project by model.currentProject
    Row {
        if (project == null) {
            Text(
                "No current project\n" + "To get started, use the folder icon to navigate to a project folder."
            )
        } else {
            Row(Modifier.fillMaxSize()) {
                ProjectPages(project!!)
                ProjectPage(project!!)
            }
        }
    }
}

@Composable
fun ProjectPages(project: Project) {
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

    Row(modifier, horizontalArrangement = Arrangement.Center) {
        Text(
            page.name,
            color = if (page.isEnabled.value) Color.White else Color.Black,
            fontSize = TextUnit(20f, TextUnitType.Sp)
        )
        if (isSelected) {
            Row(Modifier.width(50.dp), verticalAlignment = Alignment.CenterVertically) {
                Text(
                    " > ",
                    color = Color.White,
                    fontSize = TextUnit(20f, TextUnitType.Sp)
                )
            }
        } else Spacer(Modifier.width(50.dp))
    }
}

@Composable
fun ProjectPage(project: Project) {
    Column(Modifier.fillMaxWidth()) {
        Text(project.path.toString())
        Text(project.page.value.name)
    }
}