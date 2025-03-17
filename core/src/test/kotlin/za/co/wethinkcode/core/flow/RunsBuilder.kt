package za.co.wethinkcode.core.flow

class RunsBuilder : MutableList<FlowDetail> by mutableListOf() {
    var sequence = 1
    val commits = Commits()

    fun commit(altSequence: Int = sequence) {
        val run = FlowDetail("main", RunType.commit, "$altSequence", "geepaw", "geepawmail")
        commits.add(Commit(run))
        sequence += 1
    }

    fun run(altSequence: Int = sequence): FlowDetail {
        val run = FlowDetail("main", RunType.run, "$altSequence", "geepaw", "geepawmail")
        this.add(run)
        sequence += 1
        return run
    }

    fun test(testCount: Int, altSequence: Int = sequence): FlowDetail {
        val passList = mutableListOf<String>()
        (0 until testCount).forEach {
            passList += "Test $it"
        }
        val run = FlowDetail(
            "main", RunType.test, "$altSequence", "geepaw", "geepawmail",
            passList
        )
        this.add(run)
        sequence += 1
        return run
    }
}