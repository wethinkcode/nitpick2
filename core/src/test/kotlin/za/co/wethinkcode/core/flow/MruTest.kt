package za.co.wethinkcode.core.flow

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import za.co.wethinkcode.core.Mru
import za.co.wethinkcode.core.Mru.Companion.MRU_FILE
import za.co.wethinkcode.core.TestFolder
import kotlin.io.path.exists

class MruTest {

    val folder = TestFolder()
    val mru = Mru(folder.root, 3)

    @Test
    fun emptyMruOnlyContainsHome() {
        assertThat(mru.items).containsExactly(folder.root)
        folder.delete()
    }

    @Test
    fun addAdds() {
        val first = folder.root.resolve("path1")
        mru.add(first)
        assertThat(mru.items).containsExactly(first)
        folder.delete()
    }


    @Test
    fun listIsLifo() {
        val first = folder.root.resolve("path1")
        mru.add(first)
        val second = folder.root.resolve("path2")
        mru.add(second)
        assertThat(mru.items).containsExactly(second, first)
        folder.delete()
    }

    @Test
    fun repositionsDuplicates() {
        val first = folder.root.resolve("path1")
        mru.add(first)
        val second = folder.root.resolve("path2")
        mru.add(second)
        mru.add(first)
        assertThat(mru.items).containsExactly(first, second)
        folder.delete()
    }

    @Test
    fun respectsCacheSize() {
        val first = folder.root.resolve("path1")
        mru.add(first)
        val second = folder.root.resolve("path2")
        mru.add(second)
        val third = folder.root.resolve("path3")
        mru.add(third)
        val fourth = folder.root.resolve("path4")
        mru.add(fourth)
        assertThat(mru.items).containsExactly(fourth, third, second)
        folder.delete()
    }

    @Test
    fun addSaves() {
        val first = folder.root.resolve("path1")
        mru.add(first)
        assertThat(folder.root.resolve(MRU_FILE).exists()).isTrue()
        folder.delete()
    }

    @Test
    fun loadsOnConstruction() {
        val first = folder.root.resolve("path1")
        mru.add(first)
        val second = folder.root.resolve("path2")
        mru.add(second)
        val third = folder.root.resolve("path3")
        mru.add(third)
        val newMru = Mru(folder.root, 3)
        assertThat(newMru.items).containsExactly(third, second, first)
        folder.delete()
    }
}