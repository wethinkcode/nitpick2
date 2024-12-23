package za.co.wethinkcode.core

enum class MessageType {
    Exception,  // an unhandled exception was thrown. VERY BAD PROGRAm. NO BISCUIT!!
    Error,  // A process error occurred
    ResultPass,  // Indicates the student passed
    ResultFail,  // Indicates the student failed
    Comment,  // Gives commentary on the submission
    Warning,  // A comment indicating that the submission won't pass yet
    StdOut,  // A captured section of output
    StdErr,  // A captured section of error output
    Altered,  // A message about an altered file that was not to be altered.
    Unknown, NoType,  // A well-formed message we don't recognize from this list
}
