package za.co.wethinkcode.vnitpick

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import za.co.wethinkcode.vnitpick.Styles.DEFAULT_FONT_SIZE
import za.co.wethinkcode.vnitpick.Styles.DEFAULT_ICON_SIZE
import kotlin.io.path.name

@Composable
fun ProjectTabs(model: ProjectsModel) {
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
                    Row(Modifier.padding(4.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text(project.path.name, fontSize = DEFAULT_FONT_SIZE)
                        Spacer(Modifier.width(4.dp))
                        Icon(Icons.Default.Close,
                            "Close",
                            Modifier.clickable { model.close(index) }
                                .size(DEFAULT_ICON_SIZE)
                        )
                    }
                }
            }
        }
    } else {
        Spacer(Modifier.fillMaxWidth(.95f).background(Color.LightGray, RectangleShape).padding(2.dp))
    }
}