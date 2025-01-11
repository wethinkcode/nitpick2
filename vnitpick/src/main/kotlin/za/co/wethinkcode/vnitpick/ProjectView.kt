package za.co.wethinkcode.vnitpick

import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember

@Composable
fun ProjectView(model: ProjectsModel) {
    val project by remember { model.currentProject }
    Row() {
        if (project != null) {
            Text(project!!.path.toString())
        }
    }
}