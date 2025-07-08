package za.co.wethinkcode.jnitpick

import tornadofx.*
import za.co.wethinkcode.core.Mru
import java.nio.file.Path

class ProjectModel(val path: Path) {
    val name: String get() = path.fileName.toString()
}

class ProjectsModel {
    fun open(path: Path): ProjectModel {
        return ProjectModel(path)
    }

    private val mru = Mru()
    val mruPaths = mru.items.toObservable()
}