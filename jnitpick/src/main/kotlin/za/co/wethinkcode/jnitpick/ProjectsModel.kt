package za.co.wethinkcode.jnitpick

import tornadofx.*
import za.co.wethinkcode.core.Mru

class ProjectsModel {
    private val mru = Mru()
    val mruPaths = mru.items.toObservable()
}