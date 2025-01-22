package za.co.wethinkcode.core

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import za.co.wethinkcode.core.parse.LogCollater
import za.co.wethinkcode.core.parse.RunsBuilder

class CommitsTest {

    val builder = RunsBuilder()
    val collater = LogCollater()

    @Test
    fun `no commits means empty grid`() {
        val commits = collater.collate(builder.toList())
        assertThat(commits.toFlowGrid().width).isEqualTo(0)
        assertThat(commits.toFlowGrid().height).isEqualTo(0)
    }

    @Test
    fun `one solo run means 2x2`() {
        builder.run()
        val commits = collater.collate(builder.toList())
        val grid = commits.toFlowGrid()
        assertThat(grid.width).isEqualTo(2)
        assertThat(grid.height).isEqualTo(2)
        val expected = """
        |RB |LB |
        |LL |LR |""".trimIndent()
        assertThat(grid.toString()).isEqualTo(expected)

    }
}