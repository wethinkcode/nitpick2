package za.co.wethinkcode.core

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import za.co.wethinkcode.core.exceptions.EndException

class EndExceptionTest {
    var outputter: CollectingOutputter = CollectingOutputter()

    @Test
    fun outputsExceptionsThatArentEndExceptions() {
        EndException.runCommandSafely(outputter) {
            throw RuntimeException("Error.")
        }
        Assertions.assertThat(outputter.messages).isNotEmpty()
        Assertions.assertThat(outputter.messages[0]!!.type).isEqualTo(MessageType.Exception)
    }
}
