package za.co.wethinkcode.vnitpick

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.Dialog
import za.co.wethinkcode.vnitpick.tree.UiTreeNode
import za.co.wethinkcode.vnitpick.tree.UiTreeView
import java.nio.file.Path
import kotlin.io.path.name


@Composable
fun OpenDialog(model: OpenModel, onDismissRequest: () -> Unit) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(800.dp)
                .width(600.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column {
                UiTreeView(
                    model.root,
                    model::expand,
                    model::select
                ) { item ->
                    PathNode(item)
                }
            }
        }
    }
}

@Composable
fun PathNode(node: UiTreeNode<Path>) {
    val name = if (node.item.nameCount == 0) node.item.toString() else node.item.name
    Text(
        name,
        Modifier
            .padding(4.dp)
            .fillMaxWidth(),
        fontSize = TextUnit(14f, TextUnitType.Sp)
    )
}