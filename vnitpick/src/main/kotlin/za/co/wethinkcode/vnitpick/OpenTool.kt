package za.co.wethinkcode.vnitpick

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposeWindow
import compose.icons.fontawesomeicons.RegularGroup
import compose.icons.fontawesomeicons.regular.FolderOpen
import za.co.wethinkcode.vnitpick.Styles.DEFAULT_ICON_SIZE
import javax.swing.JFileChooser

@Composable
fun OpenTool(model: ProjectsModel) {
    Icon(
        RegularGroup.FolderOpen,
        "Open Project",
        Modifier.size(DEFAULT_ICON_SIZE)
            .clickable {
                open(model)
            }
    )
}

fun open(model: ProjectsModel) {
    val chooser = JFileChooser()
    chooser.fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
    val chooserResult = chooser.showOpenDialog(ComposeWindow())
    if (chooserResult == JFileChooser.APPROVE_OPTION) {
        model.add(Project(chooser.selectedFile.toPath()))
    }
}

