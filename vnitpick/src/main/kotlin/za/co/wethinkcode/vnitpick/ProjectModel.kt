package za.co.wethinkcode.vnitpick

import androidx.compose.runtime.mutableStateOf
import java.nio.file.Path
import kotlin.io.path.exists

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

    val flowModel = FlowModel()

    val pages = listOf(
        ProjectPage("Settings", ProjectPageType.Settings, true, true),
        ProjectPage("Process", ProjectPageType.Process, isPathJltk(path)),
        ProjectPage("StdOut", ProjectPageType.StdOut),
        ProjectPage("StdErr", ProjectPageType.StdErr),
    )

    private fun isPathJltk(path: Path): Boolean {
        if (!path.resolve(".jltk").exists()) return false
        flowModel.load(path)
        return true
    }

    val page = mutableStateOf(pages[0])


    fun pageTo(newPage: ProjectPage) {
        page.value.isSelected.value = false
        newPage.isSelected.value = true
        page.value = newPage
    }
}