package za.co.wethinkcode.vnitpick

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import kotlin.io.path.Path

class ProjectsModelTest {
    val model = ProjectsModel()

    @Test
    fun `empty model has no projects and no current project`() {
        assertThat(model.projects).isEmpty()
        assertThat(model.currentProject.value).isNull()
        assertThat(model.currentProjectIndex.value).isEqualTo(-1)
    }

    @Test
    fun `add adds and selects`() {
        val project = ProjectModel(Path("."))
        model.add(project)
        assertThat(model.projects).containsExactly(project)
        assertThat(model.currentProjectIndex.value).isEqualTo(0)
        assertThat(model.currentProject.value).isEqualTo(project)
    }

    @Test
    fun `select changes selection`() {
        val one = ProjectModel(Path("one"))
        val two = ProjectModel(Path("two"))
        model.add(one)
        model.add(two)
        assertThat(model.currentProject.value).isEqualTo(two)
        model.select(0)
        assertThat(model.currentProject.value).isEqualTo(one)
    }

    @Test
    fun `next and previous no-op on empty`() {
        model.nextProject()
        assertThat(model.currentProject.value).isNull()
        model.previousProject()
        assertThat(model.currentProject.value).isNull()
    }

    @Test
    fun `next and previous increment and decrement`() {
        val one = ProjectModel(Path("one"))
        val two = ProjectModel(Path("two"))
        model.add(one)
        model.add(two)
        assertThat(model.currentProject.value).isEqualTo(two)
        model.previousProject()
        assertThat(model.currentProject.value).isEqualTo(one)
        model.nextProject()
        assertThat(model.currentProject.value).isEqualTo(two)
    }

    @Test
    fun `close no-ops on invalid index`() {
        val one = ProjectModel(Path("one"))
        model.add(one)
        model.close(-1)
        assertThat(model.currentProject.value).isEqualTo(one)
        model.close(1)
        assertThat(model.currentProject.value).isEqualTo(one)
    }

    @Test
    fun `close removes single project`() {
        val one = ProjectModel(Path("one"))
        model.add(one)
        model.close(0)
        assertThat(model.currentProject.value).isNull()
        assertThat(model.currentProjectIndex.value).isEqualTo(-1)
        assertThat(model.projects).isEmpty()
    }

    @Test
    fun `close on unselected project right of current works`() {
        val one = ProjectModel(Path("one"))
        val two = ProjectModel(Path("two"))
        model.add(one)
        model.add(two)
        model.select(0)
        model.close(1)
        assertThat(model.currentProject.value).isEqualTo(one)
        assertThat(model.currentProjectIndex.value).isEqualTo(0)
        assertThat(model.projects).containsExactly(one)
    }

    @Test
    fun `close on unselected project left of current changes selection`() {
        val one = ProjectModel(Path("one"))
        val two = ProjectModel(Path("two"))
        model.add(one)
        model.add(two)
        model.select(1)
        model.close(0)
        assertThat(model.currentProject.value).isEqualTo(two)
        assertThat(model.currentProjectIndex.value).isEqualTo(0)
        assertThat(model.projects).containsExactly(two)
    }

    @Test
    fun `close on selected project changes selection`() {
        val one = ProjectModel(Path("one"))
        val two = ProjectModel(Path("two"))
        model.add(one)
        model.add(two)
        model.select(0)
        model.close(0)
        assertThat(model.currentProject.value).isEqualTo(two)
        assertThat(model.currentProjectIndex.value).isEqualTo(0)
        assertThat(model.projects).containsExactly(two)
    }
}