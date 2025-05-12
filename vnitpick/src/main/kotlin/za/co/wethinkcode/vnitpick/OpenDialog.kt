package za.co.wethinkcode.vnitpick

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import za.co.wethinkcode.vnitpick.tree.UiTreeNode
import za.co.wethinkcode.vnitpick.tree.UiTreeView
import java.nio.file.Path
import kotlin.io.path.name


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun OpenDialog(model: OpenModel, onOpen: () -> Unit) {
    val properties = DialogProperties(
        usePlatformDefaultWidth = false,
        dismissOnBackPress = false,
        dismissOnClickOutside = false,
        usePlatformInsets = true
    )
    if (model.isOpening.value) {
        Dialog(onDismissRequest = { }, properties) {
            Column(
                Modifier.padding(16.dp)
                    .size(600.dp, 800.dp)
                    .background(Color.White)
                    .border(2.dp, Color.Black, RectangleShape)
                    .padding(32.dp, 16.dp),
            ) {
                MruCombo(model)
                Column(
                    modifier = Modifier.weight(0.5F)
                ) {

                    UiTreeView(
                        model.root,
                        model::expand,
                        model::select
                    ) { item ->
                        PathNode(item)
                    }
                }
                Controls(model, onOpen)
            }
        }
    }
}

@Composable
fun Controls(model: OpenModel, onOpen: () -> Unit) {
    Column {
        Spacer(Modifier.height(1.dp).fillMaxWidth().background(Color.Black))
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(Modifier.weight(1f))
            Button(
                { model.isOpening.value = false },
                Modifier.padding(horizontal = 8.dp)
            ) {
                Text("Cancel")
            }
            Button({ onOpen() }, Modifier.padding(horizontal = 8.dp)) {
                Text("Open")
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
fun MruCombo(model: OpenModel) {
    val expanded = remember { mutableStateOf(false) }

    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp))
            .border(BorderStroke(1.dp, Color.LightGray), RoundedCornerShape(4.dp))
    ) {
        BasicTextField(
            value = model.filename.value,
            onValueChange = {
                model.filename.value = it
            },
            Modifier.fillMaxWidth()
        )
        Icon(
            Icons.Filled.ArrowDropDown, "contentDescription",
            Modifier.align(Alignment.CenterEnd)
                .clickable { expanded.value = !expanded.value },
        )
        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false }
        ) {
            model.mruOptions.forEach { selectionOption ->
                DropdownMenuItem(
                    onClick = {
                        model.filename.value = TextFieldValue(selectionOption)
                        expanded.value = false
                    }
                ) {
                    Text(text = selectionOption)
                }
            }
        }
    }
}