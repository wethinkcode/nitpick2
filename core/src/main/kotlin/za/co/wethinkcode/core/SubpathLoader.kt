package za.co.wethinkcode.core

import za.co.wethinkcode.core.exceptions.EndException
import java.io.IOException
import java.nio.file.FileVisitResult
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.SimpleFileVisitor
import java.nio.file.attribute.BasicFileAttributes
import java.util.function.Predicate

class SubpathLoader(private val root: Path, private val filter: Predicate<Subpath>) : SimpleFileVisitor<Path>() {
    internal class LongestFirst : Comparator<Subpath> {
        override fun compare(o1: Subpath, o2: Subpath): Int {
            return o1.path.compareTo(o2.path)
        }
    }

    private val results: MutableList<Subpath> = ArrayList()

    @Throws(IOException::class)
    override fun preVisitDirectory(dir: Path, attrs: BasicFileAttributes): FileVisitResult {
        if (dir !== root) {
            val subpath = Subpath(extractSubPath(dir), true)
            if (globalIgnore(subpath)) {
                return FileVisitResult.SKIP_SUBTREE
            }
            if (filter.test(subpath)) {
                results.add(subpath)
                return FileVisitResult.CONTINUE
            }
            return FileVisitResult.SKIP_SUBTREE
        }
        return FileVisitResult.CONTINUE
    }

    fun extractSubPath(path: Path): Path {
        return path.subpath(root.nameCount, path.nameCount)
    }

    @Throws(IOException::class)
    override fun visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult {
        if (file !== root) {
            val subpath = Subpath(extractSubPath(file), false)
            if (globalIgnore(subpath)) {
                return FileVisitResult.CONTINUE
            }
            if (filter.test(subpath)) {
                results.add(subpath)
            }
        }
        return FileVisitResult.CONTINUE
    }

    fun globalIgnore(path: Subpath): Boolean {
        if (path.path.endsWith("__PYCACHE__")) return true
        return false
    }

    companion object {
        fun fetch(path: Path, filter: Predicate<Subpath>, reporter: Reporter): List<Subpath> {
            try {
                val visitor = SubpathLoader(path, filter)
                Files.walkFileTree(path, visitor)
                visitor.results.sortWith(LongestFirst())
                return visitor.results
            } catch (wrapped: IOException) {
                reporter.add(
                    Message(
                        MessageType.Error, """
     An unexpected error has occurred.
     Please copy/paste this entire message and contact us with the details.
     $wrapped
     """.trimIndent()
                    )
                )
                throw EndException()
            }
        }
    }
}
