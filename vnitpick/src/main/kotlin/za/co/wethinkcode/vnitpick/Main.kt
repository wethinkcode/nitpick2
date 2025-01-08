package za.co.wethinkcode.vnitpick

import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application


fun main() = application {
    val model = NitpickModel()
    val windowTitle by remember { model.title }
    Window(
        onCloseRequest = ::exitApplication,
        title = windowTitle
    ) {
        MainView(model)
    }
}
