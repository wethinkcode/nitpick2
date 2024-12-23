package za.co.wethinkcode.core

class BashResult(
    val command: String,
    val code: Int,
    stdout: List<String>,
    stderr: List<String>
) {
    val stdout: LinesIterator = LinesIterator(stdout)
    val stderr: LinesIterator = LinesIterator(stderr)
}
