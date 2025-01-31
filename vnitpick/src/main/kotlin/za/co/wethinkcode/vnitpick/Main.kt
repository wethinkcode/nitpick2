package za.co.wethinkcode.vnitpick

import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application


fun main() = application {
    val model = NitpickModel()
    val windowTitle by remember { model.title }
    val windowState = remember { WindowState(size = DpSize(1600.dp, 900.dp)) }

    Window(
        state = windowState,
        onCloseRequest = ::exitApplication,
        title = windowTitle,
    ) {
        MainView(model)
    }
}

