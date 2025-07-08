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
import java.nio.file.Path

class OpenProjectView(val model: ProjectsModel) : Fragment() {
    var hasPath: Boolean = false
    var path: Path = Path.of("/")
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
                cellFormat { path ->
                    graphic = cache {
                        hbox {
                            stackpane {
                                button("O") {
                                    action { prepareToOpen(path) }
                                }
                            }
                            vbox {
                                padding = Insets(0.0, 8.0, 0.0, 8.0)
                                val text = path.fileName.toString()
                                label(text) {
                                    font = LARGE_FONT
                                }
                                label(path.toAbsolutePath().toString()) {
                                    font = SMALL_FONT
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    fun prepareToOpen(toOpen: Path) {
        println("Preparing $toOpen")
        hasPath = true
        path = toOpen
        close()
    }
}
