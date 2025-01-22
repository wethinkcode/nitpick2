package za.co.wethinkcode.core.parse

class FlowGrid {
    var _width = -1
    var _height = -1

    val width get() = _width + 1
    val height get() = _height + 1

    val map = mutableMapOf<Pair<Int, Int>, FlowCell>()

    operator fun get(x: Int, y: Int): FlowCell {
        return map.getOrDefault(Pair(x, y), NO_CELL)
    }

    operator fun set(x: Int, y: Int, value: FlowCell) {
        map[Pair(x, y)] = value
        if (x > _width) _width = x
        if (y > _height) _height = y
    }

    override fun toString(): String {
        val builder = StringBuilder()
        for (y in 0 until height) {
            builder.append('|')
            for (x in 0 until width) {
                val invertY = height - 1 - y
                when (get(x, invertY).type) {
                    CellType.Nothing -> builder.append("   |")
                    CellType.CommitLeft -> builder.append("CL |")
                    CellType.FakeCommitLeft -> builder.append("FCL|")
                    CellType.RunBar -> builder.append("RB |")
                    CellType.LocalBar -> builder.append("LB |")
                    CellType.LocalLeft -> builder.append("LL |")
                    CellType.LocalRight -> builder.append("LR |")
                }
            }
            if (y < height - 1) builder.append('\n')
        }
        return builder.toString()
    }
}