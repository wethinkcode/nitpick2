package za.co.wethinkcode.vnitpick

import androidx.compose.runtime.mutableStateOf
import java.nio.file.Path

class Project(val path: Path) {
    val currentPage = mutableStateOf(ProjectPage.Settings)

    fun pageTo(page: ProjectPage) {
        currentPage.value = page
    }
}