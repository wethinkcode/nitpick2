package za.co.wethinkcode.core

import java.nio.file.FileSystems
import java.nio.file.PathMatcher
import java.util.*
import java.util.function.Predicate

class MatchByGlob(globStrings: List<String>) : Predicate<Subpath> {
    private val globs: MutableList<PathMatcher> = ArrayList()

    constructor(vararg globStrings: String) : this(Arrays.stream<String>(globStrings).toList())

    init {
        val system = FileSystems.getDefault()
        for (globString in globStrings) {
            globs.add(system.getPathMatcher("glob:$globString"))
        }
    }

    override fun test(subpath: Subpath): Boolean {
        if (subpath.isFolder) return true
        for (glob in globs) {
            if (glob.matches(subpath.path)) return true
        }
        return false
    }
}
