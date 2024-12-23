package za.co.wethinkcode.core

@JvmRecord
data class Message(val type: MessageType, val content: String)
