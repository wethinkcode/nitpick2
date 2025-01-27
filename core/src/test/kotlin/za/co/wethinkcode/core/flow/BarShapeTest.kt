package za.co.wethinkcode.core.flow

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.awt.Color

class BarShapeTest {

    val runs = RunsBuilder()

    @Test
    fun `at left with no left neighbor`() {
        runs.run()
        val lastUpperRight = FlowPoint(0, 0)
        val shape = BarShape(runs[0], lastUpperRight, Color.GREEN, Color.GREEN)
        assertThat(shape.x).isEqualTo(0)
        assertThat(shape.width).isEqualTo(1)
        assertThat(shape.height).isEqualTo(1)
    }

    @Test
    fun `at left with taller left neighbor`() {
        runs.run()
        val lastUpperRight = FlowPoint(0, 4)
        val shape = BarShape(runs[0], lastUpperRight, Color.GREEN, Color.GREEN)
        assertThat(shape.x).isEqualTo(0)
        assertThat(shape.width).isEqualTo(1)
        assertThat(shape.height).isEqualTo(4)
    }

    @Test
    fun `honors lastUpperRight on x axis`() {
        runs.run()
        val lastUpperRight = FlowPoint(5, 4)
        val shape = BarShape(runs[0], lastUpperRight, Color.GREEN, Color.GREEN)
        assertThat(shape.x).isEqualTo(5)
        assertThat(shape.width).isEqualTo(1)
        assertThat(shape.height).isEqualTo(4)
    }

}

