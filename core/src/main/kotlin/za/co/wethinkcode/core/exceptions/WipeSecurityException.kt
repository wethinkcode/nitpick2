package za.co.wethinkcode.core.exceptions

import za.co.wethinkcode.core.Outputter
import java.nio.file.Path

class WipeSecurityException(outputter: Outputter?, root: Path, cause: Throwable?) : EndException(
    outputter!!, text(root), cause
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
