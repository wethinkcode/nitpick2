package za.co.wethinkcode.core

class LinesIterator(private val source: List<String>) {
    private var next = 0

    fun hasNext(): Boolean {
        return next < source.size
    }

    fun next(): String {
        if (hasNext()) return source[next++]
        throw NoNextLine()
    }

    fun backwards() {
        if (next > 0) next -= 1
        else throw NoPreviousLine()
    }

    fun peek(): String {
        if (hasNext()) return source[next]
        throw NoNextLine()
    }
}
