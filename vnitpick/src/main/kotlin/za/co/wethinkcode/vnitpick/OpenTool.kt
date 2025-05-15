package za.co.wethinkcode.vnitpick

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import compose.icons.fontawesomeicons.RegularGroup
import compose.icons.fontawesomeicons.regular.FolderOpen
import za.co.wethinkcode.vnitpick.Styles.DEFAULT_ICON_SIZE
import za.co.wethinkcode.vnitpick.projects.ProjectsModel

@Composable
fun OpenTool(model: ProjectsModel) {
    Icon(
        RegularGroup.FolderOpen,
        "Open Project",
        Modifier.size(DEFAULT_ICON_SIZE)
            .clickable {
                model.openModel.isOpening.value = true
            }
    )
}

