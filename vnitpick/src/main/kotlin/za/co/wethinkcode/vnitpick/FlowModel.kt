package za.co.wethinkcode.vnitpick

import androidx.compose.runtime.mutableStateListOf
import za.co.wethinkcode.core.parse.Base64Loader
import za.co.wethinkcode.core.parse.LogCollater
import za.co.wethinkcode.core.parse.YamlConverter
import java.nio.file.Path

class FlowModel {

    val raw = mutableStateListOf("")

    fun load(path: Path) {
        val yamls = Base64Loader().load(path.resolve(".jltk"))
        raw.clear()
        yamls.forEach { yaml -> raw.add(yaml) }
        val entries = YamlConverter().convert(yamls)
        val commits = LogCollater().collate(entries)

    }
}

const val FLOW_SIZE = 20f
