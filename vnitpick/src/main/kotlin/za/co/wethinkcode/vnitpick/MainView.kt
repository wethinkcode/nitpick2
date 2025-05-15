package za.co.wethinkcode.vnitpick

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import za.co.wethinkcode.vnitpick.projects.ProjectsView

@Composable
@Preview
fun MainView(model: NitpickModel) {
    ProjectsView(model)
}
