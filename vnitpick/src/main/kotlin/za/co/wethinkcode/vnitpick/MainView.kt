package za.co.wethinkcode.vnitpick

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import compose.icons.fontawesomeicons.SolidGroup
import compose.icons.fontawesomeicons.solid.CaretLeft
import compose.icons.fontawesomeicons.solid.CaretRight
import za.co.wethinkcode.vnitpick.Styles.DEFAULT_ICON_SIZE

@Composable
@Preview
fun MainView(model: NitpickModel) {
    ProjectsView(model)
}

@Composable
fun ProjectsView(model: NitpickModel) {
    Column(Modifier.fillMaxSize()) {
        ProjectsBar(model.projectsModel)
        ProjectView(model.projectsModel)
    }
}

@Composable
fun ProjectsBar(model: ProjectsModel) {
    Row(
        Modifier.fillMaxWidth()
            .background(Color.LightGray, RectangleShape)
            .padding(4.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        OpenTool(model)
        ProjectTabs(model)
        Spacer(Modifier.weight(1f))
        Icon(SolidGroup.CaretLeft,
            "Scroll Left",
            Modifier.size(DEFAULT_ICON_SIZE)
                .clickable { model.previousProject() }
        )
        Icon(
            SolidGroup.CaretRight,
            "Scroll Right",
            Modifier.size(DEFAULT_ICON_SIZE)
                .clickable { model.nextProject() }
        )
        OpenDialog(model.openModel) { project -> model.add(project) }
    }
}
