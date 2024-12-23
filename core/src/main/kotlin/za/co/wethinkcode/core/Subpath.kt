package za.co.wethinkcode.core

import java.nio.file.Path

@JvmRecord
data class Subpath(val path: Path, val isFolder: Boolean)
