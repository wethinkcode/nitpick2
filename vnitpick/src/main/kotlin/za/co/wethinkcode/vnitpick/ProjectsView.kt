package za.co.wethinkcode.vnitpick

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import kotlin.io.path.name

@Composable
fun ProjectsView(projects: List<Project>) {
    LazyColumn {
        items(projects.size) { i ->
            Row {
                Text(projects[i].path.toAbsolutePath().name)
            }
        }
    }
}