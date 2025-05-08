package za.co.wethinkcode.vnitpick

import androidx.compose.runtime.mutableStateOf
import za.co.wethinkcode.vnitpick.tree.ExpandedState
import za.co.wethinkcode.vnitpick.tree.UiTreeNode
import java.io.File
import java.nio.file.Path
import kotlin.io.path.isDirectory
import kotlin.io.path.listDirectoryEntries

class OpenModel {
    val root = UiTreeNode(Path.of("/"))
    val selected = mutableStateOf(root)

    init {
        val files = File.listRoots().map { it.toPath() }
        files.forEach {
            root.children.add(UiTreeNode(it))
        }
        refreshChildren(root.children.get(0))
        root.children.get(0).expandedState.value = ExpandedState.Closed
    }

    fun expand(node: UiTreeNode<Path>) {
        when (node.expandedState.value) {
            ExpandedState.None -> {}
            ExpandedState.Open -> node.expandedState.value = ExpandedState.Closed
            ExpandedState.Closed -> {
                refreshChildren(node)
                node.expandedState.value = ExpandedState.Open
            }
        }
    }

    fun refreshChildren(node: UiTreeNode<Path>) {
        val path = node.item
        if (path.isDirectory()) {
            node.children.clear()
            val children = path.listDirectoryEntries()
            children.forEach {
                node.children.add(UiTreeNode(it))
            }
        }
    }

    fun select(uiTreeNode: UiTreeNode<Path>) {
        println("Select")
        selected.value.isSelected.value = false
        uiTreeNode.isSelected.value = true
        selected.value = uiTreeNode
        refreshChildren(uiTreeNode)
    }

}