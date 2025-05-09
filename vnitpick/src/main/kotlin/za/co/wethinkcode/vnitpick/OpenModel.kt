package za.co.wethinkcode.vnitpick

import androidx.compose.runtime.mutableStateOf
import za.co.wethinkcode.vnitpick.tree.ExpandedState.*
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
                val childNode = UiTreeNode(child)
                node.children.add(childNode)
                if (child.isDirectory()) {
                    childNode.expandedState.value = Closed
                }
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