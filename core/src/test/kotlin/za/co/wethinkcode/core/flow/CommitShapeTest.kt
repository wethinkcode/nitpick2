package za.co.wethinkcode.core.flow

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CommitShapeTest {

    val runs = RunsBuilder()

    @Test
    fun `empty commit at left`() {
        runs.commit()
        val commit = runs.commits[0]
        val lastUpperRight = FlowPoint(0, 0)
        val shape = CommitShape(commit, lastUpperRight)
        assertThat(shape.x).isEqualTo(0)
        assertThat(shape.width).isEqualTo(1)
        assertThat(shape.height).isEqualTo(2)
    }

    @Test
    fun `empty commit not at left`() {
        runs.commit()
        val commit = runs.commits[0]
        val lastUpperRight = FlowPoint(5, 0)
        val shape = CommitShape(commit, lastUpperRight)
        assertThat(shape.x).isEqualTo(5)
        assertThat(shape.width).isEqualTo(1)
        assertThat(shape.height).isEqualTo(2)
    }

    @Test
    fun `empty commit honors left-most column`() {
        runs.commit()
        val commit = runs.commits[0]
        val lastUpperRight = FlowPoint(0, 5)
        val shape = CommitShape(commit, lastUpperRight)
        assertThat(shape.x).isEqualTo(0)
        assertThat(shape.width).isEqualTo(1)
        assertThat(shape.height).isEqualTo(6)
    }

    @Test
    fun `one run wide commit at left`() {
        runs.run()
        runs.commit()
        val commit = runs.commits[0]
        commit.add(runs[0])
        val lastUpperRight = FlowPoint(0, 0)
        val shape = CommitShape(commit, lastUpperRight)
        assertThat(shape.x).isEqualTo(0)
        assertThat(shape.width).isEqualTo(2)
        assertThat(shape.height).isEqualTo(2)
    }

    @Test
    fun `taller test makes taller commit`() {
        runs.test(3)
        runs.commit()
        val commit = runs.commits[0]
        commit.add(runs[0])
        val lastUpperRight = FlowPoint(0, 4)
        val shape = CommitShape(commit, lastUpperRight)
        assertThat(shape.x).isEqualTo(0)
        assertThat(shape.width).isEqualTo(2)
        assertThat(shape.height).isEqualTo(5)
    }
}