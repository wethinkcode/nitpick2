package za.co.wethinkcode.vnitpick.projects

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import compose.icons.fontawesomeicons.RegularGroup
import compose.icons.fontawesomeicons.regular.FolderOpen
import za.co.wethinkcode.vnitpick.Styles.DEFAULT_ICON_SIZE
import za.co.wethinkcode.vnitpick.Styles.HEADER_FONT_SIZE
import za.co.wethinkcode.vnitpick.Styles.LARGE_FONT_SIZE
import za.co.wethinkcode.vnitpick.Styles.SMALL_FONT_SIZE
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
                    .background(Color.White)
                    .border(2.dp, Color.Black, RectangleShape)
                    .size(DIALOG_WIDTH, DIALOG_HEIGHT)
            ) {
                Row {
                    MruColumn(model, onOpen)
                    Spacer(Modifier.width(1.dp).height(CONTENT_HEIGHT).background(Color.Black))
                    FileTreeColumn(model, onOpen)
                }
                Row {
                    Controls(model, onOpen)
                }
            }
        }

    }
}

@Composable
fun FileTreeColumn(model: OpenModel, onOpen: () -> Unit) {
    Column(
        Modifier
            .size(FILETREE_WIDTH, CONTENT_HEIGHT)
            .padding(16.dp, 16.dp),
    ) {
        CenteredHeader("Folders")
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
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MruColumn(model: OpenModel, onOpen: () -> Unit) {
    Column(
        Modifier.padding(16.dp)
            .size(400.dp, 700.dp)
    )
    {
        CenteredHeader("Recent Projects")
        LazyColumn(Modifier.padding(16.dp, 8.dp)) {
            items(model.mruOptions) {
                Column(
                    Modifier.padding(16.dp, 8.dp).combinedClickable(
                        onClick = {
                            model.openTo(Path.of(it))
                        },
                        onDoubleClick = {
                            model.filename.value = TextFieldValue(it)
                            onOpen()
                        },
                    )
                ) {
                    Row {
                        Column(
                            Modifier.align(Alignment.CenterVertically).padding(16.dp, 0.dp),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                RegularGroup.FolderOpen,
                                "Open Project",
                                Modifier.size(DEFAULT_ICON_SIZE)
                                    .clickable {
                                        model.filename.value = TextFieldValue(it)
                                        onOpen()
                                    }
                            )
                        }
                        Column(verticalArrangement = Arrangement.Center) {

                            val text = Path.of(it).fileName.toString()
                            Text(text, fontSize = LARGE_FONT_SIZE)
                            Text(it, fontSize = SMALL_FONT_SIZE)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CenteredHeader(text: String) {
    Row(
        Modifier.height(50.dp).fillMaxWidth().background(Color.DarkGray),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text, color = Color.White, fontSize = HEADER_FONT_SIZE)
    }
}

@Composable
fun Controls(model: OpenModel, onOpen: () -> Unit) {
    Column(Modifier.height(100.dp)) {
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
                        model.openTo(Path.of(model.filename.value.text))
                        expanded.value = false
                    }
                ) {
                    Text(text = selectionOption)
                }
            }
        }
    }
}

val CONTENT_HEIGHT = 700.dp
val MRU_WIDTH = 400.dp
val FILETREE_WIDTH = 500.dp
val CONTROLS_HEIGHT = 100.dp
val DIALOG_HEIGHT = CONTENT_HEIGHT + CONTROLS_HEIGHT
val DIALOG_WIDTH = MRU_WIDTH + FILETREE_WIDTH

