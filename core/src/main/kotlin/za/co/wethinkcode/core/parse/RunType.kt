package za.co.wethinkcode.core.parse

enum class RunType {
    base64, // parse error on base64 step
    run,    // main was run
    commit, // a commit happened
    local,  // a psuedo-commit happened, i.e. runs with no commit
    test,   // one or more tests were run
    error,  // some parsing error happened
    unknown // well-formed, don't recognize type
}