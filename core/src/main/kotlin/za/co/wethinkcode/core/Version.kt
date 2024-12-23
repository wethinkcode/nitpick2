package za.co.wethinkcode.core

import java.util.*

class Version : Runnable {
    override fun run() {
        println(version)
    }

    companion object {
        val version: String = makeVersionString()

        fun makeVersionString(): String {
            val loader = Thread.currentThread().contextClassLoader
            val props = Properties()
            try {
                loader.getResourceAsStream("nitpick.properties").use { resourceStream ->
                    props.load(resourceStream)
                }
            } catch (e: Exception) {
                return "Unknown Version: Can't find properties file."
            }
            return props["version"].toString()
        }
    }
}
