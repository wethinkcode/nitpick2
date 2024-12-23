package za.co.wethinkcode.core

import java.io.IOException
import java.nio.file.FileVisitResult
import java.nio.file.Path
import java.nio.file.SimpleFileVisitor
import java.nio.file.attribute.BasicFileAttributes

class DeletingFileVisitor(root: Path) : SimpleFileVisitor<Path>() {
    // MDH: Tested via FileUtilityTest
    private val root: Path = root.toAbsolutePath()

    @Throws(IOException::class)
    override fun postVisitDirectory(dir: Path, exc: IOException): FileVisitResult {
        if (root !== dir) {
            dir.toFile().delete()
        }
        return FileVisitResult.CONTINUE
    }

    @Throws(IOException::class)
    override fun visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult {
        file.toFile().delete()
        return FileVisitResult.CONTINUE
    }
}
