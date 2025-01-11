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
    }

    @Test
    fun `add adds and selects`() {
        val project = Project(Path("."))
        model.add(project)
        assertThat(model.projects).containsExactly(project)
        assertThat(model.currentProjectIndex.value).isEqualTo(0)
        assertThat(model.currentProject.value).isEqualTo(project)
    }
}