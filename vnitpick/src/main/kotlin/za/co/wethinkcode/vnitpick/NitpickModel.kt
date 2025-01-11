package za.co.wethinkcode.vnitpick

import androidx.compose.runtime.mutableStateOf

class NitpickModel {
    val title = mutableStateOf("Starting Title")
    val projectsModel = ProjectsModel()
}
