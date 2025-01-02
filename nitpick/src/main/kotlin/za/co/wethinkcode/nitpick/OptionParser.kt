package za.co.wethinkcode.nitpick

import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.CommandLineParser
import org.apache.commons.cli.DefaultParser
import org.apache.commons.cli.HelpFormatter
import org.apache.commons.cli.Option
import org.apache.commons.cli.Options
import org.apache.commons.cli.ParseException
import za.co.wethinkcode.core.EdgePick
import za.co.wethinkcode.core.Generate
import za.co.wethinkcode.core.LmsPick
import za.co.wethinkcode.core.Reporter
import za.co.wethinkcode.core.Version
import java.io.ByteArrayOutputStream
import java.io.PrintWriter

class OptionParser(val reporter: Reporter) {
    val options: Options
    val usage: String

    init {
        options = makeOptions()
        usage = makeUsage()
    }

    fun makeUsage(): String {
        val formatter = HelpFormatter()
        val usageStream = ByteArrayOutputStream()
        val writer = PrintWriter(usageStream)
        formatter.printHelp(
            writer, 80, "nitpick <generate|pick>",
            """
                                                
                        Available commands:
                         - generate: generate submission skeleton from the specified exercise
                         - pick: evaluate the submission folder and print out the evaluation
                         
                        
                         
                        """.trimIndent(),
            options, 0, 0, ""
        )
        writer.flush()
        writer.close()
        return usageStream.toString()
    }

    fun parse(vararg args: String?): Runnable {
        val parser: CommandLineParser = DefaultParser()
        return try {
            val commandLine = parser.parse(options, args)
            semanticParse(commandLine)
        } catch (exp: ParseException) {
            help("Unknown command-line failure.")
        }
    }

    fun semanticParse(commandLine: CommandLine): Runnable {
        if (commandLine.hasOption("help")) return help("Help Information")
        if (commandLine.hasOption("version")) return Version()
        val commands = commandLine.getArgs()
        if (commands.size < 1) return help("No command given.\n")
        if (commands.size > 1) return help("Only one command at a time.\n")
        val command = commands[0]
        return when (command) {
            "pick" -> makePick(commandLine)
            "generate" -> makeGenerate(commandLine)
            else -> help("Unknown command: [$command]\n")
        }
    }

    fun makePick(commandLine: CommandLine): Runnable {
        val submissionString = guessSubmissionString(commandLine)
        val shouldOverwrite = commandLine.hasOption("overwrite")
        if (commandLine.hasOption(LMS_FLAG_LONG)) {
            val authorString = commandLine.getOptionValue(LMS_FLAG_LONG)
            return LmsPick(authorString, submissionString, true, reporter)
        }
        return EdgePick(submissionString, shouldOverwrite, reporter)
    }

    private fun guessSubmissionString(commandLine: CommandLine): String {
        return if (!commandLine.hasOption("submission")) {
            "."
        } else commandLine.getOptionValue("submission")
    }

    fun makeGenerate(commandLine: CommandLine): Runnable {
        val lmsString = guessLmsString(commandLine)
        val destinationString = guessDestinationString(commandLine)
        return Generate(
            lmsString,
            destinationString, reporter
        )
    }

    private fun guessLmsString(commandLine: CommandLine): String {
        return if (!commandLine.hasOption(LMS_FLAG_LONG)) "." else commandLine.getOptionValue(LMS_FLAG_LONG)
    }

    private fun guessDestinationString(commandLine: CommandLine): String {
        return if (commandLine.hasOption("destination")) commandLine.getOptionValue("destination") else Generate.NO_DESTINATION_GIVEN
    }

    fun help(message: String): Runnable {
        return Help(message, usage)
    }

    fun makeOptions(): Options {
        val options = Options()
        options.addOption(
            Option.builder("h")
                .longOpt("help")
                .desc("get more detailed help.")
                .build()
        )
        options.addOption(
            Option.builder("v")
                .longOpt("version")
                .desc("Print the nitpick version.")
                .build()
        )
        options.addOption(
            Option.builder("s")
                .longOpt("submission")
                .argName("submission")
                .hasArg()
                .desc("the path to the submission folder.")
                .build()
        )
        options.addOption(
            Option.builder("l")
                .longOpt("lms")
                .argName("lms")
                .hasArg()
                .desc("the path to the lms exercise folder.")
                .build()
        )
        options.addOption(
            Option.builder("d")
                .longOpt("destination")
                .argName("destination")
                .hasArg()
                .desc("the path to the destination folder.")
                .build()
        )
        options.addOption(
            Option.builder("o")
                .longOpt("overwrite")
                .desc("overwrite any altered files.")
                .build()
        )
        return options
    }

    companion object {
        private const val LMS_FLAG = "l"
        private const val LMS_FLAG_LONG = "lms"
    }
}
