package za.co.wethinkcode.core

import org.buildobjects.process.StreamConsumer
import java.io.IOException
import java.io.InputStream

class BashStreamConsumer : StreamConsumer {
    var thread: Thread? = null
    var saver: StreamSaver? = null

    @Throws(IOException::class)
    override fun consume(stream: InputStream) {
        saver = StreamSaver(stream)
        thread = Thread(saver)
        //        thread.setDaemon(true);
        thread!!.start()
    }
}
