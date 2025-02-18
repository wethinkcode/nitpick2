package za.co.wethinkcode.core.flow

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CollatedTestsTest {
    val results = CollatedTests()

    @Test
    fun `tests are sorted by time of addition`() {
        results.begin()
        results.add("b", TestStatus.pass)
        results.add("a", TestStatus.pass)
        val actual = results.toList()
        assertThat(actual[0].name).isEqualTo("b")
        assertThat(actual[1].name).isEqualTo("a")
    }

    @Test
    fun `tests are sorted by time of addition regardless of second ordering`() {
        results.begin()
        results.add("b", TestStatus.pass)
        results.add("a", TestStatus.pass)
        results.begin()
        results.add("a", TestStatus.pass)
        results.add("b", TestStatus.pass)
        val actual = results.toList()
        assertThat(actual[0].name).isEqualTo("b")
        assertThat(actual[1].name).isEqualTo("a")
    }

    @Test
    fun `new tests are new`() {
        results.begin()
        results.add("new", TestStatus.pass)
        assertThat(results.toList()[0].isNew).isTrue()
    }

    @Test
    fun `old tests are old`() {
        results.begin()
        results.add("old", TestStatus.pass)
        results.begin()
        results.add("old", TestStatus.pass)
        assertThat(results.toList()[0].isNew).isFalse()
    }

    @Test
    fun `tests marked unrun after second begin`() {
        results.begin()
        results.add("old", TestStatus.pass)
        results.begin()
        val actual = results.toList()
        assertThat(actual[0].name).isEqualTo("old")
        assertThat(actual[0].status).isEqualTo(TestStatus.unrun)
        assertThat(actual[0].isNew).isFalse()
    }

}