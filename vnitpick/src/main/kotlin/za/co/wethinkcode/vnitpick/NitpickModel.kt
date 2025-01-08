package za.co.wethinkcode.vnitpick

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.awt.ComposeWindow
import javax.swing.JFileChooser

class NitpickModel {
    val title = mutableStateOf("Starting Title")
    val projects = mutableStateListOf<Project>()
    fun open() {
        val chooser = JFileChooser()
        chooser.fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
        val chooserResult = chooser.showOpenDialog(ComposeWindow())
        if (chooserResult == JFileChooser.APPROVE_OPTION) {
            projects.add(Project(chooser.selectedFile.toPath()))
        }

    }

}
