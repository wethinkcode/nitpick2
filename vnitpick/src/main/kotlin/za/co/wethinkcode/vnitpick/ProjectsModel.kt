package za.co.wethinkcode.vnitpick

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf

class ProjectsModel {
    val projects = mutableStateListOf<Project>()
    val currentProjectIndex = mutableIntStateOf(-1)
    val currentProject = mutableStateOf<Project?>(null)

    fun add(project: Project) {
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