package za.co.wethinkcode.jnitpick

import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.layout.*
import tornadofx.*
import za.co.wethinkcode.jnitpick.Styles.Companion.HEADER_BACKGROUND
import za.co.wethinkcode.jnitpick.Styles.Companion.HEADER_FONT
import za.co.wethinkcode.jnitpick.Styles.Companion.HEADER_TEXT_COLOR
import za.co.wethinkcode.jnitpick.Styles.Companion.LARGE_FONT
import za.co.wethinkcode.jnitpick.Styles.Companion.SMALL_FONT

class OpenProjectView(val model: ProjectsModel) : Fragment() {
    override val root = hbox {
        minWidth = 800.0
        minHeight = 600.0
        vbox {
            minWidth = 400.0
            padding = Insets(8.0)
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
                cellFormat {
                    graphic = cache {
                        hbox {
                            stackpane {
                                button("O") {
                                    action { println("Open") }
                                }
                            }
                            vbox {
                                padding = Insets(0.0, 8.0, 0.0, 8.0)
                                val text = it.fileName.toString()
                                label(text) {
                                    font = LARGE_FONT
                                }
                                label(it.toAbsolutePath().toString()) {
                                    font = SMALL_FONT
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
