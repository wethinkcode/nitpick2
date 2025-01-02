package za.co.wethinkcode.core

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import za.co.wethinkcode.core.exceptions.EndException

class EndExceptionTest {
    var reporter = CollectingReporter()

    @Test
    fun outputsExceptionsThatArentEndExceptions() {
        EndException.runCommandSafely(reporter) {
            throw RuntimeException("Error.")
        }
        Assertions.assertThat(reporter.messages).isNotEmpty()
        Assertions.assertThat(reporter.messages[0].type).isEqualTo(MessageType.Exception)
    }
}
