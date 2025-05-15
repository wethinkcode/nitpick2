package za.co.wethinkcode.vnitpick.projects

import org.junit.jupiter.api.Test
import za.co.wethinkcode.core.TestFolder

class MruTest {

    val folder = TestFolder()
    val mru = Mru(folder.root)

    @Test
    fun testMru() {
        println(mru.items)
    }
}