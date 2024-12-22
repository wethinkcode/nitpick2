package za.co.wethinkcode.server

import io.javalin.Javalin
import io.javalin.http.staticfiles.Location

class Server {
}

fun main() {
    val app = Javalin.create { config ->
        config.staticFiles.add { staticFiles ->
            staticFiles.hostedPath = "/"                    // change to host files on a subpath, like '/assets'
            staticFiles.directory = "vnitpick/output"               // the directory where your files are located
            staticFiles.location = Location.EXTERNAL
        }
    }
        .start(3000)
}