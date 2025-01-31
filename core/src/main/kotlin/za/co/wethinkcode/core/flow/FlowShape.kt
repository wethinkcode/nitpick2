package za.co.wethinkcode.core.flow


interface FlowShape {
    val x: Int
    val detail: FlowDetail
    val tip: String
    val titleBlock: String
        get() = when (detail.type) {
            RunType.base64 -> "Encoding Error"
            RunType.run -> "Main Program Run"
            RunType.commit -> "Git Commit"
            RunType.local -> "Uncommitted Files"
            RunType.test -> "JUnit Run"
            RunType.error -> "Error"
            RunType.unknown -> "Unknown Entry"
        }
}