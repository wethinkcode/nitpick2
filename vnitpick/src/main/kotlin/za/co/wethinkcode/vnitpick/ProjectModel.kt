package za.co.wethinkcode.vnitpick

import androidx.compose.runtime.mutableStateOf
import za.co.wethinkcode.vnitpick.flow.FlowModel
import java.nio.file.Path

class ProjectPage(
    val name: String,
    val type: ProjectPageType,
    isEnabled: Boolean = false,
    isSelected: Boolean = false
) {
    val tooltipText = mutableStateOf("")
    val isEnabled = mutableStateOf(isEnabled)
    val isSelected = mutableStateOf(isSelected)
}

class ProjectModel(val path: Path) {

    val flowModel = FlowModel(path)

    val pages = listOf(
        ProjectPage("Settings", ProjectPageType.Settings, true, true),
        ProjectPage("Process", ProjectPageType.Process, flowModel.isJltk.value),
        ProjectPage("StdOut", ProjectPageType.StdOut),
        ProjectPage("StdErr", ProjectPageType.StdErr),
    )

    val page = mutableStateOf(pages[0])

    fun close() {
        flowModel.close()
    }

    fun pageTo(newPage: ProjectPage) {
        page.value.isSelected.value = false
        newPage.isSelected.value = true
        page.value = newPage
    }
}