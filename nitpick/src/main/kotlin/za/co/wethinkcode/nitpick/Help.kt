package za.co.wethinkcode.nitpick

class Help internal constructor(private val message: String, private val usage: String) : Runnable {
    override fun run() {
        println(message)
        println(usage)
    }
}
