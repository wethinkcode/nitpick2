package za.co.wethinkcode.vnitpick.projects

import androidx.compose.runtime.*
import za.co.wethinkcode.vnitpick.ProjectModel
import java.nio.file.Path
import kotlin.io.path.exists

class ProjectsModel {
    val projects = mutableStateListOf<ProjectModel>()
    val currentProjectIndex = mutableIntStateOf(-1)
    val currentProject = mutableStateOf<ProjectModel?>(null)
    val openModel = OpenModel()

    fun add(project: ProjectModel) {
        projects.add(project)
        currentProjectIndex.value = projects.lastIndex
        currentProject.value = projects.last()
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
            projects[index].close()
            projects.removeAt(index)
            return
        }
        if (index == projects.lastIndex) {
            currentProjectIndex.value = index - 1
        }
        projects[index].close()
        projects.removeAt(index)
        if (currentProjectIndex.value > index) {
            select(currentProjectIndex.value - 1)
        } else select(currentProjectIndex.value)
    }

    fun open() {
        val path = Path.of(openModel.filename.value.text)
        if (!path.exists()) return
        add(ProjectModel(path))
        openModel.mru.add(path)
        openModel.isOpening.value = false
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
