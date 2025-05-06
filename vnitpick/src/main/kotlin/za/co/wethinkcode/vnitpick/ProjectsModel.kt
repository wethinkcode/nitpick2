package za.co.wethinkcode.vnitpick

import androidx.compose.runtime.*

class ProjectsModel {
    val projects = mutableStateListOf<ProjectModel>()
    val currentProjectIndex = mutableIntStateOf(-1)
    val currentProject = mutableStateOf<ProjectModel?>(null)
    val isOpening = mutableStateOf(false);

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
