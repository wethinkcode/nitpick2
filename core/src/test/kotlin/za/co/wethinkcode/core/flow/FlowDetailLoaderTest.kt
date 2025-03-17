package za.co.wethinkcode.core.flow

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class FlowDetailLoaderTest {
    val runs = RunsBuilder()
    val loader = FlowDetailLoader()

    @Test
    fun `one commit yields one commit with no runs`() {
        runs.commit()
        loader.putDetailsInCommits(runs.toList(), runs.commits)
        assertThat(runs.commits).hasSize(1)
        assertThat(runs.commits[0].detail.type).isEqualTo(RunType.commit)
        assertThat(runs.commits[0]).isEmpty()
    }

    @Test
    fun `empty yields empty`() {
        loader.putDetailsInCommits(runs.toList(), runs.commits)
        assertThat(runs.commits).isEmpty()
    }

    @Test
    fun `one run one commit yields one Commit`() {
        runs.run()
        runs.commit()
        loader.putDetailsInCommits(runs.toList(), runs.commits)
        assertThat(runs.commits).hasSize(1)
        assertThat(runs.commits[0].detail.type).isEqualTo(RunType.commit)
        assertThat(runs.commits[0][0].type).isEqualTo(RunType.run)
    }

    @Test
    fun `run commit run commit forms two commits`() {
        runs.run()
        runs.commit()
        runs.run()
        runs.commit()
        loader.putDetailsInCommits(runs.toList(), runs.commits)
        assertThat(runs.commits).hasSize(2)
        assertThat(runs.commits[0][0].type).isEqualTo(RunType.run)
        assertThat(runs.commits[0][0].timestamp).isEqualTo("1")
        assertThat(runs.commits[1][0].type).isEqualTo(RunType.run)
    }

    @Test
    fun `makes local commit if needed`() {
        runs.run()
        runs.run()
        loader.putDetailsInCommits(runs.toList(), runs.commits)
        assertThat(runs.commits).hasSize(1)
        assertThat(runs.commits[0].detail.type).isEqualTo(RunType.local)
        assertThat(runs.commits[0].size).isEqualTo(2)
    }
}