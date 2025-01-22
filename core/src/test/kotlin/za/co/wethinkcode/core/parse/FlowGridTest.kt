package za.co.wethinkcode.core.parse

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

enum class CellType {
    Nothing,
    CommitLeft
}

class FlowGrid {
    var _width = -1
    var _height = -1

    val width get() = _width + 1
    val height get() = _height + 1

    val map = mutableMapOf<Pair<Int, Int>, CellType>()

    operator fun get(x: Int, y: Int): CellType {
        return map.getOrDefault(Pair(x, y), CellType.Nothing)
    }

    operator fun set(x: Int, y: Int, value: CellType) {
        map[Pair(x, y)] = value
        if (x > _width) _width = x
        if (y > _height) _height = y
    }


}

class FlowGridTest {

    val grid = FlowGrid()

    @Test
    fun `empty`() {
        assertThat(grid[0, 0]).isEqualTo(CellType.Nothing)
        assertThat(grid.width).isEqualTo(0)
        assertThat(grid.height).isEqualTo(0)
    }

    @Test
    fun `existing cell`() {
        grid[0, 0] = CellType.CommitLeft
        assertThat(grid[0, 0]).isEqualTo(CellType.CommitLeft)
    }

    @Test
    fun `width and height change`() {
        grid[0, 0] = CellType.CommitLeft
        assertThat(grid.width).isEqualTo(1)
        assertThat(grid.height).isEqualTo(1)
    }
}