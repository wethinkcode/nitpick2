package za.co.wethinkcode.jnitpick

import javafx.geometry.Pos
import javafx.scene.layout.*
import tornadofx.*
import za.co.wethinkcode.jnitpick.Styles.Companion.HEADER_BACKGROUND
import za.co.wethinkcode.jnitpick.Styles.Companion.HEADER_FONT
import za.co.wethinkcode.jnitpick.Styles.Companion.HEADER_TEXT_COLOR

class OpenProjectView(val model: ProjectsModel) : Fragment() {
    override val root = hbox {
        minWidth = 800.0
        minHeight = 600.0
        vbox {
            minWidth = 400.0
            hbox {
                alignment = Pos.CENTER
                background = Background(BackgroundFill(HEADER_BACKGROUND, null, null))
                label("Recent Projects") {
                    font = HEADER_FONT
                    textFill = HEADER_TEXT_COLOR
                }
            }
            listview(model.mruPaths) {
                vgrow = Priority.ALWAYS
            }
        }
    }
}
