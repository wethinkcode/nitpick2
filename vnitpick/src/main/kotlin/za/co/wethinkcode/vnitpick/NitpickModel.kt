package za.co.wethinkcode.vnitpick

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.awt.ComposeWindow
import javax.swing.JFileChooser

class NitpickModel {
    val title = mutableStateOf("Starting Title")
    val projectsModel = ProjectsModel()
    val projects get() = projectsModel.projects
    val currentProjectIndex get() = projectsModel.currentProjectIndex
    val currentProject get() = projectsModel.currentProject

    fun open() {
        val chooser = JFileChooser()
        chooser.fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
        val chooserResult = chooser.showOpenDialog(ComposeWindow())
        if (chooserResult == JFileChooser.APPROVE_OPTION) {
            projectsModel.add(Project(chooser.selectedFile.toPath()))
        }
    }

    fun select(index: Int) {
        projectsModel.select(index)
    }

    fun close(index: Int) {
        projectsModel.close(index)
    }

    fun nextProject() {
        projectsModel.nextProject()
    }

    fun previousProject() {
        projectsModel.previousProject()
    }

}
