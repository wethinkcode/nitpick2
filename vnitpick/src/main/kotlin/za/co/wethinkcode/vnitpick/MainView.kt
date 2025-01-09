package za.co.wethinkcode.vnitpick

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import compose.icons.fontawesomeicons.SolidGroup
import compose.icons.fontawesomeicons.solid.CaretLeft
import compose.icons.fontawesomeicons.solid.CaretRight
import kotlin.io.path.name

@Composable
@Preview
fun MainView(model: NitpickModel) {
    Column(Modifier.fillMaxSize()) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(onClick = { model.open() }) {
                Text("Open")
            }
            LazyRow() {
                items(model.projects) { project ->
                    Button(onClick = { }) {
                        Text(project.path.name)
                    }
                }
            }
            Icon(SolidGroup.CaretLeft, "Scroll Left", Modifier.size(22.dp))
            Icon(SolidGroup.CaretRight, "Scroll Right", Modifier.size(22.dp))
        }
    }
}