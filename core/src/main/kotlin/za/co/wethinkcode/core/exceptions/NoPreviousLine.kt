package za.co.wethinkcode.core.exceptions

class NoPreviousLine : RuntimeException("The BashInterpreter has tried to go backwards past the beginning of the file.")
