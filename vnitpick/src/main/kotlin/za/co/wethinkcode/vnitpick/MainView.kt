package za.co.wethinkcode.vnitpick

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import compose.icons.fontawesomeicons.RegularGroup
import compose.icons.fontawesomeicons.SolidGroup
import compose.icons.fontawesomeicons.regular.FolderOpen
import compose.icons.fontawesomeicons.solid.CaretLeft
import compose.icons.fontawesomeicons.solid.CaretRight
import za.co.wethinkcode.vnitpick.Styles.DEFAULT_FONT_SIZE
import za.co.wethinkcode.vnitpick.Styles.DEFAULT_ICON_SIZE
import kotlin.io.path.name

@Composable
@Preview
fun MainView(model: NitpickModel) {
    Column(Modifier.fillMaxSize()) {
        Row(
            Modifier.fillMaxWidth()
                .background(Color.LightGray, RectangleShape)
                .padding(4.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OpenIcon(model)
            ProjectTabRow(model)
            Icon(SolidGroup.CaretLeft, "Scroll Left", Modifier.size(DEFAULT_ICON_SIZE))
            Icon(SolidGroup.CaretRight, "Scroll Right", Modifier.size(DEFAULT_ICON_SIZE))

        }
    }
}

@Composable
fun ProjectTabRow(model: NitpickModel) {
    if (model.projects.isNotEmpty()) {
        var tab by remember { model.currentProjectIndex }
        ScrollableTabRow(
            tab,
            Modifier.fillMaxWidth(0.95f),
            backgroundColor = Color.LightGray,
            contentColor = Color.Black,
        ) {
            model.projects.forEachIndexed { index, project ->
                Tab(
                    index == tab,
                    { model.select(index) },
                    Modifier.padding(4.dp)
                ) {
                    Text(project.path.name, fontSize = DEFAULT_FONT_SIZE)
                }
            }
        }
    } else {
        Spacer(Modifier.fillMaxWidth(.95f).background(Color.LightGray, RectangleShape).padding(2.dp))
    }
}

@Composable
fun OpenIcon(model: NitpickModel) {
    Icon(RegularGroup.FolderOpen,
        "Open Project",
        Modifier.size(DEFAULT_ICON_SIZE)
            .clickable {
                model.open()
            }
    )
}

object Styles {
    val DEFAULT_ICON_SIZE = 26.dp
    val DEFAULT_FONT_SIZE = TextUnit(16f, TextUnitType.Sp)

}