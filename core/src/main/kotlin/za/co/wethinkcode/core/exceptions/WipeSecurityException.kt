package za.co.wethinkcode.core.exceptions

import za.co.wethinkcode.core.Reporter
import java.nio.file.Path

class WipeSecurityException(reporter: Reporter?, root: Path, cause: Throwable?) : EndException(
    reporter!!, text(root), cause
) {
    companion object {
        private fun text(root: Path): String {
            return """
                A security exception occurred while wiping the destination folder.
                [$root]
                
                """.trimIndent()
        }
    }
}
