package za.co.wethinkcode.core

import java.nio.file.FileSystems
import java.nio.file.PathMatcher
import java.util.function.Predicate

class MatchAllButGit : Predicate<Subpath> {
    var matcher: PathMatcher = FileSystems.getDefault().getPathMatcher("glob:.git")

    override fun test(subpath: Subpath): Boolean {
        return !matcher.matches(subpath.path)
    }
}
