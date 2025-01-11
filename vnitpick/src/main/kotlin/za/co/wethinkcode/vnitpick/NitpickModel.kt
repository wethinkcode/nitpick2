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
            add(Project(chooser.selectedFile.toPath()))
        }
    }

    private fun add(project: Project) {
        projectsModel.add(project)
    }

    fun select(index: Int) {
        currentProjectIndex.value = index
        currentProject.value = projects[index]
    }

    fun close(index: Int) {
        if (index < 0 || index >= projects.size) return
        if (projects.size == 1) {
            // this is the last project
            currentProjectIndex.value = -1
            currentProject.value = null
            projects.removeAt(index)
            return
        }
        if (index == projects.lastIndex) {
            currentProjectIndex.value = index - 1
        }
        projects.removeAt(index)
        if (currentProjectIndex.value > index) {
            select(currentProjectIndex.value - 1)
        } else select(currentProjectIndex.value)
    }

    fun nextProject() {
        if (currentProjectIndex.value != projects.lastIndex) {
            select(currentProjectIndex.value + 1)
        }
    }

    fun previousProject() {
        if (currentProjectIndex.value > 0) {
            select(currentProjectIndex.value - 1)
        }
    }

}
