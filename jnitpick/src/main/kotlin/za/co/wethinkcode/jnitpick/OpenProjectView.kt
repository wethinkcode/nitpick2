package za.co.wethinkcode.jnitpick

import javafx.event.EventTarget
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.TreeItem
import javafx.scene.layout.*
import tornadofx.*
import za.co.wethinkcode.jnitpick.Styles.Companion.HEADER_BACKGROUND
import za.co.wethinkcode.jnitpick.Styles.Companion.HEADER_FONT
import za.co.wethinkcode.jnitpick.Styles.Companion.HEADER_TEXT_COLOR
import za.co.wethinkcode.jnitpick.Styles.Companion.LARGE_FONT
import za.co.wethinkcode.jnitpick.Styles.Companion.SMALL_FONT
import java.io.File
import java.nio.file.Path
import kotlin.io.path.isDirectory

private const val OPEN_PROJECT_HALF_WIDTH = 400.0

class OpenProjectModel {
    val root = TreeItem<Path>()

    init {
        root.isExpanded = true
        val files = File.listRoots().map { it.toPath() }
        files.forEach {
            val drive = TreeItem(it)
            if (it.isDirectory()) {
                drive.children.add(TreeItem<Path>(Path.of("Loading...")))
                drive.expandedProperty().addListener { item, _, expanded ->
                    if (expanded) reload(drive)
                }
            }
            root.children.add(drive)
        }
    }

    private fun reload(item: TreeItem<Path>) {
        val path = item.value
        val files = path.toFile().listFiles()
        item.children.clear()
        files.forEach { candidate ->
            if (candidate.isDirectory()) {
                val new = TreeItem(candidate.toPath())
                new.children.add(TreeItem(Path.of("Loading...")))
                new.expandedProperty().addListener { item, _, expanded ->
                    if (expanded) reload(new)
                }
                item.children.add(new)
            }
        }
    }
}

class OpenProjectView(val model: ProjectsModel) : Fragment() {
    var hasPath: Boolean = false
    var path: Path = Path.of("/")
    val projectModel = OpenProjectModel()
    override val root = hbox {
        minWidth = 800.0
        minHeight = 600.0
        vbox {
            minWidth = OPEN_PROJECT_HALF_WIDTH
            dialogHeader("Recent Projects")
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
        vbox {
            minWidth = OPEN_PROJECT_HALF_WIDTH
            dialogHeader("Available Files")
            treeview(projectModel.root) {
                vgrow = Priority.ALWAYS
                isShowRoot = false
            }
        }
    }

    private fun EventTarget.dialogHeader(text: String) {
        hbox {
            alignment = Pos.CENTER
            background = Background(BackgroundFill(HEADER_BACKGROUND, null, null))
            label(text) {
                font = HEADER_FONT
                textFill = HEADER_TEXT_COLOR
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
