package za.co.wethinkcode.jnitpick

import javafx.application.Application
import tornadofx.*


class MyMainView : View() {
    override val root = stackpane {

    }
}

class HelloWorldApp : App() {
    override val primaryView = MyMainView::class

}

fun main(args: Array<String>) {
    Application.launch(HelloWorldApp::class.java, *args)
}