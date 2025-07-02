package za.co.wethinkcode.jnitpick

import javafx.application.Application
import tornadofx.*


class MainApp : App() {
    override val primaryView = ProjectsView::class

}

fun main(args: Array<String>) {
    Application.launch(MainApp::class.java, *args)
}