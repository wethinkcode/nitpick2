package za.co.wethinkcode.core

import java.io.PrintWriter
import java.nio.file.Path

class CollectingOutputter : Outputter {
    var grade: Grade = Grade.GradeNone

    var messages: MutableList<Message?> = ArrayList()
    override fun add(message: Message?) {
        messages.add(message)
        println(message!!.type)
        println(DASHES.substring(0, message.type.toString().length))
        println(message.content)
        println()
        when (message.type) {
            MessageType.ResultPass -> saveNewGrade(Grade.GradePass)
            MessageType.Altered -> saveNewGrade(Grade.GradeAltered)
            MessageType.ResultFail -> saveNewGrade(Grade.GradeFail)
            MessageType.Exception, MessageType.Error -> saveNewGrade(Grade.GradeError)
            else -> {}
        }
    }

    fun saveNewGrade(newGrade: Grade) {
        // error trumps all
        if (newGrade == Grade.GradeError) {
            grade = newGrade
            return
        }
        if (newGrade == Grade.GradeAltered) {
            grade = newGrade
            return
        }
        // don't change if previous was error or altered
        if (grade == Grade.GradeError) return
        if (grade == Grade.GradeAltered) return

        // Notice multiple grades.
        if (grade != Grade.GradeNone) {
            grade = Grade.GradeMultiple
            return
        }
        grade = newGrade
    }

    override fun saveResults(results: Path?) {
        val longResults = results!!.resolve(LONG_RESULT_LEAF)
        val printer: PrintWriter = FileUtility.openForWriting(longResults, "Cannot save long results.")
        for (message in messages) {
            printer.println(message!!.type)
            printer.println(DASHES.substring(0, message!!.type.toString().length))
            printer.println(message!!.content)
            printer.println()
        }
        printer.flush()
        printer.close()
    }

    override fun saveGrade(results: Path?) {
        val gradeResults = results!!.resolve(GRADE_RESULT_LEAF)
        val printer: PrintWriter = FileUtility.openForWriting(gradeResults, "Cannot save grade results.")
        printer.println(grade)
        printer.flush()
        printer.close()
    }

    companion object {
        private const val DASHES = "-----------------------------------"
    }
}
