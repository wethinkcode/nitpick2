package za.co.wethinkcode.vnitpick

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp

@Composable
fun ProjectView(model: ProjectsModel) {
    val project by remember { model.currentProject }
    Row() {
        if (project == null) {
            Text(
                "No current project\n" + "To get started, use the folder icon to navigate to a project folder."
            )
        } else {
            Row(modifier = Modifier.fillMaxWidth()) {
                ProjectPages(project!!)
                ProjectPage(project!!)
            }
        }
    }
}

@Composable
fun ProjectPages(project: Project) {
    Column(Modifier.width(200.dp).fillMaxHeight().padding(10.dp)) {
        project.pages.forEach { page ->
            PageSelector(page, { project.pageTo(page) })
        }
    }
}

@Composable
fun PageSelector(page: ProjectPage, onClick: (ProjectPage) -> Unit) {
    Row(
        Modifier.fillMaxWidth().border(2.dp, Color.Blue)
    ) {
        val isSelected by remember { page.isSelected }
        val isEnabled by remember { page.isEnabled }
        Box(Modifier.weight(1f).fillMaxWidth().background(Color.White).clickable {
            if (isEnabled) onClick(page)
        }) {
            Text(
                page.name, color = Color.Black, fontSize = TextUnit(20f, TextUnitType.Sp)
            )
            if (isSelected) {
                Row(Modifier.width(50.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(" > ", color = Color.Black, fontSize = TextUnit(20f, TextUnitType.Sp))
                }
            } else Spacer(Modifier.width(50.dp))
        }
    }
}

@Composable
fun ProjectPage(project: Project) {
    Column(Modifier.fillMaxWidth()) {
        Text(project.path.toString())
    }
}