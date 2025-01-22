package za.co.wethinkcode.core.parse

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

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

    @Test
    fun `width and height change even with sparse`() {
        grid[3, 7] = CellType.CommitLeft
        assertThat(grid.width).isEqualTo(4)
        assertThat(grid.height).isEqualTo(8)
    }

    @Test
    fun `less than width and height don't change width and height`() {
        grid[3, 7] = CellType.CommitLeft
        assertThat(grid.width).isEqualTo(4)
        assertThat(grid.height).isEqualTo(8)
        grid[2, 6] = CellType.CommitLeft
        assertThat(grid.width).isEqualTo(4)
        assertThat(grid.height).isEqualTo(8)
    }
}