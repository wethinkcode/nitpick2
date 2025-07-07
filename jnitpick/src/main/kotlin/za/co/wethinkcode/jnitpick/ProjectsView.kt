package za.co.wethinkcode.jnitpick

import javafx.geometry.Pos
import javafx.scene.control.Tab
import javafx.scene.layout.*
import javafx.scene.layout.AnchorPane.*
import tornadofx.*

class ProjectsView : View() {

    val model = ProjectsModel()

    val controls = hbox {
        alignment = Pos.CENTER_RIGHT
        button("+") {
            background = Background.EMPTY
            border = Border.EMPTY
            action { open() }
        }
    }
    val tabs = tabpane {
    }


    override val root = borderpane {
        center = anchorpane {
            this += tabs
            this += controls
            setTopAnchor(controls, 3.0);
            setRightAnchor(controls, 5.0);
            setTopAnchor(tabs, 1.0);
            setRightAnchor(tabs, 1.0);
            setLeftAnchor(tabs, 1.0);
            setBottomAnchor(tabs, 1.0);
        }
    }

    fun open() {
        OpenProjectView(model).openModal()
        tabs.tabs.add(Tab("new"))
        tabs.selectionModel.select(tabs.tabs.size - 1)
    }
}