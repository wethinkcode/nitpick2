package za.co.wethinkcode.vnitpick

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import javax.swing.JFileChooser


@Composable
@Preview
fun App(model: NitpickModel) {
    var text by remember { mutableStateOf("Hello, World!") }

    MaterialTheme {
        Button(onClick = {
            text = "Hello, Desktop!"
            model.title.value = "Different title."
            val chooser = JFileChooser()
            chooser.fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
            val returnVal = chooser.showOpenDialog(ComposeWindow())
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                println(
                    "You chose to open this file: " +
                            chooser.selectedFile.name
                )
            }
        }) {
            Text(text)
        }
    }
}

fun main() = application {
    val model = NitpickModel()
    var windowTitle by remember { model.title }
    Window(
        onCloseRequest = ::exitApplication,
        title = windowTitle
    ) {
        App(model)
    }
}
