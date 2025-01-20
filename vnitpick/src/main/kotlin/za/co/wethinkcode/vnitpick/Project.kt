package za.co.wethinkcode.vnitpick

import androidx.compose.runtime.mutableStateOf
import java.nio.file.Path

class ProjectPage(val name: String, val type: ProjectPageType) {
    val tooltipText = mutableStateOf("")
    val isEnabled = mutableStateOf(false)
    val isSelected = mutableStateOf(false)
}

class Project(val path: Path) {

    val pages = listOf(
        ProjectPage("Settings", ProjectPageType.Settings),
        ProjectPage("Process", ProjectPageType.Process),
        ProjectPage("StdOut", ProjectPageType.StdOut),
        ProjectPage("StdErr", ProjectPageType.StdErr),
    )

    val page = mutableStateOf(pages[0])

    fun pageTo(newPage: ProjectPage) {
        page.value.isSelected.value = false
        newPage.isSelected
        page.value = newPage
    }
}