package za.co.wethinkcode.core.exceptions

import za.co.wethinkcode.core.Message
import za.co.wethinkcode.core.MessageType
import za.co.wethinkcode.core.Reporter
import java.nio.file.Path

open class EndException : RuntimeException {
    constructor()

    constructor(reporter: Reporter, text: String, cause: Throwable) : super(text, cause) {
        reporter.add(Message(MessageType.Error, text))
    }

    constructor(message: String) : super(message)

    companion object {
        fun runCommandSafely(reporter: Reporter, runnable: Runnable) {
            try {
                runnable.run()
            } catch (ignored: EndException) {
                // Used when a problem is reported on the outputter and we need to quit
            } catch (unhandled: Throwable) {
                val printableTrace = makePrintableStrackTrace(unhandled)
                // Used when an unexpected exception is thrown
                reporter.add(
                    Message(
                        MessageType.Exception,
                        """
                        An unhandled exception occurred. Please report this to support!
                        
                        $printableTrace
                        """.trimIndent()
                    )
                )
            }
        }

        fun runCommandSafely(reporter: Reporter, runnable: Runnable, submissionPath: Path) {
            try {
                runnable.run()
            } catch (ignored: EndException) {
                // Used when a problem is reported on the outputter and we need to quit
            } catch (unhandled: Throwable) {
                val printableTrace = makePrintableStrackTrace(unhandled)
                // Used when an unexpected exception is thrown
                reporter.add(
                    Message(
                        MessageType.Exception,
                        """
                        An unhandled exception occurred. Please report this to support!
                        
                        $printableTrace
                        """.trimIndent()
                    )
                )
            }
            reporter.saveResults(submissionPath)
        }

        private fun makePrintableStrackTrace(unhandled: Throwable): String {
            val traces = ArrayList<String?>()
            traces.add(unhandled.message)
            val trace = unhandled.stackTrace
            for (element in trace) {
                traces.add(element.toString())
            }
            return java.lang.String.join("\n", traces)
        }
    }
}
