package za.co.wethinkcode.core

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import java.util.*

class LinesIteratorTest {
    @Test
    fun nothingButHasNextWorksOnEmpty() {
        val lines = LinesIterator(emptyList())
        Assertions.assertThat(lines.hasNext()).isFalse()
        org.junit.jupiter.api.Assertions.assertThrows(
            NoNextLine::class.java
        ) { lines.next() }
        org.junit.jupiter.api.Assertions.assertThrows(
            NoPreviousLine::class.java
        ) { lines.backwards() }
        org.junit.jupiter.api.Assertions.assertThrows(
            NoNextLine::class.java
        ) { lines.peek() }
    }

    @Test
    fun hasNextAndNextWork() {
        val lines = LinesIterator(listOf("first"))
        Assertions.assertThat(lines.hasNext()).isTrue()
        Assertions.assertThat(lines.next()).isEqualTo("first")
        Assertions.assertThat(lines.hasNext()).isFalse()
    }

    @Test
    fun nextAdvances() {
        val lines = LinesIterator(makeList("first", "second"))
        Assertions.assertThat(lines.next()).isEqualTo("first")
        Assertions.assertThat(lines.next()).isEqualTo("second")
        Assertions.assertThat(lines.hasNext()).isFalse()
    }

    @Test
    fun backwardsBacksUp() {
        val lines = LinesIterator(makeList("first", "second"))
        Assertions.assertThat(lines.next()).isEqualTo("first")
        lines.backwards()
        Assertions.assertThat(lines.next()).isEqualTo("first")
        Assertions.assertThat(lines.next()).isEqualTo("second")
    }

    @Test
    fun peekDoesntChangeNext() {
        val lines = LinesIterator(makeList("first", "second"))
        Assertions.assertThat(lines.peek()).isEqualTo("first")
        Assertions.assertThat(lines.next()).isEqualTo("first")
        Assertions.assertThat(lines.next()).isEqualTo("second")
    }

    fun makeList(vararg strings: String): List<String> {
        return Arrays.stream(strings).toList()
    }
}
