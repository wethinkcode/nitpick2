package za.co.wethinkcode.vnitpick

import androidx.compose.runtime.mutableStateOf
import dev.hydraulic.conveyor.control.SoftwareUpdateController
import za.co.wethinkcode.vnitpick.projects.ProjectsModel

class NitpickModel {
    val title = mutableStateOf("Starting Title")
    val projectsModel = ProjectsModel()

    constructor() {
        val controller: SoftwareUpdateController? = SoftwareUpdateController.getInstance()
        if (controller != null) {
            val version = controller.currentVersion
            title.value = version.version
        } else title.value = "Developer Build"

    }
}
