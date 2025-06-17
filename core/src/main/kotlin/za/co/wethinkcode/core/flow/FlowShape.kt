package za.co.wethinkcode.core.flow


interface FlowShape {

    enum class Kind {
        base64,
        error,
        unknown,
        run,
        commit,
        local,
        failed,
        passed,
        disabled,
        aborted,
        unrun,
    }

    val kind: Kind
    val x: Int
    val y: Int
    val width: Int
    val height: Int
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