package za.co.wethinkcode.core

import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.util.*

class StreamSaver(val stream: InputStream) : Runnable {
    val output: ByteArrayOutputStream = ByteArrayOutputStream()
    val lines: ArrayList<String> = ArrayList()

    override fun run() {
        try {
            val s = Scanner(stream)
            while (s.hasNextLine()) {
                lines.add(s.nextLine())
            }
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}
