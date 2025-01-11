package za.co.wethinkcode.vnitpick

import androidx.compose.runtime.mutableIntStateOf
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class TestGui {

    val x = mutableIntStateOf(5)

    @Test
    fun `test something in core`() {
        assertThat(true).isEqualTo(true)
    }

    @Test
    fun `can test stuff using mutable state`() {
        x.value = 1
        assertThat(x.value).isEqualTo(1)
    }
}