package za.co.wethinkcode.jnitpick

import javafx.scene.control.Tab
import javafx.scene.layout.Background
import javafx.scene.layout.Border
import tornadofx.*

class ProjectsView : View() {

    val model = ProjectsModel()

    val tabs = tabpane {
        tab("") {
            graphic = button("+") {
                background = Background.EMPTY
                border = Border.EMPTY
                action { open() }
            }
            isClosable = false
            content = stackpane {
                minWidth = 400.0
                minHeight = 400.0
            }
        }
    }


    override val root = tabs

    fun open() {
        tabs.tabs.add(tabs.tabs.size - 1, Tab("new"))
        tabs.selectionModel.select(tabs.tabs.size - 2)
    }
}