package za.co.wethinkcode.vnitpick.projects

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import za.co.wethinkcode.vnitpick.tree.ExpandedState.*
import za.co.wethinkcode.vnitpick.tree.UiTreeNode
import java.io.File
import java.nio.file.Path
import kotlin.io.path.*

class OpenModel {
    val root = UiTreeNode(Path.of("/"))
    val selected = mutableStateOf(root)
    val mruOptions = listOf<String>()
    val filename = mutableStateOf(TextFieldValue(""))
    val isOpening = mutableStateOf(false)


    init {
        val files = File.listRoots().map { it.toPath() }
        files.forEach {
            val drive = UiTreeNode(it)
            if (it.isDirectory()) {
                drive.expandedState.value = Closed
            }
            root.children.add(drive)
        }
    }

    fun expand(node: UiTreeNode<Path>) {
        when (node.expandedState.value) {
            None -> {}
            Open -> node.expandedState.value = Closed
            Closed -> {
                refreshChildren(node)
                node.expandedState.value = Open
            }
        }
    }

    fun refreshChildren(node: UiTreeNode<Path>) {
        val path = node.item
        if (path.isDirectory()) {
            node.children.clear()
            val children = path.listDirectoryEntries()
            children.forEach { child ->
                if (child.isDirectory()) {
                    val childNode = UiTreeNode(child)
                    node.children.add(childNode)
                    childNode.expandedState.value = Closed
                }
            }
        }
    }

    fun select(node: UiTreeNode<Path>) {
        selected.value.isSelected.value = false
        node.isSelected.value = true
        selected.value = node
        refreshChildren(node)
        filename.value = TextFieldValue(node.item.absolutePathString())
    }

}