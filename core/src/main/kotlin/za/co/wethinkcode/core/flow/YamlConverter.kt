package za.co.wethinkcode.core.flow

import org.yaml.snakeyaml.Yaml

class YamlConverter {

    fun convert(texts: List<String>): List<FlowDetail> {
        val entries = mutableListOf<FlowDetail>()
        texts.forEach { text ->
            entries.add(convert(text))
        }
        return entries
    }

    fun convert(decoded: String): FlowDetail {
        val map = (Yaml().load<Any?>(decoded) ?: return notValidYaml()) as? Map<*, *> ?: return notValidYaml()
        val messages = mutableListOf<String>()
        val typeString = forceString(TYPE_KEY, map, MISSING_TYPE, messages)
        val type = typeFromString(typeString, messages)
        val branch = forceString(BRANCH_KEY, map, MISSING_BRANCH, messages)
        val timestamp = forceString(TIMESTAMP_KEY, map, MISSING_TIMESTAMP, messages)
        val email = forceString(EMAIL_KEY, map, MISSING_EMAIL, messages)
        val committer = forceString(COMMITTER_KEY, map, MISSING_COMMITTER, messages)
        val passes = stringListFrom(map, PASSES_KEY)
        val fails = stringListFrom(map, FAILS_KEY)
        val disables = stringListFrom(map, DISABLES_KEY)
        val aborts = stringListFrom(map, ABORTS_KEY)
        return FlowDetail(
            branch,
            type,
            timestamp,
            committer,
            email,
            passes,
            fails,
            disables,
            aborts,
            messages
        )
    }

    private fun forceString(key: String, map: Map<*, *>, message: String, errors: MutableList<String>): String {
        if (!map.contains(key) || map[key] !is String) {
            errors += message
            return UNKNOWN
        }
        return map[key] as String
    }

    private fun notValidYaml(): FlowDetail {
        return FlowDetail(
            UNKNOWN, RunType.error, UNKNOWN, UNKNOWN, UNKNOWN, emptyList(), emptyList(), emptyList(),
            emptyList(), listOf(INVALID_YAML)
        )
    }

    private fun stringListFrom(map: Map<*, *>, key: String): List<String> {
        val fieldAny = map[key] ?: return emptyList()
        if (fieldAny !is List<*>) return emptyList()
        val result = mutableListOf<String>()
        for (item in fieldAny) result.add(item.toString())
        return result
    }

    private fun typeFromString(typeString: String, errors: MutableList<String>): RunType {
        return try {
            RunType.valueOf(typeString)
        } catch (ignored: Exception) {
            errors += UNKNOWN_TYPE
            RunType.unknown
        }
    }

    companion object {
        const val UNKNOWN: String = "Unknown"
        const val INVALID_YAML = "No YAML found."
        const val MISSING_TYPE = "Missing type field."
        const val MISSING_BRANCH = "Missing branch field."
        const val MISSING_COMMITTER = "Missing committer field."
        const val MISSING_EMAIL = "Missing email field."
        const val MISSING_TIMESTAMP = "Missing timestamp field."
        const val UNKNOWN_TYPE = "Unknown type value"

        const val PASSES_KEY: String = "passes"
        const val FAILS_KEY: String = "fails"
        const val DISABLES_KEY: String = "disables"
        const val ABORTS_KEY: String = "aborts"
        const val BRANCH_KEY: String = "branch"
        const val EMAIL_KEY: String = "email"
        const val COMMITTER_KEY: String = "committer"
        const val TIMESTAMP_KEY: String = "timestamp"
        const val TYPE_KEY: String = "type"
    }
}
