package za.co.wethinkcode.vnitpick

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
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
            shape = androidx.compose.ui.graphics.RectangleShape,
        ) {
            Column {
                Dropdown()
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

@Composable
fun Dropdown() {
    val options = listOf("Option 1", "Option 2", "Option 3", "Option 4", "Option 5")
    val expanded = remember { mutableStateOf(false) }
    val selectedOptionText = remember { mutableStateOf(options[0]) }
    var textState by remember { mutableStateOf(TextFieldValue(options[0])) }

    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = Modifier
            .size(250.dp, 32.dp)
            .clip(RoundedCornerShape(4.dp))
            .border(BorderStroke(1.dp, Color.LightGray), RoundedCornerShape(4.dp))
            .clickable { expanded.value = !expanded.value },
    ) {
        BasicTextField(value = textState, onValueChange = {
            textState = it
        })
        Icon(
            Icons.Filled.ArrowDropDown, "contentDescription",
            Modifier.align(Alignment.CenterEnd)
        )
        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false }
        ) {
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    onClick = {
                        selectedOptionText.value = selectionOption
                        expanded.value = false
                    }
                ) {
                    Text(text = selectionOption)
                }
            }
        }
    }
}